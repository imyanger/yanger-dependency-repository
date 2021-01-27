package com.yanger.starter.basic.convert;

import com.google.common.collect.Maps;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.tools.web.exception.BasicException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 使用 jackson 作为前端到后端参数反序列化时, 默认是按照索引值处理
 * 这里采用按照 value 处理, 前端只需要传实体枚举对应的 value 值, 后端使用实体枚举接收参数即可, 自动通过 value 转换为枚举
 * 支持 List<>
 * 支持 单独的 value 解析
 * 支持 {"value":1,"desc":"男"} json 解析
 */
@Slf4j
@Data
@SuppressWarnings("unchecked")
@EqualsAndHashCode(callSuper = true)
public class EntityEnumDeserializer<T extends SerializeEnum<?>> extends JsonDeserializer<T> implements ContextualDeserializer {

    /** 当前被处理的枚举类 */
    private Class<T> clz;

    /** 缓存枚举反序列化器 */
    private static final Map<String, EntityEnumDeserializer<? extends SerializeEnum<?>>> DESERIALIZER_MAP = Maps.newConcurrentMap();

    /** 缓存枚举值 */
    private static final Map<String, Map<Serializable, SerializeEnum<?>>> ALL_ENUM_MAP = Maps.newHashMap();

    /** EMPTY_JSON */
    private static final String EMPTY_JSON = "{}";

    /**
     * 反序列化优先级: value > 枚举名 > 枚举索引
     *
     * @param jsonParser json parser
     * @param ctx        ctx
     * @return the entity enum
     * @throws IOException io exception
     */
    @Override
    public T deserialize(@NotNull JsonParser jsonParser, DeserializationContext ctx) throws IOException {
        if (StringUtils.isEmpty(jsonParser.getText())) {
            return null;
        }
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        log.trace("执行自定义枚举反序列化: Enum = {}, valueType = {}, value = {}", this.clz, node.getNodeType(), node.toString());

        Serializable finalValue = getValue(node);

        if (finalValue == null) {
            return null;
        }

        SerializeEnum<?> result = ALL_ENUM_MAP.get(this.clz.getName()).get(finalValue);

        if (result == null) {
            result = SerializeEnum.getEnumByOrder((Class<? extends Enum<?>>) this.clz, finalValue);
            ALL_ENUM_MAP.get(this.clz.getName()).put(finalValue, result);
        }

        return (T) result;
    }

    /**
     * 通过 {@link SerializeEnum#getValue()} 反序列化枚举.
     *
     * @param node node
     * @return the value
     */
    @Contract("null -> fail")
    private static @Nullable Serializable getValue(JsonNode node) {
        // POST 请求传入的 json 字符串中没有 value 节点, 则直接抛出异常
        if (node == null) {
            log.debug("未找到枚举类型的 value 节点");
            return null;
        }
        JsonNodeType jsonNodeType = node.getNodeType();
        Serializable value;
        switch (jsonNodeType) {
            case OBJECT:
                // 如果是 "{}" 类型, 则直接返回 null
                if (EMPTY_JSON.equals(node.asText())) {
                    value = null;
                    break;
                }
                // 枚举的 json 类型, 需要递归解析找出 value
                value = getValue(node.get(SerializeEnum.VALUE_FILED_NAME));
                break;
            case NUMBER:
                // 枚举的 index 或者 value 为 number 类型
                value = node.intValue();
                break;
            case STRING:
                // 枚举名或者 value 为 string 类型
                value = node.asText();
                break;
            case BOOLEAN:
                // value 为 boolean 类型
                value = node.asBoolean();
                break;
            case NULL:
            case ARRAY:
                // todo [暂时不支持的类型]
            case POJO:
                // todo [暂时不支持的类型]
            case BINARY:
                // todo [暂时不支持的类型]
            case MISSING:
            default:
                throw new BasicException("不支持的枚举转换");
        }
        return value;
    }

    /**
     * 获取合适的解析器, 把当前解析的属性 Class 对象存起来, 以便反序列化的转换类型.
     *
     * @param ctx      ctx
     * @param property property
     * @return json deserializer
     */
    @Override
    public JsonDeserializer<?> createContextual(@NotNull DeserializationContext ctx, BeanProperty property) {
        Class<T> rawCls = (Class<T>) ctx.getContextualType().getRawClass();
        // 当 key 存在返回当前 value 值, 不存在执行函数并保存到 map 中
        EntityEnumDeserializer<?> entityEnumJsonDeserializer = DESERIALIZER_MAP.get(rawCls.getName());

        if (entityEnumJsonDeserializer == null) {
            EntityEnumDeserializer<T> deserializer = new EntityEnumDeserializer<>();
            DESERIALIZER_MAP.put(rawCls.getName(), deserializer);

            T[] enums = rawCls.getEnumConstants();
            // 使用 getValue 作为 key 缓存当前枚举类的所有枚举
            Map<Serializable, SerializeEnum<?>> currentEnumMap = Maps.newHashMap();
            for (T e : enums) {
                currentEnumMap.put(e.getValue(), e);
            }

            ALL_ENUM_MAP.put(rawCls.getName(), currentEnumMap);

            deserializer.setClz(rawCls);
            DESERIALIZER_MAP.put(rawCls.getName(), deserializer);

            entityEnumJsonDeserializer = deserializer;
        }

        return entityEnumJsonDeserializer;
    }
}