package com.yanger.starter.basic.util;

import com.yanger.starter.basic.convert.CustomConversionService;
import com.yanger.starter.basic.convert.CustomConverter;
import com.yanger.tools.web.support.BeanCopier;
import com.yanger.tools.web.tools.BeanUtils;
import com.yanger.tools.web.tools.ClassUtils;

import org.jetbrains.annotations.Contract;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.experimental.UtilityClass;

/**
 * @Description 基于 spring ConversionService 类型转换
 * @Author yanger
 * @Date 2020/12/29 19:16
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class ConvertUtils {

    /**
     * Convenience operation for converting a source object to the specified targetType.
     * {@link TypeDescriptor#forObject(Object)}.
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        if (ClassUtils.isAssignableValue(targetType, source)) {
            return (T) source;
        }
        GenericConversionService conversionService = CustomConversionService.getInstance();
        return conversionService.convert(source, targetType);
    }

    /**
     * Convenience operation for converting a source object to the specified targetType,
     * where the target type is a descriptor that provides additional conversion context.
     * {@link TypeDescriptor#forObject(Object)}.
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param sourceType the source type
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null},  or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        GenericConversionService conversionService = CustomConversionService.getInstance();
        return (T) conversionService.convert(source, sourceType, targetType);
    }

    /**
     * Convenience operation for converting a source object to the specified targetType,
     * where the target type is a descriptor that provides additional conversion context.
     * Simply delegates to {@link #convert(Object, TypeDescriptor, TypeDescriptor)} and
     * encapsulates the construction of the source type descriptor using
     * {@link TypeDescriptor#forObject(Object)}.
     *
     * @param <T>        泛型标记
     * @param source     the source object
     * @param targetType the target type
     * @return the converted value
     * @throws IllegalArgumentException if targetType is {@code null}, or sourceType is {@code null} but source is not {@code null}
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T convert(@Nullable Object source, TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        GenericConversionService conversionService = CustomConversionService.getInstance();
        return (T) conversionService.convert(source, targetType);
    }

    /**
     * 拷贝对象并对不同类型属性进行转换
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param source      源对象
     * @param targetClazz 转换成的类
     * @return T t
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T copyWithConvert(@Nullable Object source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        return ConvertUtils.copyWithConvert(source, source.getClass(), targetClazz);
    }

    /**
     * 拷贝对象并对不同类型属性进行转换
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param source      源对象
     * @param sourceClazz 源类
     * @param targetClazz 转换成的类
     * @return T t
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    @Nullable
    public static <T> T copyWithConvert(@Nullable Object source, Class<?> sourceClazz, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(sourceClazz, targetClazz, true);
        T to = BeanUtils.newInstance(targetClazz);
        copier.copy(source, to, new CustomConverter(sourceClazz, targetClazz));
        return to;
    }

    /**
     * 拷贝列表并对不同类型属性进行转换
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param sourceList  源对象列表
     * @param targetClazz 转换成的类
     * @return List list
     * @since 1.0.0
     */
    @Contract("null, _ -> !null")
    public static <T> List<T> copyWithConvert(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        Class<?> sourceClazz = null;
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            if (sourceClazz == null) {
                sourceClazz = source.getClass();
            }
            T bean = ConvertUtils.copyWithConvert(source, sourceClazz, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

}
