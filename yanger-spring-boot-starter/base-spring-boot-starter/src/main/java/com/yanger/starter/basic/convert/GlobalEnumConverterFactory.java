package com.yanger.starter.basic.convert;

import com.google.common.collect.Maps;
import com.yanger.starter.basic.enums.SerializeEnum;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Map;

/**
 * spring mvc 枚举转换 (前端传入的都是 String 类型, 因此这里直接指定为 String, 但是真实的值包含: value/name/枚举下标)
 * @Author yanger
 * @Date 2021/1/27 17:06
 */
public class GlobalEnumConverterFactory implements ConverterFactory<String, SerializeEnum<?>> {

    /** 缓存枚举转换器 */
    private static final Map<Class<?>, Converter<String, ?>> CONVERTER_MAP = Maps.newConcurrentMap();

    /**
     * 返回一个 enum 转换器
     * @param <T>        parameter   需要被转换的枚举类型
     * @param targetType target type
     * @return the converter
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends SerializeEnum<?>> Converter<String, T> getConverter(@NotNull Class<T> targetType) {
        Converter<String, T> result = (Converter<String, T>) CONVERTER_MAP.get(targetType);
        if (result == null) {
            result = new GlobalEnumConverter<>(targetType);
            CONVERTER_MAP.put(targetType, result);
        }
        return result;
    }

    /**
     * enum 的具体转换逻辑实现1
     */
    private static class GlobalEnumConverter<T extends SerializeEnum<?>> implements Converter<String, T> {
        /** 枚举的 {@link SerializeEnum#getValue()} 与 枚举的映射关系 */
        private final Map<String, T> enumMap = Maps.newHashMap();
        /** Enum type */
        private final Class<? extends SerializeEnum<?>> enumType;

        /**
         * 建立映射关系
         * @param enumType enum type
         */
        GlobalEnumConverter(@NotNull Class<T> enumType) {
            this.enumType = enumType;
            // 获取当前枚举类型的所有已定义的枚举
            T[] enums = enumType.getEnumConstants();
            for (T e : enums) {
                // 将 getValue 转为 String, 因为前端传过来的都是 String 类型的参数
                this.enumMap.put(String.valueOf(e.getValue()), e);
            }
        }

        /**
         * 转换逻辑:
         * 优先匹配 getValue, 然后才是 name, 最后是 index
         * 与 json 格式的枚举转换优先级保持一致 {@link EntityEnumDeserializer}
         * @param source source
         * @return the t
         */
        @Override
        @SuppressWarnings("unchecked")
        public T convert(@NotNull String source) {
            T result = this.enumMap.get(source);

            if (result == null) {
                result = SerializeEnum.getEnumByOrder((Class<? extends Enum<?>>) this.enumType, source);
                this.enumMap.put(source, result);
            }
            return result;
        }
    }
}
