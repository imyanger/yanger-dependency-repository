package com.yanger.tools.web.tools;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单超时工具类
 * @Author yanger
 * @Date 2021/2/22 18:13
 */
@Slf4j
@UtilityClass
@SuppressWarnings("all")
public final class TimeoutUtil {

    private static final ExecutorService TIMEOUT_EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * Process
     *
     * @param <T>     parameter
     * @param task    task
     * @param timeout timeout
     * @return the t
     */
    @Contract("null, _ -> null")
    public static <T> T process(Callable<T> task, long timeout) {
        if (task == null) {
            return null;
        }
        Future<T> futureRet = TIMEOUT_EXECUTOR.submit(task);
        try {
            return futureRet.get(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Interrupt Exception", e);
        } catch (ExecutionException e) {
            log.error("Task execute exception", e);
        } catch (TimeoutException e) {
            log.warn("Process Timeout");
            if (!futureRet.isCancelled()) {
                futureRet.cancel(true);
            }
        }
        return null;
    }

}
