package com.yanger.tools.general.function;

import org.jetbrains.annotations.Nullable;

/**
 * @Description 受检的 function
 * @Author yanger
 * @Date 2020/12/25 9:56
 */
@FunctionalInterface
public interface CheckedFunction<T, R> {

    /**
     * Run the Function
     *
     * @param t T
     * @return R R
     * @throws Throwable CheckedException
     */
    @Nullable
    R apply(@Nullable T t) throws Throwable;

}
