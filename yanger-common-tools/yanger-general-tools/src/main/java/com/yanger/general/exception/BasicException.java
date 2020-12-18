package com.yanger.general.exception;

import com.yanger.general.format.StringFormatter;
import com.yanger.general.support.Trace;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 基础异常类
 * @Author yanger
 * @Date 2020/12/18 10:36
 */
@Slf4j
public class BasicException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 3076052230646484392L;

    /** Code */
    @Getter
    protected String code;

    /** Message */
    protected String message;

    /** Trace id */
    @Getter
    protected String traceId;

    /** DEFAULT_ERROR_CODE */
    public static final String DEFAULT_ERROR_CODE = "Basic-500";

    /** DEFAULT_MESSAGE */
    public static final String DEFAULT_MESSAGE = "服务内部错误";

    /**
     * Basic exception
     *

     */
    public BasicException() {
        super(DEFAULT_MESSAGE);
        this.code = DEFAULT_ERROR_CODE;
        this.message = DEFAULT_MESSAGE;
        this.traceId = Trace.context().get();
    }

    /**
     * msg 占位符替换
     *
     * @param message message
     * @param args    args

     */
    public BasicException(String message, Object... args) {
        this(StringFormatter.mergeFormat(message, args));
        this.code = DEFAULT_ERROR_CODE;
        this.message = message;
        this.traceId = Trace.context().get();
    }

    /**
     * Basic exception
     *
     * @param code    code
     * @param message message

     */
    public BasicException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.traceId = Trace.context().get();
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param message message

     */
    public BasicException(String message) {
        super(message);
        this.code = DEFAULT_ERROR_CODE;
        this.message = message;
        this.traceId = Trace.context().get();
    }

    /**
     * Base exception
     *
     * @param cause cause

     */
    public BasicException(Throwable cause) {
        super(cause);
        this.code = DEFAULT_ERROR_CODE;
        this.message = DEFAULT_MESSAGE;
        this.traceId = Trace.context().get();
    }

    /**
     * Base exception
     *
     * @param message message
     * @param cause   cause

     */
    public BasicException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT_ERROR_CODE;
        this.message = message;
        this.traceId = Trace.context().get();
    }

    /**
     * Basic exception
     *
     * @param code    code
     * @param message message
     * @param cause   cause

     */
    public BasicException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.traceId = Trace.context().get();
    }

    /**
     * 重写打印异常堆栈, 转为日志输出.
     */
    @Override
    public void printStackTrace() {
        log.error("", this);
    }

}
