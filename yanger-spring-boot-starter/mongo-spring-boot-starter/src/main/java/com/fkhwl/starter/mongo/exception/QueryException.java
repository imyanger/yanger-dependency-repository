package com.fkhwl.starter.mongo.exception;

import com.yanger.tools.web.exception.BasicException;

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
