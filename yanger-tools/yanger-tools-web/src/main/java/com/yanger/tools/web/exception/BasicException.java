package com.yanger.tools.web.exception;

import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.format.StringFormat;
import com.yanger.tools.web.support.ResultCode;
import com.yanger.tools.web.support.Trace;
import com.yanger.tools.web.tools.NumberUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 基础异常类
 * @Author yanger
 * @Date 2020/12/18 10:36
 */
@Slf4j
public class BasicException extends RuntimeException {

    private static final long serialVersionUID = 3076052230646484392L;

    /** Code */
    @Getter
    protected String code;

    /** Message */
    @Getter
    protected String message;

    /** Trace id */
    @Getter
    protected String traceId;

    /** DEFAULT_ERROR_CODE */
    public static final String DEFAULT_ERROR_CODE = "BASIC-500";

    /** DEFAULT_MESSAGE */
    public static final String DEFAULT_MESSAGE = "服务内部错误";

    /**
     * Basic exception
     */
    public BasicException() {
        super();
        this.code = DEFAULT_ERROR_CODE;
        this.message = DEFAULT_MESSAGE;
        this.traceId = Trace.context().get();
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param message message
     */
    public BasicException(String message) {
        super();
        this.code = DEFAULT_ERROR_CODE;
        this.message = message;
        this.traceId = Trace.context().get();
    }

    /**
     * msg 占位符替换
     *
     * @param message message
     * @param args    args
     */
    public BasicException(String message, Object... args) {
        this(StringFormat.mergeFormat(message, args));
        this.code = DEFAULT_ERROR_CODE;
        this.message = message;
        this.traceId = Trace.context().get();
    }

    /**
     * 传入code参数的构造使用of方法，避免两个参数重载问题
     *
     * @param code    code
     * @param message message
     */
    public static BasicException of(String code, String message) {
        BasicException basicException = new BasicException();
        basicException.code = code;
        basicException.message = message;
        basicException.traceId = Trace.context().get();
        return basicException;
    }

    /**
     * 传入code参数的构造使用of方法，避免两个参数重载问题
     *
     * @param code    code
     * @param message message
     */
    public static BasicException of(String code, String message, Object... args) {
        BasicException basicException = new BasicException();
        basicException.code = code;
        basicException.message = StringFormat.mergeFormat(message, args);
        basicException.traceId = Trace.context().get();
        return basicException;
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
        log.error(this.getMessage(), this);
    }

    /**
     * 获取异常的返回状态码
     */
    public Integer getResultCode() {
        if(code != null) {
            if (NumberUtils.isNumer(code)) {
                return NumberUtils.toInt(code, ResultCode.ERROR.getCode());
            } else {
                return NumberUtils.toInt(code.split(StringPool.DASH)[1], ResultCode.ERROR.getCode());
            }
        }
        return ResultCode.ERROR.getCode();
    }

}
