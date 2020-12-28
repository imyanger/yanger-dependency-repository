package com.yanger.tools.general.function;

/**
 * @Description 受检的 Consumer
 * @Author yanger
 * @Date 2020/12/25 9:56
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    /**
     * Run the Consumer
     *
     * @param t T
     * @throws Throwable UncheckedException
     */
    void accept(T t) throws Throwable;

}