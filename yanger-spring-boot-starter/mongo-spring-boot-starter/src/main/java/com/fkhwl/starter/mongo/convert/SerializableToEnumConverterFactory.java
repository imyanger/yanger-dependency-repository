package com.fkhwl.starter.mongo.convert;

import com.google.common.collect.Maps;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.io.*;
import java.util.Map;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: Serializable 转换为枚举, DB -> 实体 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.07 11:15
 * @since 1.0.0
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
     * @since 1.0.0
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

    /**
     * <p>Company: 成都返空汇网络技术有限公司 </p>
     * <p>Description: </p>
     *
     * @param <T> parameter
     * @author dong4j
     * @version 1.3.0
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.04.07 11:28
     * @since 1.0.0
     */
    private static class GenericToEnumConverter<T extends Enum<?>> extends AbstractSerializableToEnumConverter<T> {
        /**
         * 建立映射关系 (V[value] <--> E[SerializeEnum])
         *
         * @param enumClass enum class
         * @since 1.0.0
         */
        GenericToEnumConverter(@NotNull Class<T> enumClass) {
            super(enumClass);
        }
    }
}
