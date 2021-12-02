package com.yanger.tools.web.support;

import com.alibaba.ttl.TransmittableThreadLocal;

import org.jetbrains.annotations.Contract;

/**
 * 用来存储traceID相关信息
 * @Author yanger
 * @Date 2020/12/18 11:27
 */
public class Trace {

    /** context */
    private static final TransmittableThreadLocal<String> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * Context
     *
     * @return the thread context map
     * @Author yanger
     */
    @Contract(pure = true)
    public static TransmittableThreadLocal<String> context() {
        return CONTEXT;
    }

    /**
     * Clear
     *
     * @Author yanger
     */
    @Contract(pure = true)
    public static void clear() {
        CONTEXT.remove();
    }

}
