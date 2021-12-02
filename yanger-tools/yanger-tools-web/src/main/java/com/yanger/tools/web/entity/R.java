package com.yanger.tools.web.entity;

import com.yanger.tools.web.support.IResultCode;
import com.yanger.tools.web.support.ResultCode;
import com.yanger.tools.web.support.Trace;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * 通用返回体
 * @Author yanger
 * @Date 2020/12/21 18:23
 */
public final class R<T> extends Result<T> {

    private static final long serialVersionUID = 3077918845714343375L;

    /**
     * R
     *
     * @param code    code
     * @param message message
     * @param data    data
     * @param success success
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
     * @param data data
     * @param code code
     * @param msg  msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(T data, Integer code, String msg) {
        return build(code, msg, data, true);
    }

    /**
     * Succeed result
     *
     * @param <T>        parameter
     * @param data       data
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(T data, IResultCode resultCode, Object... msg) {
        return succeed(data, resultCode.getCode(), resultCode.generateMessage(msg));
    }

    /**
     * Succeed result
     *
     * @param <T>        parameter
     * @param data       data
     * @param resultCode result code
     * @return the result
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(T data, IResultCode resultCode) {
        return succeed(data, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * Succeed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(IResultCode resultCode, Object... msg) {
        return succeed(null, resultCode.getCode(), resultCode.generateMessage(msg));
    }

    /**
     * Succeed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @return the result
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(IResultCode resultCode) {
        return succeed(null, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * Succeed result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(Integer code, String msg) {
        return succeed(null, code, msg);
    }

    /**
     * Succeed result
     *
     * @param <T>  parameter
     * @param data data
     * @return the result
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> Result<T> succeed(T data) {
        return succeed(data, ResultCode.OK.getCode(), ResultCode.OK.getMessage());
    }

    /**
     * Succeed result
     *
     * @param <T> parameter
     * @return the result
     */
    @Contract(pure = true)
    @NotNull
    public static <T> Result<T> succeed() {
        return succeed(ResultCode.OK);
    }

    /**
     * Failed result
     *
     * @param <T>  parameter
     * @param data data
     * @param code code
     * @param msg  msg
     * @return the result
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> Result<T> failed(T data, Integer code, String msg) {
        return build(code, msg, data, false);
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param data       data
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     */
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public static <T> Result<T> failed(T data, IResultCode resultCode, Object... msg) {
        return failed(data, resultCode.getCode(), resultCode.generateMessage(msg));
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param data       data
     * @param resultCode result code
     * @return the result
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> Result<T> failed(T data, IResultCode resultCode) {
        return failed(data, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> Result<T> failed(@NotNull IResultCode resultCode, Object... msg) {
        return failed(null, resultCode.getCode(), resultCode.generateMessage(msg));
    }

    /**
     * Failed result
     *
     * @param <T>        parameter
     * @param resultCode result code
     * @return the result
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> Result<T> failed(@NotNull IResultCode resultCode) {
        return failed(null, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * Failed result
     *
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @return the result
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static <T> Result<T> failed(Integer code, String msg) {
        return failed(null, code, msg);
    }

    /**
     * Failed result
     *
     * @param <T> parameter
     * @param msg msg
     * @return the result
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
     */
    @Contract("_, _ -> !null")
    public static <T> Result<T> of(boolean expression, String message) {
        return expression ? succeed() : failed(message);
    }

}
