package com.yanger.tools.web.entity;

import com.yanger.tools.web.context.Trace;
import com.yanger.tools.web.support.DefaultResultCode;
import com.yanger.tools.web.support.IResultCode;
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
     * @param code    code
     * @param message message
     * @param data    data
     * @param success success
     */
    @Contract(pure = true)
    private R(Integer code, String message, T data, boolean success, String moduleMarker) {
        super(code, message, data, success, Trace.context().get(), moduleMarker);
    }

    /**
     * Build result
     * @param <T>     parameter
     * @param code    code
     * @param msg     msg
     * @param data    data
     * @param success success
     * @param moduleMarker moduleMarker
     * @return the result
     */
    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> build(Integer code, String msg, T data, boolean success, String moduleMarker) {
        return new R<>(code, msg, data, success, moduleMarker);
    }

    /**
     * succeed result
     * @param <T>  parameter
     * @param data data
     * @param code code
     * @param msg  msg
     * @param moduleMarker moduleMarker
     * @return the result
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeed(Integer code, String msg, T data, String moduleMarker) {
        return build(code, msg, data, true, moduleMarker);
    }

    /**
     * succeed result
     * @param <T>  parameter
     * @param data data
     * @param code code
     * @param msg  msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeed(Integer code, String msg, T data) {
        return succeed(code, msg, data, null);
    }

    /**
     * succeed result
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @return the result
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeed(Integer code, String msg){
        return succeed(code, msg, null, null);
    }

    /**
     * succeed result
     * @param <T>        parameter
     * @param data       data
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeed(T data, IResultCode resultCode, Object... msg) {
        return succeed(resultCode.getCode(), resultCode.generateMessage(msg), data, resultCode.getModuleMarker());
    }

    /**
     * succeed result
     * @param <T>        parameter
     * @param data       data
     * @param resultCode result code
     * @return the result
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeed(T data, IResultCode resultCode) {
        return succeed(resultCode.getCode(), resultCode.generateMessage(), data, resultCode.getModuleMarker());
    }

    /**
     * succeed result
     * @param <T>        parameter
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeed(IResultCode resultCode, Object... msg) {
        return succeed(null, resultCode, msg);
    }
    
    /**
     * succeed result
     * @param <T>        parameter
     * @param resultCode result code
     * @return the result
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeed(IResultCode resultCode) {
        return succeed(null, resultCode);
    }

    /**
     * succeed result
     * @param <T>  parameter
     * @param data data
     * @return the result
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeed(T data) {
        return succeed(DefaultResultCode.OK.getCode(), DefaultResultCode.OK.getMessage(), data);
    }

    /**
     * succeed result
     * @param <T> parameter
     * @return the result
     */
    @Contract(pure = true)
    @NotNull
    public static <T> R<T> succeed() {
        return succeed(DefaultResultCode.OK);
    }

    /**
     * succeed result
     * @param <T>  parameter
     * @param message message
     * @return the result
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> R<T> succeedDefault(String message) {
        return succeed(DefaultResultCode.OK.getCode(), message, null);
    }

    
    /**
     * failed result
     * @param <T>  parameter
     * @param data data
     * @param code code
     * @param msg  msg
     * @param moduleMarker moduleMarker
     * @return the result
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> failed(Integer code, String msg, T data, String moduleMarker) {
        return build(code, msg, data, false, moduleMarker);
    }

    /**
     * failed result
     * @param <T>  parameter
     * @param data data
     * @param code code
     * @param msg  msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> failed(Integer code, String msg, T data) {
        return failed(code, msg, data, null);
    }

    /**
     * failed result
     * @param <T>  parameter
     * @param code code
     * @param msg  msg
     * @return the result
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> failed(Integer code, String msg){
        return failed(code, msg, null, null);
    }

    /**
     * failed result
     * @param <T>        parameter
     * @param data       data
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> failed(T data, IResultCode resultCode, Object... msg) {
        return failed(resultCode.getCode(), resultCode.generateMessage(msg), data, resultCode.getModuleMarker());
    }

    /**
     * failed result
     * @param <T>        parameter
     * @param data       data
     * @param resultCode result code
     * @return the result
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> failed(T data, IResultCode resultCode) {
        return failed(resultCode.getCode(), resultCode.generateMessage(), data, resultCode.getModuleMarker());
    }

    /**
     * failed result
     * @param <T>        parameter
     * @param resultCode result code
     * @param msg        msg
     * @return the result
     */
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    public static <T> R<T> failed(IResultCode resultCode, Object... msg) {
        return failed(null, resultCode, msg);
    }

    /**
     * succeed result
     * @param <T>        parameter
     * @param resultCode result code
     * @return the result
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> R<T> failed(IResultCode resultCode) {
        return failed(null, resultCode);
    }

    /**
     * failed result
     * @param <T>  parameter
     * @param data data
     * @return the result
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> R<T> failed(T data) {
        return failed(DefaultResultCode.ERROR.getCode(), DefaultResultCode.ERROR.getMessage(), data);
    }

    /**
     * failed result
     * @param <T> parameter
     * @return the result
     */
    @Contract(pure = true)
    @NotNull
    public static <T> R<T> failed() {
        return failed(DefaultResultCode.ERROR);
    }

    /**
     * failed result
     * @param <T>  parameter
     * @param message message
     * @return the result
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    public static <T> R<T> failedDefault(String message) {
        return failed(DefaultResultCode.ERROR.getCode(), message, null);
    }
    
    
    /**
     * Status result
     * @param <T>        parameter
     * @param expression expression
     * @return the result
     */
    @Contract("_ -> !null")
    public static <T> R<T> of(boolean expression) {
        return of(expression, DefaultResultCode.ERROR);
    }

    /**
     * Status result
     * @param <T>        parameter
     * @param expression expression
     * @param resultCode result code
     * @return the result
     */
    @Contract("_, _ -> !null")
    public static <T> R<T> of(boolean expression, @NotNull IResultCode resultCode) {
        return expression ? succeed() : failed(resultCode);
    }

    /**
     * Status result
     * @param <T>        parameter
     * @param expression expression
     * @param message    message
     * @return the result
     */
    @Contract("_, _, _ -> !null")
    public static <T> R<T> of(boolean expression, Integer code,  String message) {
        return expression ? succeed() : failed(code, message);
    }

    /**
     * Status result
     * @param <T>        parameter
     * @param expression expression
     * @param resultCode result code
     * @return the result
     */
    @Contract("_, _, _ -> !null")
    public static <T> R<T> of(boolean expression, T data, @NotNull IResultCode resultCode) {
        return expression ? succeed(data) : failed(data, resultCode);
    }

    /**
     * Status result
     * @param <T>        parameter
     * @param expression expression
     * @param message    message
     * @return the result
     */
    @Contract("_, _, _, _ -> !null")
    public static <T> R<T> of(boolean expression, T data, Integer code,  String message) {
        return expression ? succeed(data) : failed(code, message, data);
    }
    
}
