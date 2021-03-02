package com.fkhwl.starter.mongo.exception;

import com.yanger.tools.web.exception.BasicException;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.04 10:09
 * @since 1.0.0
 */

/**
 * @Description
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
     *
     * @param message the message
     */
    public MongoException(String message) {
        super(message);
    }

    /**
     * Mongo exception
     *
     * @param message the message
     * @param cause   the cause
     */
    public MongoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * msg 占位符替换
     *
     * @param msg  msg
     * @param args args
     */
    public MongoException(String msg, Object... args) {
        super(msg, args);
    }

    /**
     * Mongo exception
     *
     * @param cause the cause
     */
    public MongoException(Throwable cause) {
        super(cause);
    }

}
