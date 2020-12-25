package com.yanger.general.function;

/**
 * @Description 受检的 runnable
 * @Author yanger
 * @Date 2020/12/25 9:53
 */
@FunctionalInterface
public interface CheckedRunnable {

    /**
     * Run this runnable.
     *
     * @throws Throwable CheckedException
     */
    void run() throws Throwable;

}
