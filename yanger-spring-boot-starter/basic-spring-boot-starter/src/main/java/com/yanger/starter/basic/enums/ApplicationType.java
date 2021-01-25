package com.yanger.starter.basic.enums;

import org.springframework.util.ClassUtils;

/**
 * @Description 应用类型
 * @Author yanger
 * @Date 2020/12/29 16:12
 */
public enum ApplicationType {

    /** 非 web 应用, 启动完成后将自动退出 */
    NONE,

    /**
     * 非 web 应用, 但是启动后不会退出, 比如 dubbo 服务提供者, 只提供 RPC 服务.
     * 这种类型有 2 中情况:
     * 一是 class 存在 web 依赖, 但是启动时关闭了 web 功能 (web 相关的依赖也会被打包)
     * 二是不存在 web 相关依赖, 作为一个普通应用启动, 但是不能退出.
     */
    SERVICE,

    /** web 应用 */
    SERVLET,

    /** webflux 应用 */
    REACTIVE;

    /** SERVLET_INDICATOR_CLASSES */
    private static final String[] SERVLET_INDICATOR_CLASSES = {
        "javax.servlet.Servlet",
        "org.springframework.web.context.ConfigurableWebApplicationContext"
    };

    /** WEBMVC_INDICATOR_CLASS */
    private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

    /** WEBFLUX_INDICATOR_CLASS */
    private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

    /** JERSEY_INDICATOR_CLASS */
    private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

    /** SERVLET_APPLICATION_CONTEXT_CLASS */
    private static final String SERVLET_APPLICATION_CONTEXT_CLASS = "org.springframework.web.context.WebApplicationContext";

    /** REACTIVE_APPLICATION_CONTEXT_CLASS */
    private static final String REACTIVE_APPLICATION_CONTEXT_CLASS = "org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext";

    /**
     * Deduce from classpath web application type
     *
     * @return the web application type
     */
    public static ApplicationType deduceFromClasspath() {
        if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
            && !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
            return ApplicationType.REACTIVE;
        }
        for (String className : SERVLET_INDICATOR_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                return ApplicationType.NONE;
            }
        }
        return ApplicationType.SERVLET;
    }

    /**
     * Deduce from application context web application type
     *
     * @param applicationContextClass application context class
     * @return the web application type
     */
    public static ApplicationType deduceFromApplicationContext(Class<?> applicationContextClass) {
        if (isAssignable(SERVLET_APPLICATION_CONTEXT_CLASS, applicationContextClass)) {
            return ApplicationType.SERVLET;
        }
        if (isAssignable(REACTIVE_APPLICATION_CONTEXT_CLASS, applicationContextClass)) {
            return ApplicationType.REACTIVE;
        }
        return ApplicationType.NONE;
    }

    /**
     * Is assignable boolean
     *
     * @param target target
     * @param type   type
     * @return the boolean
     */
    private static boolean isAssignable(String target, Class<?> type) {
        try {
            return ClassUtils.resolveClassName(target, null).isAssignableFrom(type);
        } catch (Throwable ex) {
            return false;
        }
    }

}
