package com.yanger.tools.web.exception;

import com.yanger.tools.general.format.StringFormat;
import com.yanger.tools.web.support.IResultCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 基础异常类
 * @Author yanger
 * @Date 2020/12/18 10:36
 */
@Slf4j
public class BasicException extends RuntimeException implements IResultCode {

    private static final long serialVersionUID = 3076052230646484392L;

    /** Code */
    @Getter
    protected Integer code;

    /** simple message */
    @Getter
    protected String simpleMessage;

    /** Message */
    @Getter
    protected String message;

    /** DEFAULT_ERROR_CODE */
    public static final Integer DEFAULT_ERROR_CODE = 500;

    /** DEFAULT_MESSAGE */
    public static final String DEFAULT_MESSAGE = "服务内部错误";

    /**
     * Basic exception
     */
    public BasicException() {
        super();
        this.code = DEFAULT_ERROR_CODE;
        this.message = DEFAULT_MESSAGE;
        this.simpleMessage = DEFAULT_MESSAGE;
    }

    public BasicException(String message) {
        super();
        this.code = DEFAULT_ERROR_CODE;
        this.simpleMessage = message;
        this.message = generateFullMessage(getModuleMarker(), code, message);
    }

    public BasicException(Integer code, String message) {
        super();
        this.code = code;
        this.simpleMessage = message;
        this.message = generateFullMessage(getModuleMarker(), code, message);
    }

    /**
     * @param code
     * @param message
     * @param args
     * @return {@link null}
     * @Author yanger
     * @Date 2022/03/21 14:46
     */
    public BasicException(Integer code, String message, Object... args) {
        super();
        this.code = code;
        this.simpleMessage = StringFormat.format(message, args);
        this.message = generateFullMessage(getModuleMarker(), code, message, args);
    }

    public BasicException(IResultCode resultCode) {
        super();
        this.code = resultCode.getCode();
        this.simpleMessage = resultCode.getMessage();
        this.message = generateFullMessage(resultCode.getModuleMarker(), resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * @param resultCode
     * @param args
     * @return {@link null}
     * @Author yanger
     * @Date 2022/03/21 14:46
     */
    public BasicException(IResultCode resultCode, Object... args) {
        super();
        this.code = resultCode.getCode();
        this.simpleMessage = StringFormat.format(resultCode.getMessage(), args);
        this.message = generateFullMessage(resultCode.getModuleMarker(), resultCode.getCode(), resultCode.getMessage(), args);
    }

    public BasicException(Throwable cause) {
        super(cause);
        this.code = DEFAULT_ERROR_CODE;
        this.simpleMessage = DEFAULT_MESSAGE;
        this.message = DEFAULT_MESSAGE;
    }

    public BasicException(Throwable cause, String message) {
        super(cause);
        this.code = DEFAULT_ERROR_CODE;
        this.simpleMessage = message;
        this.message = message;
    }

    public BasicException(Throwable cause, String message, Object... args) {
        super(cause);
        this.code = DEFAULT_ERROR_CODE;
        this.simpleMessage = StringFormat.format(message, args);
        this.message = generateFullMessage(getModuleMarker(), code, message, args);
    }

    /**
     * @param cause
     * @param code
     * @param message
     * @param args
     * @return {@link null}
     * @Author yanger
     * @Date 2022/03/21 14:46
     */
    public BasicException(Throwable cause, Integer code, String message, Object... args) {
        super(cause);
        this.code = code;
        this.simpleMessage = StringFormat.format(message, args);
        this.message = generateFullMessage(getModuleMarker(), code, message, args);
    }

    public BasicException(Throwable cause, IResultCode resultCode) {
        super(cause);
        this.code = resultCode.getCode();
        this.simpleMessage = resultCode.getMessage();
        this.message = generateFullMessage(resultCode.getModuleMarker(), resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * @param cause
     * @param resultCode
     * @param args
     * @return {@link null}
     * @Author yanger
     * @Date 2022/03/21 14:50
     */
    public BasicException(Throwable cause, IResultCode resultCode, Object... args) {
        super(cause);
        this.code = resultCode.getCode();
        this.simpleMessage = StringFormat.format(resultCode.getMessage(), args);
        this.message = generateFullMessage(resultCode.getModuleMarker(), resultCode.getCode(), resultCode.getMessage(), args);
    }

    /**
     * 使用默认code
     * @param message
     * @param args
     * @return {@link null}
     * @Author yanger
     * @Date 2022/03/21 14:46
     */
    public static BasicException of(String message, Object... args) {
        return new BasicException(DEFAULT_ERROR_CODE, message, args);
    }

    /**
     * 使用默认code
     * @param cause
     * @param message
     * @param args
     * @return {@link null}
     * @Author yanger
     * @Date 2022/03/21 14:46
     */
    public static BasicException of(Throwable cause, String message, Object... args) {
        return new BasicException(cause, DEFAULT_ERROR_CODE, message, args);
    }

}
