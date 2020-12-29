package com.yanger.starter.basic.convert;

import com.yanger.starter.basic.util.ConvertUtils;
import com.yanger.tools.general.function.CheckedFunction;
import com.yanger.tools.web.exception.Unchecked;
import com.yanger.tools.web.tools.ClassUtils;
import com.yanger.tools.web.tools.ReflectionUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.cglib.core.Converter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2020/12/29 19:23
 */
@Slf4j
@AllArgsConstructor
public class CustomConverter implements Converter {
    /** TYPE_CACHE */
    private static final ConcurrentMap<String, TypeDescriptor> TYPE_CACHE = new ConcurrentHashMap<>();
    /** Source clazz */
    private final Class<?> sourceClazz;
    /** Target clazz */
    private final Class<?> targetClazz;

    /**
     * cglib convert
     *
     * @param value     源对象属性
     * @param target    目标对象属性类
     * @param fieldName 目标的field名,原为 set 方法名,MicaBeanCopier 里做了更改
     * @return {Object}
     * @since 1.0.0
     */
    @Override
    @Nullable
    @SuppressWarnings("checkstyle:ReturnCount")
    public Object convert(Object value, Class target, Object fieldName) {
        if (value == null) {
            return null;
        }
        // 类型一样,不需要转换
        if (ClassUtils.isAssignableValue(target, value)) {
            return value;
        }
        try {
            TypeDescriptor targetDescriptor = CustomConverter.getTypeDescriptor(this.targetClazz, (String) fieldName);
            // 1. 判断 sourceClazz 为 Map
            if (Map.class.isAssignableFrom(this.sourceClazz)) {
                return ConvertUtils.convert(value, targetDescriptor);
            } else {
                TypeDescriptor sourceDescriptor = CustomConverter.getTypeDescriptor(this.sourceClazz, (String) fieldName);
                return ConvertUtils.convert(value, sourceDescriptor, targetDescriptor);
            }
        } catch (Exception e) {
            log.warn("Converter error", e);
        }
        return null;
    }

    /**
     * Gets type descriptor *
     *
     * @param clazz     clazz
     * @param fieldName field name
     * @return the type descriptor
     * @since 1.0.0
     */
    private static TypeDescriptor getTypeDescriptor(@NotNull Class<?> clazz, String fieldName) {
        String srcCacheKey = clazz.getName() + fieldName;
        // 忽略抛出异常的函数,定义完整泛型,避免编译问题
        CheckedFunction<String, TypeDescriptor> uncheckedFunction = (key) -> {
            // 这里 property 理论上不会为 null
            Field field = ReflectionUtils.getField(clazz, fieldName);
            if (field == null) {
                throw new NoSuchFieldException(fieldName);
            }
            return new TypeDescriptor(field);
        };
        return TYPE_CACHE.computeIfAbsent(srcCacheKey, Unchecked.function(uncheckedFunction));
    }
}
