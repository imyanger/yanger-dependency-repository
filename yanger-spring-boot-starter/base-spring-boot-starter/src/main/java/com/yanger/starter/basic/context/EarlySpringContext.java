package com.yanger.starter.basic.context;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * 以静态变量保存 Spring ApplicationContext, 可在任何代码任何地方任何时候取出 ApplicaitonContext
 * 以前使用 ApplicationContextAware 接口注入, 但是有时需要在 bean 中使用, 不能确保在使用之前已经将 ApplicaitonContext 注入
 * 可能会出现 null 异常, 因此修改为使用 ApplicationContextInitializer,
 * 会在 ConfigurableApplicationContext 类型（或者子类型）的 ApplicationContext 做 refresh 之前,
 * 允许我们对 ConfigurableApplicationContext 的实例做进一步的设置或者处理, 能有效解决上述问题.
 * 此类在 Spring Cloud 中会被多次刷新.
 *
 * @Author yanger
 * @Date 2021/2/22 10:00
 */
@Slf4j
public class EarlySpringContext implements ApplicationContextInitializer<ConfigurableApplicationContext>, DisposableBean {

    private static ConfigurableApplicationContext applicationContext = null;

    /**
     * 取得存储在静态变量中的 ApplicationContext
     */
    @Contract(pure = true)
    public static ConfigurableApplicationContext getApplicationContext() {
        InnerContext.assertContextInjected(applicationContext);
        return applicationContext;
    }

    /**
     * Set application context.
     * @param context the context
     */
    public static void setApplicationContext(ConfigurableApplicationContext context) {
        applicationContext = context;
    }

    /**
     * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
     * @param <T>  the type parameter
     * @param name the name
     * @return the bean
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String name) {
        return (T) InnerContext.getInstance(applicationContext, name);
    }

    /**
     * 从静态变量 applicationContext 中取得 Bean, 自动转型为所赋值对象的类型.
     * @param <T>          the type parameter
     * @param requiredType the required type
     * @return the bean
     */
    @NotNull
    public static <T> T getInstance(Class<T> requiredType) {
        return InnerContext.getInstance(applicationContext, requiredType);
    }

    /**
     * 通过 beanName 和 beanClass 获取 bean, 一般用于多个不同名称的相同 bean
     * @param <T>       the type parameter
     * @param beanClass the bean class
     * @param beanName  the bean name
     * @return the instance
     */
    @NotNull
    public static <T> T getInstance(Class<T> beanClass, String beanName) {
        return InnerContext.getInstance(applicationContext, beanClass, beanName);
    }

    /**
     * 获取容器中特定注解的所有 bean
     * @param annotationType the annotation type
     * @return the map
     */
    @NotNull
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        return InnerContext.getBeansWithAnnotation(applicationContext, annotationType);
    }

    /**
     * 获取指定 class 的所有实现类
     * @param clazz the clazz
     * @return the impl class
     */
    @NotNull
    public static List<Class<?>> getImplClass(Class<?> clazz) {
        return InnerContext.getImplClass(applicationContext, clazz);
    }

    /**
     * 获取指定 class 的所有实现类
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return the impl instance
     */
    @NotNull
    public static <T> Map<String, T> getImplInstance(Class<T> clazz) {
        return InnerContext.getImplInstance(applicationContext, clazz);
    }

    /**
     * 使用自定义的 context 注册 bean, 防止 applicationContext 还未初始化时调用导致异常
     * @param context  context
     * @param beanName bean name
     * @param target   target
     */
    public static void registerBean(@NotNull ConfigurableApplicationContext context, String beanName, Object target) {
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        // 注册bean
        log.info("register bean by custom, bean name = {}", beanName);
        defaultListableBeanFactory.registerSingleton(beanName, target);
    }

    /**
     * 在应用启动过程中, applicationContext 执行了 refresh 之后才能调用此方法.
     * @param applicationEvent application event
     */
    public static void publishEvent(ApplicationEvent applicationEvent) {
        InnerContext.publishEvent(applicationContext, applicationEvent);
    }

    /**
     * Initialize the given application context.
     * 实现 ApplicationContextInitializer 接口, 在所有 bean 初始化之前注入, 能解决不能确定 bean 初始化顺序导致 ApplicationContext 注入失败的问题
     * @param applicationContext the application to configure
     */
    @Override
    public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
        log.debug("初始化 SpringContext :{}", applicationContext);
        if (EarlySpringContext.applicationContext != null) {
            log.info("SpringContext's ApplicationContext override, old ApplicationContext is: {}", EarlySpringContext.applicationContext);
        }
        EarlySpringContext.applicationContext = applicationContext;
    }

    /**
     * 实现 DisposableBean 接口, 在Context关闭时清理静态变量.
     */
    @Override
    public void destroy() {
        InnerContext.destroy(applicationContext);
    }

}
