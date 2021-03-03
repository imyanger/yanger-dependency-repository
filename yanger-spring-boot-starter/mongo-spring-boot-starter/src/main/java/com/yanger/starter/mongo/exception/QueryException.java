package com.yanger.starter.mongo.exception;

import com.yanger.tools.web.exception.BasicException;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class QueryException extends BasicException {
    /** serialVersionUID */
    private static final long serialVersionUID = 4842002397836842293L;

    /**
     * Instantiates a new Query exception.
     */
    public QueryException() {
        super();
    }

    /**
     * Instantiates a new Query exception.
     *
     * @param message the message
     */
    public QueryException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Query exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Query exception.
     *
     * @param cause the cause
     */
    public QueryException(Throwable cause) {
        super(cause);
    }

}
