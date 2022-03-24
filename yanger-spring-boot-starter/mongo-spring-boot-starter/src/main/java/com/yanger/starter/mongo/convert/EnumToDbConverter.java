package com.yanger.starter.mongo.convert;

import com.google.common.collect.Maps;
import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.starter.basic.util.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.data.convert.WritingConverter;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 枚举写入到 db
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
@WritingConverter
public class EnumToDbConverter implements ConditionalGenericConverter {

    /** 缓存 Enum 类信息,提供性能 */
    private static final ConcurrentMap<Class<?>, AccessibleObject> ENUM_CACHE_MAP = Maps.newConcurrentMap();

    /**
     * Matches boolean
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
     * @return the convertible types
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairSet = new HashSet<>(4);
        pairSet.add(new ConvertiblePair(Enum.class, String.class));
        pairSet.add(new ConvertiblePair(Enum.class, Integer.class));
        pairSet.add(new ConvertiblePair(Enum.class, Boolean.class));
        return Collections.unmodifiableSet(pairSet);
    }

    /**
     * 写入时将枚举转换为 value 字段或者 name
     * 1. 如果是 SerializeEnum 则写入 value
     * 2. 如果不是则写入 name
     * @param source     待写入的数据
     * @param sourceType 待写入的数据类型
     * @param targetType 需要被转换的类型
     * @return the object
     */
    @Override
    @SuppressWarnings("checkstyle:ReturnCount")
    public Object convert(@Nullable Object source, @NotNull TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        Class<?> sourceClazz = sourceType.getType();
        AccessibleObject accessibleObject = ENUM_CACHE_MAP.computeIfAbsent(sourceClazz, SerializeEnum::getAnnotation);

        // 没有使用 @SerializeValue 标识
        if (accessibleObject == null) {
            if (SerializeEnum.class.isAssignableFrom(sourceClazz)) {
                return ((SerializeEnum<?>) source).getValue();
            } else {
                return ((Enum<?>) source).name();
            }
        }
        try {
            return EnumToDbConverter.invoke(accessibleObject, source);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Invoke object
     * @param accessibleObject accessible object
     * @param source           source
     * @return the object
     * @throws IllegalAccessException illegal access exception
     */
    @Nullable
    private static Object invoke(AccessibleObject accessibleObject, Object source)
        throws IllegalAccessException {
        Object value = null;
        if (accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            value = field.get(source);
        }
        if (value == null) {
            return null;
        }
        return ConvertUtils.convert(value, value.getClass());
    }
}
