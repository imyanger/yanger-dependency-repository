package com.yanger.starter.mongo.convert;

import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.tools.general.tools.EnumUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.convert.ReadingConverter;

import java.io.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 从 db 中读取枚举
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
@ReadingConverter
public class DbToEnumConverter implements ConditionalGenericConverter {
    /** 缓存 Enum 类信息,提升性能 */
    private static final ConcurrentMap<Class<?>, AccessibleObject> ENUM_CACHE_MAP = new ConcurrentHashMap<>(8);

    /**
     * Matches boolean
     *
     * @param sourceType source type
     * @param targetType target type
     * @return the boolean
     */
    @Override
    public boolean matches(@NotNull TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        return true;
    }

    /**
     * Gets convertible types *
     *
     * @return the convertible types
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairSet = new HashSet<>(2);
        pairSet.add(new ConvertiblePair(String.class, Enum.class));
        pairSet.add(new ConvertiblePair(Integer.class, Enum.class));
        pairSet.add(new ConvertiblePair(Boolean.class, Enum.class));
        return Collections.unmodifiableSet(pairSet);
    }

    /**
     * 读取时将 value/name 值转为枚举
     *
     * @param source     待读取的数据
     * @param sourceType 待读取的数据类型
     * @param targetType 需要被转换的类型
     * @return the object
     */
    @Nullable
    @Override
    @SuppressWarnings(value = {"checkstyle:ReturnCount", "unchecked"})
    public Object convert(@Nullable Object source, @NotNull TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {

        if (source == null) {
            return null;
        }
        if (StringUtils.isBlank(String.valueOf(source))) {
            return null;
        }
        AccessibleObject accessibleObject = ENUM_CACHE_MAP.computeIfAbsent(targetType.getType(), SerializeEnum::getAnnotation);
        String value = String.valueOf(source).trim();
        // 没有使用 @SerializeValue 标识, 则需要根据类型判断
        if (accessibleObject == null) {
            if (SerializeEnum.class.isAssignableFrom(targetType.getType())) {
                Class<? extends SerializeEnum<?>> clazz = (Class<? extends SerializeEnum<?>>) targetType.getType();
                return valueOf(clazz, source);
            } else {
                return valueOf(targetType.getType(), value);
            }
        }
        try {
            return DbToEnumConverter.invoke((Class<? extends Enum<?>>) targetType.getType(), accessibleObject, source);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 兼容处理, 读取顺序 value > name > 下标
     *
     * @param <T>    parameter
     * @param clazz  clazz
     * @param source source
     * @return the t
     */
    @SuppressWarnings("unchecked")
    private static <T extends SerializeEnum<?>> T valueOf(Class<? extends SerializeEnum<?>> clazz, Object source) {
        T result = (T) EnumUtils.getEnumObject(clazz, v -> v.getValue().equals(source)).orElse(null);
        if (result != null) {
            return result;
        }
        return SerializeEnum.getEnumByOrder((Class<? extends Enum<?>>) clazz, (Serializable) source);
    }

    /**
     * Value of t
     *
     * @param <T>   parameter
     * @param clazz clazz
     * @param value value
     * @return the t
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> T valueOf(Class<?> clazz, String value) {
        return Enum.valueOf((Class<T>) clazz, value);
    }

    /**
     * Invoke object
     *
     * @param clazz            clazz
     * @param accessibleObject accessible object
     * @param value            value
     * @return the object
     * @throws IllegalAccessException    illegal access exception
     * @throws InvocationTargetException invocation target exception
     * @throws InstantiationException    instantiation exception
     */
    @Contract("_, null, _ -> null")
    @Nullable
    private static Object invoke(Class<? extends Enum<?>> clazz, AccessibleObject accessibleObject, Object value) {
        if (accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            return valueOf(clazz, field, value);
        }

        return null;
    }

    /**
     * Value of enum
     *
     * @param <T>       parameter
     * @param enumClass enum class
     * @param field     field
     * @param value     value
     * @return the enum
     */
    private static <T extends Enum<?>> Enum<?> valueOf(@NotNull Class<? extends Enum<?>> enumClass, Field field, Object value) {
        Enum<?>[] es = enumClass.getEnumConstants();
        GetFieldInvoker invoker = new GetFieldInvoker(field);
        return Arrays.stream(es).filter((e) -> equalsValue(value, invoker.invoke(e))).findAny().orElse(null);
    }


    /**
     * 值比较
     *
     * @param sourceValue 数据库字段值
     * @param targetValue 当前枚举属性值
     * @return 是否匹配 boolean
     */
    private static boolean equalsValue(Object sourceValue, Object targetValue) {
        if (sourceValue instanceof Number
            && targetValue instanceof Number
            && new BigDecimal(String.valueOf(sourceValue)).compareTo(new BigDecimal(String.valueOf(targetValue))) == 0) {
            return true;
        }
        return Objects.equals(sourceValue, targetValue);
    }

    public static class GetFieldInvoker {
        /** Field */
        private final Field field;

        /**
         * Get field invoker
         *
         * @param field field
         */
        public GetFieldInvoker(Field field) {
            this.field = field;
        }

        /**
         * Invoke object
         *
         * @param target target
         * @return the object
         */
        @SneakyThrows
        public Object invoke(Object target) {
            return this.field.get(target);
        }

        /**
         * Gets type *
         *
         * @return the type
         */
        public Class<?> getType() {
            return this.field.getType();
        }
    }

}
