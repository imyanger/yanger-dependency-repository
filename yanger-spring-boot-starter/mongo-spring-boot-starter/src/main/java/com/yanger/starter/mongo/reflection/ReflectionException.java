package com.yanger.starter.mongo.reflection;

import com.yanger.tools.web.exception.BasicException;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class ReflectionException extends BasicException {

    /** serialVersionUID */
    private static final long serialVersionUID = 7642570221267566591L;

    /**
     * Reflection exception
     */
    public ReflectionException() {
        super();
    }

    /**
     * Reflection exception
     * @param message message
     */
    public ReflectionException(String message) {
        super(message);
    }

    /**
     * Reflection exception
     * @param msg  msg
     * @param args args
     */
    public ReflectionException(String msg, Object... args) {
        super(BasicException.DEFAULT_ERROR_CODE, msg, args);
    }

    /**
     * Reflection exception
     * @param message message
     * @param cause   cause
     */
    public ReflectionException(String message, Throwable cause) {
        super(cause, message);
    }

    /**
     * Reflection exception
     * @param cause cause
     */
    public ReflectionException(Throwable cause) {
        super(cause);
    }

}
