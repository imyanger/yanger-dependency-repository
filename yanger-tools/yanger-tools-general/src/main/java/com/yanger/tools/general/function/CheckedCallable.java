package com.yanger.tools.general.function;

import org.jetbrains.annotations.Nullable;

/**
 * @Description 受检的 Callable
 * @Author yanger
 * @Date 2020/12/25 9:55
 */
@FunctionalInterface
public interface CheckedCallable<T> {

    /**
     * Run this callable.
     *
     * @return result t
     * @throws Throwable CheckedException
     */
    @Nullable
    T call() throws Throwable;

}
