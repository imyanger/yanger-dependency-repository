package com.yanger.web.tools;

import com.yanger.general.exception.BasicException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 类工具类
 * @Author yanger
 * @Date 2020/12/18 18:45
 */
public class ClassUtils extends org.springframework.util.ClassUtils {

    /** PARAMETER_NAME_DISCOVERER */
    public static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    /** PACKAGE_SEPARATOR */
    public static final char PACKAGE_SEPARATOR = '.';
    /** 代理 class 的名称 */
    public static final List<String> PROXY_CLASS_NAMES = Arrays.asList(
        "net.sf.cglib.proxy.Factory",
        "org.springframework.cglib.proxy.Factory",
        "javassist.util.proxy.ProxyObject",
        "org.apache.ibatis.javassist.util.proxy.ProxyObject");

    /**
     * 获取方法参数信息
     *
     * @param constructor    构造器
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     * @since 1.0.0
     */
    public static @NotNull MethodParameter getMethodParameter(Constructor<?> constructor, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(constructor, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return methodParameter;
    }

    /**
     * 获取方法参数信息
     *
     * @param method         方法
     * @param parameterIndex 参数序号
     * @return {MethodParameter}
     * @since 1.0.0
     */
    public static @NotNull MethodParameter getMethodParameter(Method method, int parameterIndex) {
        MethodParameter methodParameter = new SynthesizingMethodParameter(method, parameterIndex);
        methodParameter.initParameterNameDiscovery(PARAMETER_NAME_DISCOVERER);
        return methodParameter;
    }

    /**
     * 获取Annotation
     *
     * @param <A>            泛型标记
     * @param method         Method
     * @param annotationType 注解类
     * @return {Annotation}
     * @since 1.0.0
     */
    public static <A extends Annotation> A getAnnotation(@NotNull Method method, Class<A> annotationType) {
        Class<?> targetClass = method.getDeclaringClass();
        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 先找方法,再找方法上的类
        A annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, annotationType);
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation,可能包含组合注解,故采用spring的工具类
        return AnnotatedElementUtils.findMergedAnnotation(specificMethod.getDeclaringClass(), annotationType);
    }

    /**
     * 获取Annotation
     *
     * @param <A>            泛型标记
     * @param handlerMethod  HandlerMethod
     * @param annotationType 注解类
     * @return {Annotation}
     * @since 1.0.0
     */
    public static <A extends Annotation> A getAnnotation(@NotNull HandlerMethod handlerMethod, Class<A> annotationType) {
        // 先找方法,再找方法上的类
        A annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation,可能包含组合注解,故采用spring的工具类
        Class<?> beanType = handlerMethod.getBeanType();
        return AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
    }

    /**
     * 获取接口上的泛型T
     *
     * @param clazz          clazz
     * @param interfaceClass interface class
     * @param index          泛型索引
     * @return the interface t
     * @since 1.0.0
     */
    public static @NotNull Class<?> getInterfaceT(@NotNull Class<?> clazz, Class<?> interfaceClass, int index) {
        Type[] types = clazz.getGenericInterfaces();
        Type targetType = Arrays.stream(types).filter(type -> type.toString().contains(interfaceClass.getName())).findAny().orElse(null);
        if (targetType == null) {
            throw new BasicException("[{}] 未实现 [{}] 接口", clazz, interfaceClass);
        }
        ParameterizedType parameterizedType = (ParameterizedType) targetType;
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);
    }

    /**
     * 获取父类上的泛型
     *
     * @param clazz clazz
     * @param index index
     * @return the class
     * @since 1.0.0
     */
    public static @NotNull Class<?> getSuperClassT(@NotNull Class<?> clazz, @NotNull Integer index) {
        Type type = clazz.getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type actType = parameterizedType.getActualTypeArguments()[index];
        return checkType(actType, index);
    }

    /**
     * 递归获取泛型类型
     *
     * @param type  type
     * @param index index
     * @return the class
     * @since 1.0.0
     */
    @Contract("null, _ -> fail")
    private static @NotNull Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type t = parameterizedType.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new BasicException("Expected a Class, ParameterizedType" + ", but <" + type + "> is of type " + className);
        }
    }

    /**
     * 判断传入的类型是否是布尔类型
     *
     * @param type 类型
     * @return 如果是原生布尔或者包装类型布尔 , 均返回 true
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static boolean isBoolean(Class<?> type) {
        return type == boolean.class || Boolean.class == type;
    }

    /**
     * 判断是否为代理对象
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class, 返回 true
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前对象的 class
     *
     * @param clazz 传入
     * @return 如果是代理的class , 返回父 class, 否则返回自身
     * @since 1.0.0
     */
    public static @NotNull Class<?> getUserClass(Class<?> clazz) {
        return isProxy(clazz) ? clazz.getSuperclass() : clazz;
    }

    /**
     * 获取当前对象的class
     *
     * @param object 对象
     * @return 返回对象的 user class
     * @since 1.0.0
     */
    public static @NotNull Class<?> getUserClass(Object object) {
        Assert.notNull(object, "Error: Instance must not be null");
        return getUserClass(object.getClass());
    }

    /**
     * 根据指定的 class ,  实例化一个对象, 根据构造参数来实例化
     * 在 java9 及其之后的版本 Class.newInstance() 方法已被废弃
     *
     * @param <T>   类型, 由输入类型决定
     * @param clazz 需要实例化的对象
     * @return 返回新的实例 @ not null t
     * @since 1.0.0
     */
    public static <T> @NotNull T newInstance(@NotNull Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BasicException("实例化对象时出现错误,请尝试给 %s 添加无参的构造方法", e, clazz.getName());
        }
    }

    /**
     * 请仅在确定类存在的情况下调用该方法
     *
     * @param name 类名称
     * @return 返回转换后的 Class
     * @since 1.0.0
     */
    public static @NotNull Class<?> toClassConfident(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new BasicException("找不到指定的class！请仅在明确确定会有 class 的时候, 调用该方法", e);
        }
    }


    /**
     * Determine the name of the package of the given class,
     * e.g. "java.lang" for the {@code java.lang.String} class.
     *
     * @param clazz the class
     * @return the package name, or the empty String if the class     is defined in the default package
     * @since 1.0.0
     */
    public static @NotNull String getPackageName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return getPackageName(clazz.getName());
    }

    /**
     * Determine the name of the package of the given fully-qualified class name,
     * e.g. "java.lang" for the {@code java.lang.String} class name.
     *
     * @param fqClassName the fully-qualified class name
     * @return the package name, or the empty String if the class     is defined in the default package
     * @since 1.0.0
     */
    public static @NotNull String getPackageName(String fqClassName) {
        Assert.notNull(fqClassName, "Class name must not be null");
        int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

}