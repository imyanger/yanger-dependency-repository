package com.yanger.starter.mongo.convert;

import com.google.common.collect.Maps;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.io.*;
import java.util.Map;

/**
 * Serializable 转换为枚举, DB -> 实体
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class SerializableToEnumConverterFactory implements ConverterFactory<Serializable, Enum<?>> {

    /** 缓存枚举转换器 */
    private static final Map<Class, Converter> CONVERTER_MAP = Maps.newConcurrentMap();

    /**
     * 根据 targetType 返回一个枚举转换器
     *
     * @param <T>        parameter      枚举类型
     * @param targetType target type
     * @return the converter
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Enum<?>> Converter<Serializable, T> getConverter(@NotNull Class<T> targetType) {
        Converter result = CONVERTER_MAP.get(targetType);
        if (result == null) {
            result = new GenericToEnumConverter<>(targetType);
            CONVERTER_MAP.put(targetType, result);
        }
        return result;
    }

    private static class GenericToEnumConverter<T extends Enum<?>> extends AbstractSerializableToEnumConverter<T> {
        /**
         * 建立映射关系 (V[value] <--> E[SerializeEnum])
         *
         * @param enumClass enum class
         */
        GenericToEnumConverter(@NotNull Class<T> enumClass) {
            super(enumClass);
        }
    }
}
