package com.yanger.starter.basic.enums;

import com.google.common.collect.Sets;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yanger.starter.basic.annotation.SerializeValue;
import com.yanger.starter.basic.convert.EntityEnumDeserializer;
import com.yanger.starter.basic.convert.EntityEnumSerializer;
import com.yanger.tools.general.tools.EnumUtils;
import com.yanger.tools.web.exception.BasicException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import cn.hutool.core.text.StrFormatter;

/**
 * 可序列化为 json 的枚举接口
 * @Author yanger
 * @Date 2021/1/27 16:52
 */
@JsonSerialize(using = EntityEnumSerializer.class)
@JsonDeserialize(using = EntityEnumDeserializer.class)
public interface SerializeEnum<T extends Serializable> {

    /** 子类枚举的 vaule 字段的字段名 */
    String VALUE_FILED_NAME = "value";

    /**
     * 存入数据库的值
     *
     * @return the values
     */
    T getValue();

    /**
     * Type
     *
     * @return the class
     */
    default Class<?> valueClass() {
        return this.getValue().getClass();
    }

    /**
     * 描述
     *
     * @return the desc
     */
    String getDesc();

    /**
     * 返回枚举名
     *
     * @return the string
     */
    String name();

    /**
     * 返回枚举下标
     *
     * @return the int
     */
    int ordinal();

    /**
     * Value of e.
     *
     * @param <E>   the type parameter
     * @param clazz the clazz
     * @param value the value
     * @return the e
     */
    static <E extends Enum<E> & SerializeEnum<?>> E valueOf(Class<E> clazz, Serializable value) {
        String errorMessage = StrFormatter.format("枚举类型转换错误: 没有找到对应枚举. value = {}, SerializeEnum = {}",
                                                  value,
                                                  clazz);
        return EnumUtils.getEnumObject(clazz, e -> e.getValue().equals(value)).orElseThrow(() -> new BasicException(errorMessage));
    }

    /**
     * 如果无法使用 {@link SerializeEnum#getValue()} 解析枚举, 将再次使用 name() 获取枚举, 最后是 ordinal().
     *
     * @param <T>        parameter
     * @param clz        clz
     * @param finalValue final value
     * @return the enum by order
     */
    @SuppressWarnings("unchecked")
    static <T> T getEnumByOrder(Class<? extends Enum<?>> clz, Serializable finalValue) {
        T result;
        String value = String.valueOf(finalValue);
        // 使用 name() 匹配
        Optional<? extends Enum<?>> getForName = EnumUtils.getEnumObject(clz, e -> e.name().equals(value));
        if (getForName.isPresent()) {
            result = (T) getForName.get();
        } else {
            int index;
            try {
                index = Integer.parseInt(value);
            } catch (Exception e) {
                throw new BasicException("通过枚举下标查找枚举出错: [{}] 无法转换为下标", value);
            }
            // 使用下标匹配
            Optional<? extends Enum<?>> getForOrdinal = EnumUtils.getEnumObject(clz, e -> e.ordinal() == index);
            result = (T) getForOrdinal.orElseThrow(() -> new BasicException("无法将 [{}] 转换为 [{}]", value, clz));
        }
        return result;
    }

    /**
     * 查找被 {@link SerializeValue} 标识的元素
     *
     * @param clazz clazz
     * @return the annotation
     */
    @Nullable
    static AccessibleObject getAnnotation(@NotNull Class<?> clazz) {
        Set<AccessibleObject> accessibleObjects = Sets.newHashSetWithExpectedSize(8);
        Field[] fields = clazz.getDeclaredFields();
        Collections.addAll(accessibleObjects, fields);
        for (AccessibleObject accessibleObject : accessibleObjects) {
            SerializeValue jsonCreator = accessibleObject.getAnnotation(SerializeValue.class);
            if (jsonCreator != null) {
                accessibleObject.setAccessible(true);
                return accessibleObject;
            }
        }
        return null;
    }

}
