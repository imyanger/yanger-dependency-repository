package com.yanger.web.tools;

import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

import lombok.experimental.UtilityClass;

/**
 * @Description Aop获取代理对象
 * @Author yanger
 * @Date 2020/12/21 10:35
 */
@UtilityClass
public class AopTargetUtils {

    /**
     * 获取 目标对象
     *
     * @param proxy 代理对象
     * @return target
     * @throws Exception exception
     */
    public static Object getTarget(Object proxy) throws Exception {

        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }

        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else {
            return getCglibProxyTargetObject(proxy);
        }

    }

    /**
     * Gets cglib proxy target object *
     *
     * @param proxy proxy
     * @return the cglib proxy target object
     * @throws Exception exception
     */
    private static Object getCglibProxyTargetObject(@NotNull Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }

    /**
     * Gets jdk dynamic proxy target object *
     *
     * @param proxy proxy
     * @return the jdk dynamic proxy target object
     * @throws Exception exception
     */
    private static Object getJdkDynamicProxyTargetObject(@NotNull Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }

}
