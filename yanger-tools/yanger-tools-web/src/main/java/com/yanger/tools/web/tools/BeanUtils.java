package com.yanger.tools.web.tools;

import com.google.common.collect.Maps;

import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.support.BeanCopier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @Description 对象拷贝类
 * @Author yanger
 * @Date 2020/7/17 18:34
 */
@SuppressWarnings("all")
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 单个对象拷贝，方便函数式调用
     *
     * @param source 数据源类
     * @param target 目标类::new(eg: UserVO::new)
     * @return T
     * @date 2020/7/17
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target) {
        T t = target.get();
        copyProperties(source, t);
        return t;
    }

    /**
     * 单个对象拷贝，方便函数式调用，增加回调方法（可自定义字段拷贝规则）
     *
     * @param source   数据源类
     * @param target   目标类::new(eg: UserVO::new)
     * @param callBack 回调函数
     * @return T
     * @date 2020/7/17
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target, ConverterUtilsCallBack<S, T> callBack) {
        T t = target.get();
        copyProperties(source, t);
        callBack.callBack(source, t);
        return t;
    }

    /**
     * 集合数据的拷贝
     *
     * @param sources 数据源类
     * @param target  目标类::new(eg: UserVO::new)
     * @return java.util.List<T>
     * @date 2020/7/17
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
     *
     * @param sources  数据源类
     * @param target   目标类::new(eg: UserVO::new)
     * @param callBack 回调函数
     * @return java.util.List<T>
     * @date 2020/7/17
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, ConverterUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
        }
        return list;
    }

    /**
     * 实例化对象
     *
     * @param <T>      泛型标记
     * @param clazzStr 类名
     * @return 对象 t
     * @since 1.0.0
     */
    @NotNull
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            return newInstance(clazz);
        } catch (ClassNotFoundException e) {
            throw new BasicException(e);
        }
    }

    /**
     * 实例化对象
     *
     * @param <T>   泛型标记
     * @param clazz 类
     * @return 对象 t
     * @since 1.0.0
     */
    @NotNull
    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiateClass(clazz);
    }

    /**
     * 获取Bean的属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @return 属性值 property
     * @since 1.0.0
     */
    public static Object getProperty(Object bean, String propertyName) {
        Assert.notNull(bean, "bean Could not null");
        return BeanMap.create(bean).get(propertyName);
    }

    /**
     * 设置Bean属性
     *
     * @param bean         bean
     * @param propertyName 属性名
     * @param value        属性值
     * @since 1.0.0
     */
    public static void setProperty(Object bean, String propertyName, Object value) {
        Assert.notNull(bean, "bean Could not null");
        BeanMap.create(bean).put(propertyName, value);
    }

    /**
     * 深复制
     * 注意: 不支持链式Bean
     *
     * @param <T>    泛型标记
     * @param source 源对象
     * @return T t
     * @since 1.0.0
     */
    public static <T> T clone(T source) {
        return (T) BeanUtils.copy(source, source.getClass());
    }

    /**
     * copy 对象属性到另一个对象,默认不使用Convert
     * 注意: 不支持链式Bean,链式用 copyProperties
     *
     * @param <T>    泛型标记
     * @param source 源对象
     * @param clazz  类名
     * @return T t
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    public static <T> T copy(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        return BeanUtils.copy(source, source.getClass(), clazz);
    }

    /**
     * copy 对象属性,默认不使用Convert
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param source      源对象
     * @param sourceClazz 源类型
     * @param targetClazz 转换成的类型
     * @return T t
     * @since 1.0.0
     */
    @Contract("null, _, _ -> null")
    public static <T> T copy(Object source, Class sourceClazz, Class<T> targetClazz) {
        BeanCopier copier = BeanCopier.create(sourceClazz, targetClazz, false);
        T to = newInstance(targetClazz);
        copier.copy(source, to, null);
        return to;
    }

    /**
     * copy 列表对象,默认不使用Convert
     * <p>
     * 支持 map bean copy
     * </p>
     *
     * @param <T>         泛型标记
     * @param sourceList  源列表
     * @param targetClazz 转换成的类型
     * @return T list
     * @since 1.0.0
     */
    @Contract("null, _ -> !null")
    public static <T> List<T> copy(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
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
            T bean = BeanUtils.copy(source, sourceClazz, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param <T>         泛型标记
     * @param sourceList  the source list bean
     * @param targetClazz the target bean class
     * @return List list
     * @throws BeansException if the copying failed
     * @since 1.0.0
     */
    @Contract("null, _ -> !null")
    public static <T> List<T> copyProperties(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> outList = new ArrayList<>(sourceList.size());
        for (Object source : sourceList) {
            if (source == null) {
                continue;
            }
            T bean = BeanUtils.copyProperties(source, targetClazz);
            outList.add(bean);
        }
        return outList;
    }

    /**
     * Copy the property values of the given source bean into the target class.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     *
     * @param <T>         泛型标记
     * @param source      the source bean
     * @param targetClazz the target bean class
     * @return T t
     * @throws BeansException if the copying failed
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @Nullable
    public static <T> T copyProperties(@Nullable Object source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        T to = newInstance(targetClazz);
        BeanUtils.copyProperties(source, to);
        return to;
    }

    /**
     * 将对象装成 map 形式.
     * 注意: 返回的 Map 不能使用 put 添加键值对, 因为 BeanMap 未做实现, 所以不会添加成功, 必须使用强转为 BeanMap 后才使用 put
     *
     * @param bean 源对象
     * @return {Map}
     * @since 1.0.0
     */
    @Contract("null -> new")
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        return BeanMap.create(bean);
    }

    /**
     * To string map map
     *
     * @param bean bean
     * @return the map
     * @since 1.0.0
     */
    @Contract("null -> !null")
    public static Map<String, String> toStringMap(Object bean) {
        Map<String, Object> map = toMap(bean);
        Map<String, String> stringMap = Maps.newHashMapWithExpectedSize(map.size());
        map.forEach((k, v) -> {
            if (ObjectUtils.isNotNull(v)) {
                stringMap.put(k, String.valueOf(v));
            }
        });
        return stringMap;
    }

    /**
     * 将map 转为 bean
     *
     * @param <T>       泛型标记
     * @param beanMap   map
     * @param valueType 对象类型
     * @return {T}
     * @since 1.0.0
     */
    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        Objects.requireNonNull(beanMap, "beanMap Could not null");
        T bean = BeanUtils.newInstance(valueType);
        if (beanMap.isEmpty()) {
            return bean;
        }
        BeanMap.create(bean).putAll(beanMap);
        return bean;
    }

    /**
     * 拷贝对象
     * 注意: 不支持链式Bean,链式用 copyProperties
     *
     * @param source     源对象
     * @param targetBean 需要赋值的对象
     * @since 1.0.0
     */
    public static void copy(@NotNull Object source, @NotNull Object targetBean) {
        BeanCopier copier = BeanCopier
            .create(source.getClass(), targetBean.getClass(), false);

        copier.copy(source, targetBean, null);
    }

    /**
     * 获取 Bean 的所有 get方法
     *
     * @param type 类
     * @return PropertyDescriptor数组 property descriptor [ ]
     * @since 1.0.0
     */
    public static PropertyDescriptor[] getBeanGetters(Class<?> type) {
        return getPropertiesHelper(type, true, false);
    }

    /**
     * Get properties helper property descriptor [ ]
     *
     * @param type  type
     * @param read  read
     * @param write write
     * @return the property descriptor [ ]
     * @since 1.0.0
     */
    private static PropertyDescriptor[] getPropertiesHelper(Class<?> type, boolean read, boolean write) {
        try {
            PropertyDescriptor[] all = BeanUtils.getPropertyDescriptors(type);
            if (read && write) {
                return all;
            } else {
                List<PropertyDescriptor> properties = new ArrayList<>(all.length);
                for (PropertyDescriptor pd : all) {
                    if (read && pd.getReadMethod() != null) {
                        properties.add(pd);
                    } else if (write && pd.getWriteMethod() != null) {
                        properties.add(pd);
                    }
                }
                return properties.toArray(new PropertyDescriptor[0]);
            }
        } catch (BeansException ex) {
            throw new CodeGenerationException(ex);
        }
    }

    /**
     * 获取 Bean 的所有 set方法
     *
     * @param type 类
     * @return PropertyDescriptor数组 property descriptor [ ]
     * @since 1.0.0
     */
    public static PropertyDescriptor[] getBeanSetters(Class<?> type) {
        return getPropertiesHelper(type, false, true);
    }

}
