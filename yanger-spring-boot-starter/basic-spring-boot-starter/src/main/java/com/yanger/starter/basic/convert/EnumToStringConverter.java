package com.yanger.starter.basic.convert;

import com.google.common.collect.Maps;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yanger.starter.basic.util.ConvertUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import lombok.extern.slf4j.Slf4j;

/**
 * 接收参数 同 jackson Enum -> String 转换
 * @Author yanger
 * @Date 2020/12/29 19:21
 */
@Slf4j
public class EnumToStringConverter implements ConditionalGenericConverter {

    /** 缓存 Enum 类信息,提供性能 */
    private static final ConcurrentMap<Class<?>, AccessibleObject> ENUM_CACHE_MAP = Maps.newConcurrentMap();

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
        Set<ConvertiblePair> pairSet = new HashSet<>(4);
        pairSet.add(new ConvertiblePair(Enum.class, String.class));
        pairSet.add(new ConvertiblePair(Enum.class, Integer.class));
        pairSet.add(new ConvertiblePair(Enum.class, Long.class));
        return Collections.unmodifiableSet(pairSet);
    }

    /**
     * Convert object
     *
     * @param source     source
     * @param sourceType source type
     * @param targetType target type
     * @return the object
     */
    @Override
    @SuppressWarnings("checkstyle:ReturnCount")
    public Object convert(@Nullable Object source, @NotNull TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Class<?> sourceClazz = sourceType.getType();
        AccessibleObject accessibleObject = ENUM_CACHE_MAP.computeIfAbsent(sourceClazz, EnumToStringConverter::getAnnotation);
        Class<?> targetClazz = targetType.getType();
        // 如果为null,走默认的转换
        if (accessibleObject == null) {
            if (String.class == targetClazz) {
                return ((Enum) source).name();
            }
            int ordinal = ((Enum) source).ordinal();
            return ConvertUtils.convert(ordinal, targetClazz);
        }
        try {
            return EnumToStringConverter.invoke(sourceClazz, accessibleObject, source, targetClazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Gets annotation *
     *
     * @param clazz clazz
     * @return the annotation
     */
    @Nullable
    private static AccessibleObject getAnnotation(@NotNull Class<?> clazz) {
        Set<AccessibleObject> accessibleObjects = new HashSet<>();
        // JsonValue METHOD, FIELD
        Field[] fields = clazz.getDeclaredFields();
        Collections.addAll(accessibleObjects, fields);
        // methods
        Method[] methods = clazz.getDeclaredMethods();
        Collections.addAll(accessibleObjects, methods);
        for (AccessibleObject accessibleObject : accessibleObjects) {
            // 复用 jackson 的 JsonValue 注解
            JsonValue jsonValue = accessibleObject.getAnnotation(JsonValue.class);
            if (jsonValue != null && jsonValue.value()) {
                accessibleObject.setAccessible(true);
                return accessibleObject;
            }
        }
        return null;
    }

    /**
     * Invoke object
     *
     * @param clazz            clazz
     * @param accessibleObject accessible object
     * @param source           source
     * @param targetClazz      target clazz
     * @return the object
     * @throws IllegalAccessException    illegal access exception
     * @throws InvocationTargetException invocation target exception
     */
    @Nullable
    private static Object invoke(Class<?> clazz, AccessibleObject accessibleObject, Object source, Class<?> targetClazz)
        throws IllegalAccessException, InvocationTargetException {
        Object value = null;
        if (accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            value = field.get(source);
        } else if (accessibleObject instanceof Method) {
            Method method = (Method) accessibleObject;
            Class<?> paramType = method.getParameterTypes()[0];
            // 类型转换
            Object object = ConvertUtils.convert(source, paramType);
            value = method.invoke(clazz, object);
        }
        if (value == null) {
            return null;
        }
        return ConvertUtils.convert(value, targetClazz);
    }

}
