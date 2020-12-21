package com.yanger.web.entity;

import com.yanger.general.support.IResultCode;
import com.yanger.general.support.ResultCode;
import com.yanger.web.support.Trace;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @Description 通用返回体
 * @Author yanger
 * @Date 2020/12/21 18:23
 */
public final class R<T> extends Result<T> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3077918845714343375L;

    /**
     * R
     *
     * @param code    code
     * @param message message
     * @param data    data
     * @param success    success
     * @since 1.0.0
     */
    @Contract(pure = true)
    private R(Integer code, String message, T data, boolean success) {
        super(code, message, data, Trace.context().get(), success);
    }

    /**
     * Build result
     *
     * @param <T>     parameter
     * @param code    code
     * @param msg     msg
     * @param data    data
     * @param success success
     * @return the result
     * @since 1.0.0
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> build(Integer code, String msg, T data, boolean success) {
        return new R<>(code, msg, data, success);
    }

    /**
     * Succeed result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @param data data
     * @return the result
     * @since 1.0.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(Integer code, String msg, T data) {
        return build(code, msg, data, true);
    }

    /**
     * Succeed result
     *
     * @param <T>  parameter
     * @param msg  msg
     * @param data data
     * @return the result
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(String msg, T data) {
        return succeed(ResultCode.OK.getCode(), msg, data);
    }

    /**
     * Succeed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param data       data
     * @return the result
     * @since 1.0.0
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(IResultCode resultCode, T data) {
        return succeed(resultCode.getCode(), resultCode.getMessage(), data);
    }

    /**
     * Succeed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param data       data
     * @param msg        msg
     * @return the result
     * @since 1.0.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(IResultCode resultCode, T data, Object... msg) {
        return succeed(resultCode.getCode(), resultCode.generateMessage(msg), data);
    }

    /**
     * Succeed result
     *
     * @param <T>  parameter
     * @param data data
     * @return the result
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(T data) {
        return succeed(ResultCode.OK.getCode(), ResultCode.OK.getMessage(), data);
    }

    /**
     * Succeed result
     *
     * @param <T> parameter
     * @param msg msg
     * @return the result
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(String msg) {
        return succeed(ResultCode.OK.getCode(), msg, null);
    }

    /**
     * Succeed result
     *
     * @param <T> parameter
     * @return the result
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> succeed() {
        return succeed(null);
    }

    /**
     * Failed result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @param data data
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> Result<T> failed(Integer code, String msg, T data) {
        return build(code, msg, data, false);
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param data       data
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> Result<T> failed(IResultCode resultCode, T data) {
        return build(resultCode.getCode(), resultCode.getMessage(), data, false);
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param data       data
     * @param msg        msg
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> Result<T> failed(IResultCode resultCode, T data, Object... msg) {
        return build(resultCode.getCode(), resultCode.generateMessage(msg), data, false);
    }

    /**
     * Failed result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> Result<T> failed(Integer code, String msg) {
        return failed(code, msg, null);
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> Result<T> failed(@NotNull IResultCode resultCode) {
        return failed(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     * @since 1.0.0
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> Result<T> failed(@NotNull IResultCode resultCode, Object... msg) {
        return failed(resultCode.getCode(), resultCode.generateMessage(msg));
    }

    /**
     * Failed result
     *
     * @param <T> parameter
     * @param msg msg
     * @return the result
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> failed(String msg) {
        return failed(ResultCode.ERROR.getCode(), msg);
    }

    /**
     * Failed result
     *
     * @param <T> parameter
     * @return the result
     * @since 1.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> failed() {
        return failed(ResultCode.ERROR);
    }


    /**
     * Status result
     *
     * @param <T>        parameter
     * @param expression expression
     * @return the result
     * @since 1.0.0
     */
    @Contract("_ -> !null")
    public static <T> Result<T> of(boolean expression) {
        return of(expression, "");
    }

    /**
     * Status result
     *
     * @param <T>        parameter
     * @param expression expression
     * @param resultCode result code
     * @return the result
     * @since 1.0.0
     */
    @Contract("_, _ -> !null")
    public static <T> Result<T> of(boolean expression, @NotNull IResultCode resultCode) {
        return of(expression, resultCode.getMessage());
    }

    /**
     * Status result
     *
     * @param <T>        parameter
     * @param expression expression
     * @param message    message
     * @return the result
     * @since 1.0.0
     */
    @Contract("_, _ -> !null")
    public static <T> Result<T> of(boolean expression, String message) {
        return expression ? succeed() : failed(message);
    }

}
