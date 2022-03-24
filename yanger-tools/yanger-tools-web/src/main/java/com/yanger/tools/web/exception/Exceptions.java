package com.yanger.tools.web.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FastStringWriter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常处理工具类
 * @Author yanger
 * @Date 2020/12/21 9:45
 */
public class Exceptions extends ExceptionUtil {

    /**
     * 将 CheckedException 转换为 UncheckedException.
     * @param e Throwable
     * @return {RuntimeException}
     */
    @NotNull
    @Contract("null -> new")
    public static RuntimeException unchecked(Throwable e) {
        return unchecked(e.getMessage(), e);
    }

    /**
     * 将受检异常转换为 unchecked 异常
     * @param message message
     * @param e       e
     * @return the runtime exception
     */
    @NotNull
    public static RuntimeException unchecked(String message, Throwable e) {
        RuntimeException exception;
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
            || e instanceof NoSuchMethodException) {
            exception = new IllegalArgumentException(message, e);
        } else if (e instanceof InvocationTargetException) {
            exception = new RuntimeException(message, ((InvocationTargetException) e).getTargetException());
        } else if (e instanceof BasicException) {
            exception = (BasicException) e;
        } else if (e instanceof RuntimeException) {
            exception = (RuntimeException) e;
        } else {
            exception = new BasicException(e, message);
        }
        return exception;
    }

    /**
     * 代理异常解包
     * @param wrapped 包装过得异常
     * @return 解包后的异常 throwable
     */
    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

    /**
     * 将 ErrorStack 转化为 String.
     * @param ex Throwable
     * @return {String}
     */
    @SuppressWarnings("all")
    public static String getStackTraceAsString(@NotNull Throwable ex) {
        FastStringWriter stringWriter = new FastStringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

}
