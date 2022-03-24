package com.yanger.starter.mongo.exception;

import com.yanger.tools.web.exception.BasicException;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class MongoException extends BasicException {

    /** serialVersionUID */
    private static final long serialVersionUID = 4842002397836842293L;

    /**
     * Mongo exception
     */
    public MongoException() {
        super();
    }

    /**
     * Mongo exception
     * @param message the message
     */
    public MongoException(String message) {
        super(message);
    }

    /**
     * Mongo exception
     * @param message the message
     * @param cause   the cause
     */
    public MongoException(String message, Throwable cause) {
        super(cause, message);
    }

    /**
     * msg 占位符替换
     * @param msg  msg
     * @param args args
     */
    public MongoException(String msg, Object... args) {
        super(BasicException.DEFAULT_ERROR_CODE, msg, args);
    }

    /**
     * Mongo exception
     * @param cause the cause
     */
    public MongoException(Throwable cause) {
        super(cause);
    }

}
