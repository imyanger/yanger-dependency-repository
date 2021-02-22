package com.fkhwl.starter.mongo.exception;

import com.fkhwl.starter.core.exception.BaseException;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: ${description}</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:43
 * @since 1.0.0
 */
public class QueryException extends BaseException {
    /** serialVersionUID */
    private static final long serialVersionUID = 4842002397836842293L;

    /**
     * Instantiates a new Query exception.
     *
     * @since 1.0.0
     */
    public QueryException() {
        super();
    }

    /**
     * Instantiates a new Query exception.
     *
     * @param message the message
     * @since 1.0.0
     */
    public QueryException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Query exception.
     *
     * @param message the message
     * @param cause   the cause
     * @since 1.0.0
     */
    public QueryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Query exception.
     *
     * @param cause the cause
     * @since 1.0.0
     */
    public QueryException(Throwable cause) {
        super(cause);
    }
}
