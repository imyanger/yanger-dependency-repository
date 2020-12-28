package com.yanger.tools.general.function;

import org.jetbrains.annotations.Nullable;

/**
 * @Description 受检的 Supplier
 * @Author yanger
 * @Date 2020/12/25 9:56
 */
@FunctionalInterface
public interface CheckedSupplier<T> {

    /**
     * Run the Supplier
     *
     * @return T t
     * @throws Throwable CheckedException
     */
    @Nullable
    T get() throws Throwable;

}
