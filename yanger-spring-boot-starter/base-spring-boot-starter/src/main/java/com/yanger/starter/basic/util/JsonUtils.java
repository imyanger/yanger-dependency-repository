package com.yanger.starter.basic.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigDefaultValue;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.format.ConcurrentDateFormat;
import com.yanger.tools.web.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.reflections.ReflectionUtils.forName;
import static org.reflections.util.Utils.index;

/**
 * <p>Description: Jackson工具类
 * 系统中使用到 {@link ObjectMapper} 的地方定将用此类进行初始化, 提供 2 种方式:
 * 1. {@link JsonUtils#getInstance()};
 * 2. {@link JsonUtils#getCopyMapper()};
 * 第一种方式将返回一个单例对象, 请确保在使用 {@link ObjectMapper} 的过程中不会修改 {@link ObjectMapper} 的配置, 如果需要定制化 {@link ObjectMapper},
 * 可使用第二种方式, 此方式将每次返回一个新的 {@link ObjectMapper} 对象.
 * 此类每个工具方法都进行过重载, 如果需要使用自定义 {@link ObjectMapper} 对 json 处理, 请调用具有 {@link ObjectMapper} 参数的工具方法.
 * </p>
 *
 * @Author yanger
 * @Date 2020/12/18 18:54
 */
@Slf4j
public class JsonUtils {

    /** PATTERN_DATETIME */
    public static final String PATTERN_DATETIME = System.getProperty(ConfigKey.JsonConfigKey.JSON_DATE_FORMAT, ConfigDefaultValue.JsonConfigValue.JSON_DATE_FORMAT);

    /** Empty array */
    public static final byte[] EMPTY_ARRAY = new byte[0];

    /**
     * 返回一个新对象, 避免属性覆盖, 一般用于基于 {@see JacksonHolder.INSTANCE} 基础上进行自定义配置时使用.
     * @return the object mapper
     */
    public static ObjectMapper getCopyMapper() {
        return getInstance().copy();
    }

    /**
     * 获取单例对象, 这里只提供全局的基础配置, 让整个应用配置保持一致, 如果需要其他配置, 请使用 {@link JsonUtils#getCopyMapper()}.
     * @return the instance
     */
    @Contract(pure = true)
    public static ObjectMapper getInstance() {
        return JacksonHolder.INSTANCE;
    }

    /**
     * 判断字符串是否为 json 格式.
     * @param jsonInString json in string
     * @return the boolean
     */
    public static boolean isJson(String jsonInString) {
        return isJson(getInstance(), jsonInString);
    }

    /**
     * 压缩为一行
     * @param json json
     * @return the string
     */
    public static @NotNull String compress(String json) {
        return toJson(json);
    }

    /**
     * 根据自定义 mapper 判断字符串是否为 json 格式.
     * @param mapper       mapper
     * @param jsonInString json in string
     * @return the boolean
     */
    public static boolean isJson(@NotNull ObjectMapper mapper, String jsonInString) {
        try {
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 判断字节数组是否为 json 格式.
     * @param jsonBytes json bytes
     * @return the boolean
     */
    public static boolean isJson(byte[] jsonBytes) {
        return isJson(getInstance(), jsonBytes);
    }

    /**
     * 根据自定义 mapper 判断字节数组是否为 json 格式.
     * @param mapper    mapper
     * @param jsonBytes json bytes
     * @return the boolean
     */
    public static boolean isJson(@NotNull ObjectMapper mapper, byte[] jsonBytes) {
        try {
            mapper.readTree(jsonBytes);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 将对象序列化成 json 字符串.
     * @param object javaBean
     * @return jsonString json 字符串
     */
    @NotNull
    public static String toJson(Object object) {
        return toJson(object, false);
    }

    /**
     * 将对象序列化成 json 字符串, 根据 pretty 判断是否格式化 json.
     * @param object object
     * @param pretty pretty
     * @return the string
     */
    @NotNull
    public static String toJson(Object object, boolean pretty) {
        return toJson(getInstance(), object, pretty);
    }

    /**
     * 根据自定义 mapper 将对象序列化成 json 字符串.
     * @param mapper mapper
     * @param object object
     * @return the string
     */
    public static String toJson(ObjectMapper mapper, Object object) {
        return toJson(mapper, object, false);
    }

    /**
     * 使用自定义的 mapper 将对象序列化成 json 字符串, 根据 pretty 判断是否格式化 json,
     * 如果 object 是 String 类型, 将直接返回原始字符串.
     * @param mapper mapper
     * @param object object
     * @param pretty pretty
     * @return the string
     */
    public static String toJson(ObjectMapper mapper, Object object, boolean pretty) {
        if (object == null) {
            return StringPool.EMPTY_JSON;
        }
        // 如果是 string, 先转为 object 再转为 json, 避免转义字符
        if (object instanceof String) {
            if (isJson(mapper, (String) object)) {
                object = parse((String) object, Object.class);
            } else {
                // 非 json 字符串, 直接返回原始字符串
                return (String) object;
            }
        }

        try {
            String json;
            if (pretty) {
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } else {
                json = mapper.writeValueAsString(object);
            }
            return json;
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将对象序列化成 json byte 数组.
     * @param object javaBean
     * @return jsonString json字符串
     */
    @Contract("null -> new")
    @NotNull
    public static byte[] toJsonAsBytes(Object object) {
        return toJsonAsBytes(getInstance(), object);
    }

    /**
     * 根据自定义 mapper 将对象序列化成 json byte 数组.
     * @param mapper mapper
     * @param object object
     * @return the byte [ ]
     */
    @NotNull
    public static byte[] toJsonAsBytes(ObjectMapper mapper, Object object) {
        if (object == null) {
            return EMPTY_ARRAY;
        }
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将 json 字符串转成 JsonNode.
     * @param jsonString jsonString
     * @return jsonString json字符串
     */
    @NotNull
    public static JsonNode readTree(String jsonString) {
        return readTree(getInstance(), jsonString);
    }

    /**
     * 使用自定义 mapper 将 json 字符串转成 JsonNode.
     * @param mapper     mapper
     * @param jsonString json string
     * @return the json node
     */
    @NotNull
    public static JsonNode readTree(@NotNull ObjectMapper mapper, String jsonString) {
        Assert.hasText(jsonString, "jsonString 不能为空");
        try {
            return mapper.readTree(jsonString);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将 json 格式的输入流转成 JsonNode.
     * @param in InputStream
     * @return jsonString json字符串
     */
    @NotNull
    public static JsonNode readTree(InputStream in) {
        return readTree(getInstance(), in);
    }

    /**
     * 根据自定义 mapper 将 json 格式的输入流转成 JsonNode.
     * @param mapper mapper
     * @param in     in
     * @return the json node
     */
    @NotNull
    public static JsonNode readTree(@NotNull ObjectMapper mapper, InputStream in) {
        Assert.notNull(in, "inputstream 不能为空");
        try {
            return mapper.readTree(in);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将字节数组转成 JsonNode.
     * @param content content
     * @return jsonString json字符串
     */
    @NotNull
    public static JsonNode readTree(byte[] content) {
        return readTree(getInstance(), content);
    }

    /**
     * 根据自定义 mapper 将字节数组转成 JsonNode.
     * @param mapper  mapper
     * @param content content
     * @return the json node
     */
    @NotNull
    public static JsonNode readTree(@NotNull ObjectMapper mapper, byte[] content) {
        Assert.notNull(content, "byte[] 不能为空");
        try {
            return mapper.readTree(content);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将 JsonParser 转成 JsonNode.
     * @param jsonParser JsonParser
     * @return jsonString json字符串
     */
    @NotNull
    public static JsonNode readTree(JsonParser jsonParser) {
        return readTree(getInstance(), jsonParser);
    }

    /**
     * 根据自定义 mapper 将 JsonParser 转成 JsonNode.
     * @param mapper     mapper
     * @param jsonParser json parser
     * @return the json node
     */
    @NotNull
    public static JsonNode readTree(@NotNull ObjectMapper mapper, JsonParser jsonParser) {
        Assert.notNull(jsonParser, "jsonParser 不能为空");
        try {
            return mapper.readTree(jsonParser);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将 json 反序列化成对象, 使用 T 标记需要反序列化的类型.
     * @param <T>        T 泛型标记
     * @param jsonString jsonString
     * @param valueType  class
     * @return Bean t
     */
    public static <T> T parse(String jsonString, Class<T> valueType) {
        return parse(getInstance(), jsonString, valueType);
    }

    /**
     * 根据自定义 mapper 将 json 反序列化成对象, 使用 T 标记需要反序列化的类型.
     * @param <T>        parameter
     * @param mapper     mapper
     * @param jsonString json string
     * @param valueType  value type
     * @return the t
     */
    public static <T> T parse(@NotNull ObjectMapper mapper, String jsonString, Class<T> valueType) {
        Assert.hasText(jsonString, "待解析的数据为空");
        try {
            return mapper.readValue(jsonString, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将 json byte 数组反序列化成对象.
     * @param <T>       T 泛型标记
     * @param content   json bytes
     * @param valueType class
     * @return Bean t
     */
    @Nullable
    public static <T> T parse(byte[] content, Class<T> valueType) {
        return parse(getInstance(), content, valueType);
    }

    /**
     * Parse t
     * @param <T>       parameter
     * @param mapper    mapper
     * @param content   content
     * @param valueType value type
     * @return the t
     */
    @Nullable
    public static <T> T parse(@NotNull ObjectMapper mapper, byte[] content, @NotNull Class<T> valueType) {
        if (Void.class.getTypeName().equals(valueType.getTypeName())) {
            return null;
        }
        Assert.notNull(content, "待解析的数据为空");
        try {
            return mapper.readValue(content, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     * @param <T>       T 泛型标记
     * @param in        InputStream
     * @param valueType class
     * @return Bean t
     */
    public static <T> T parse(InputStream in, Class<T> valueType) {
        return parse(getInstance(), in, valueType);
    }

    /**
     * Parse t
     * @param <T>       parameter
     * @param mapper    mapper
     * @param in        in
     * @param valueType value type
     * @return the t
     */
    public static <T> T parse(@NotNull ObjectMapper mapper, InputStream in, Class<T> valueType) {
        Assert.notNull(in, "待解析的数据为空");
        try {
            return mapper.readValue(in, valueType);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     * @param <T>           T 泛型标记
     * @param content       bytes
     * @param typeReference 泛型类型
     * @return Bean t
     */
    @NotNull
    public static <T> T parse(byte[] content, TypeReference<?> typeReference) {
        return parse(getInstance(), content, typeReference);
    }

    /**
     * Parse t
     * @param <T>           parameter
     * @param mapper        mapper
     * @param content       content
     * @param typeReference type reference
     * @return the t
     */
    @NotNull
    public static <T> T parse(@NotNull ObjectMapper mapper, byte[] content, TypeReference<?> typeReference) {
        Assert.notNull(content, "待解析的数据为空");
        try {
            return (T) mapper.readValue(content, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     * @param <T>           T 泛型标记
     * @param jsonString    jsonString
     * @param typeReference 泛型类型
     * @return Bean t
     */
    @NotNull
    public static <T> T parse(String jsonString, TypeReference<?> typeReference) {
        return parse(getInstance(), jsonString, typeReference);
    }

    /**
     * Parse t
     * @param <T>           parameter
     * @param mapper        mapper
     * @param jsonString    json string
     * @param typeReference type reference
     * @return the t
     */
    public static <T> T parse(@NotNull ObjectMapper mapper, String jsonString, TypeReference<?> typeReference) {
        Assert.hasText(jsonString, "待解析的数据为空");
        try {
            return (T) mapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Parse t
     * @param <T>           parameter
     * @param jsonString    json string
     * @param typeReference type reference
     * @return the t
     */
    public static <T> T parse(String jsonString, Object typeReference) {
        return parse(getInstance(), jsonString, typeReference);
    }

    /**
     * Parse t
     * @param <T>           parameter
     * @param mapper        mapper
     * @param jsonString    json string
     * @param typeReference type reference
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(@NotNull ObjectMapper mapper, String jsonString, Object typeReference) {
        Assert.hasText(jsonString, "待解析的数据为空");
        Assert.notNull(typeReference, "必须指定转换类型");
        try {
            if (typeReference instanceof TypeReference) {
                return (T) mapper.readValue(jsonString, (TypeReference<?>) typeReference);
            } else {
                T t;
                Class<T> clazz = (Class<T>) typeReference;
                // 如果是 String, 直接返回, 避免异常
                if (String.class.isAssignableFrom(clazz)) {
                    t = (T) jsonString;
                } else if (Date.class.isAssignableFrom(clazz)) {
                    // 如果是时间类型, 则直接转为 Date, 避免异常
                    ConcurrentDateFormat format = ConcurrentDateFormat.of(PATTERN_DATETIME);
                    try {
                        t = (T) format.parse(jsonString);
                    } catch (ParseException e) {
                        throw Exceptions.unchecked(e);
                    }
                } else {
                    t = mapper.readValue(jsonString, (Class<T>) typeReference);
                }
                return t;
            }
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将json反序列化成对象
     * @param <T>           T 泛型标记
     * @param in            InputStream
     * @param typeReference 泛型类型
     * @return Bean t
     */
    @NotNull
    public static <T> T parse(InputStream in, TypeReference<?> typeReference) {
        return parse(getInstance(), in, typeReference);
    }

    /**
     * Parse t
     * @param <T>           parameter
     * @param mapper        mapper
     * @param in            in
     * @param typeReference type reference
     * @return the t
     */
    public static <T> T parse(@NotNull ObjectMapper mapper, InputStream in, TypeReference<?> typeReference) {
        Assert.notNull(in, "待解析的数据为空");
        try {
            return (T) mapper.readValue(in, typeReference);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 将直接数组转换为指定类型时, 根据实体内的 {@link javax.validation.constraints} 注解进行参数验证.
     * @param <T>       parameter
     * @param content   content
     * @param valueType value type
     * @param validator validator
     * @return the t
     */
    public static <T> T parseAndValidate(byte[] content, Class<T> valueType, Validator validator) {
        T t = parse(content, valueType);
        validate(t, validator);
        return t;
    }

    /**
     * Parse and validate t
     * @param <T>       parameter
     * @param mapper    mapper
     * @param content   content
     * @param valueType value type
     * @param validator validator
     * @return the t
     */
    public static <T> T parseAndValidate(@NotNull ObjectMapper mapper, byte[] content, Class<T> valueType, Validator validator) {
        T t = parse(mapper, content, valueType);
        validate(t, validator);
        return t;
    }

    /**
     * Parse and validate t
     * @param <T>        parameter
     * @param jsonString json string
     * @param valueType  value type
     * @param validator  validator
     * @return the t
     */
    public static <T> T parseAndValidate(String jsonString, Class<T> valueType, Validator validator) {
        T t = parse(jsonString, valueType);
        validate(t, validator);
        return t;
    }

    /**
     * Parse and validate t
     * @param <T>        parameter
     * @param mapper     mapper
     * @param jsonString json string
     * @param valueType  value type
     * @param validator  validator
     * @return the t
     */
    public static <T> T parseAndValidate(@NotNull ObjectMapper mapper, String jsonString, Class<T> valueType, Validator validator) {
        T t = parse(mapper, jsonString, valueType);
        validate(t, validator);
        return t;
    }

    /**
     * Validate
     * @param <T>       parameter
     * @param t         t
     * @param validator validator
     */
    private static <T> void validate(T t, @NotNull Validator validator) {
        Set<ConstraintViolation<T>> validate = validator.validate(t);
        if (!validate.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : validate) {
                sb.append(violation.getPropertyPath().toString()).append(": ").append(violation.getMessage()).append(" ");
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /**
     * 读取集合
     * @param <T>          泛型
     * @param content      bytes
     * @param elementClass elementClass
     * @return 集合 list
     */
    @NotNull
    public static <T> List<T> toList(byte[] content, Class<T> elementClass) {
        return toList(getInstance(), content, elementClass);
    }

    /**
     * To list
     * @param <T>          parameter
     * @param mapper       mapper
     * @param content      content
     * @param elementClass element class
     * @return the list
     */
    public static <T> List<T> toList(@NotNull ObjectMapper mapper, byte[] content, Class<T> elementClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyList();
        }
        try {
            return mapper.readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 封装 map type
     * @param elementClass 集合值类型
     * @return CollectionLikeType list type
     */
    public static CollectionLikeType getListType(Class<?> elementClass) {
        return getListType(getInstance(), elementClass);
    }

    /**
     * Get list type
     * @param mapper       mapper
     * @param elementClass element class
     * @return the list type
     */
    public static CollectionLikeType getListType(@NotNull ObjectMapper mapper, Class<?> elementClass) {
        return mapper.getTypeFactory().constructCollectionLikeType(List.class, elementClass);
    }

    /**
     * 读取集合
     * @param <T>          泛型
     * @param content      InputStream
     * @param elementClass elementClass
     * @return 集合 list
     */
    @NotNull
    public static <T> List<T> toList(InputStream content, Class<T> elementClass) {
        return toList(getInstance(), content, elementClass);
    }

    /**
     * To list list
     * @param <T>          parameter
     * @param mapper       mapper
     * @param content      content
     * @param elementClass element class
     * @return the list
     */
    @Contract("_, null, _ -> !null")
    public static <T> List<T> toList(@NotNull ObjectMapper mapper, InputStream content, Class<T> elementClass) {
        if (content == null) {
            return Collections.emptyList();
        }
        try {
            return mapper.readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     * @param <T>          泛型
     * @param content      bytes
     * @param elementClass elementClass
     * @return 集合 list
     */
    @NotNull
    public static <T> List<T> toList(String content, Class<T> elementClass) {
        return toList(getInstance(), content, elementClass);
    }

    /**
     * To list
     * @param <T>          parameter
     * @param mapper       mapper
     * @param content      content
     * @param elementClass element class
     * @return the list
     */
    public static <T> List<T> toList(@NotNull ObjectMapper mapper, String content, Class<T> elementClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyList();
        }
        try {
            return mapper.readValue(content, getListType(elementClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     */
    @NotNull
    public static <K, V> Map<K, V> toMap(byte[] content, Class<?> keyClass, Class<?> valueClass) {
        return toMap(getInstance(), content, keyClass, valueClass);
    }

    /**
     * To map
     * @param <K>        parameter
     * @param <V>        parameter
     * @param mapper     mapper
     * @param content    content
     * @param keyClass   key class
     * @param valueClass value class
     * @return the map
     */
    public static <K, V> Map<K, V> toMap(@NotNull ObjectMapper mapper, byte[] content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyMap();
        }
        try {
            return mapper.readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 获取 map type
     * @param keyClass   key 类型
     * @param valueClass value 类型
     * @return MapType map type
     */
    public static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
        return getMapType(getInstance(), keyClass, valueClass);
    }

    /**
     * Get map type
     * @param mapper     mapper
     * @param keyClass   key class
     * @param valueClass value class
     * @return the map type
     */
    public static MapType getMapType(@NotNull ObjectMapper mapper, Class<?> keyClass, Class<?> valueClass) {
        return mapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    /**
     * 读取集合
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    InputStream
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     */
    @NotNull
    public static <K, V> Map<K, V> toMap(InputStream content, Class<?> keyClass, Class<?> valueClass) {
        return toMap(getInstance(), content, keyClass, valueClass);
    }

    /**
     * To map
     * @param <K>        parameter
     * @param <V>        parameter
     * @param mapper     mapper
     * @param content    content
     * @param keyClass   key class
     * @param valueClass value class
     * @return the map
     */
    public static <K, V> Map<K, V> toMap(@NotNull ObjectMapper mapper, InputStream content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyMap();
        }
        try {
            return mapper.readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 读取集合
     * @param <K>        泛型
     * @param <V>        泛型
     * @param content    bytes
     * @param keyClass   key类型
     * @param valueClass 值类型
     * @return 集合 map
     */
    @NotNull
    public static <K, V> Map<K, V> toMap(String content, Class<?> keyClass, Class<?> valueClass) {
        return toMap(getInstance(), content, keyClass, valueClass);
    }

    /**
     * To map
     * @param <K>        parameter
     * @param <V>        parameter
     * @param mapper     mapper
     * @param content    content
     * @param keyClass   key class
     * @param valueClass value class
     * @return the map
     */
    public static <K, V> Map<K, V> toMap(@NotNull ObjectMapper mapper, String content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtils.isEmpty(content)) {
            return Collections.emptyMap();
        }
        try {
            return mapper.readValue(content, getMapType(keyClass, valueClass));
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 使用静态内部类实例化单例对象, 此 ObjectMapper 会在全局使用
     * @Author yanger
     * @Date 2020/12/28 19:16
     * @throws
     */
    private static class JacksonHolder {
        /** INSTANCE */
        private static final ObjectMapper INSTANCE;
        /** CHINA */
        private static final Locale CHINA = Locale.CHINA;

        static {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setLocale(CHINA);
            // 序列化时, 日期的统一格式 (JVM 参数优先)
            objectMapper.setDateFormat(new SimpleDateFormat(PATTERN_DATETIME, CHINA));
            // 去掉默认的时间戳格式
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            // 设置为中国上海时区
            String zone = System.getProperty(ConfigKey.JsonConfigKey.JSON_TIME_ZONE, ConfigDefaultValue.JsonConfigValue.JSON_TIME_ZONE);
            objectMapper.setTimeZone(TimeZone.getTimeZone(zone));
            // 单引号
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            // 可解析反斜杠引用的所有字符, 默认：false, 不可解析
            objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            // 允许 json 字符串包含非引号控制字符 (值小于32的ASCII字符, 包含制表符和换行符)
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            // 忽略 json 字符串中不识别的属性
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 设置 Jackson 序列化时只包含不为空的字段 objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.USE_DEFAULTS);
            // 忽略无法转换的对象
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.findAndRegisterModules();

            registerSubtypes(objectMapper);

            INSTANCE = objectMapper;
        }

        /**
         * 全局注册抽象类/接口的子类, 避免每次手动处理反序列化问题:
         * 1. 在抽象类/接口类上添加 @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
         * 2. 在子类或实现类上添加 @JsonTypeName(value = "标识")
         * @param objectMapper object mapper
         */
        private static void registerSubtypes(@NotNull ObjectMapper objectMapper) {
            StopWatch stopWatch = new StopWatch("Scan JsonTypeName Annotation");
            stopWatch.start();

            // 复写 Reflections ，去掉日志输出
            Reflections reflections = new Reflections(App.Const.BASE_PACKAGES) {
                @Override
                public void expandSuperTypes() {
                    String index = index(SubTypesScanner.class);
                    Set<String> keys = store.keys(index);
                    keys.removeAll(store.values(index));
                    for (String key : keys) {
                        final Class<?> type = forName(key, configuration.getClassLoaders());
                        if (type != null) {
                            expandSupertypes(store, key, type);
                        }
                    }
                }
                private void expandSupertypes(Store store, String key, Class<?> type) {
                    for (Class<?> supertype : ReflectionUtils.getSuperTypes(type)) {
                        if (store.put(SubTypesScanner.class, supertype.getName(), key)) {
                            if (log != null && ConfigKit.isDebugModel()) {
                                log.debug("expanded subtype {} -> {}", supertype.getName(), key);
                            }
                            expandSupertypes(store, supertype.getName(), supertype);
                        }
                    }
                }
            };

            // 扫描 JsonTypeName 注解, 注册所有子类
            Set<Class<?>> subTypes = reflections.getTypesAnnotatedWith(JsonTypeName.class);
            // 注册所有子类型
            objectMapper.registerSubtypes(subTypes.toArray(new Class[0]));
            stopWatch.stop();
            if (ConfigKit.isDebugModel()) {
                log.info("{}", stopWatch.prettyPrint());
            }
        }
    }

}
