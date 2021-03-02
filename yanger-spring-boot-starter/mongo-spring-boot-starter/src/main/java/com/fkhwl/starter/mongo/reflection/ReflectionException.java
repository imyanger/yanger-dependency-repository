package com.fkhwl.starter.mongo.reflection;

import com.yanger.tools.web.exception.BasicException;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 11:54
 * @since 0.0
 */

/**
 * @Description
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
     *
     * @param message message
     */
    public ReflectionException(String message) {
        super(message);
    }

    /**
     * Reflection exception
     *
     * @param msg  msg
     * @param args args
     */
    public ReflectionException(String msg, Object... args) {
        super(msg, args);
    }

    /**
     * Reflection exception
     *
     * @param message message
     * @param cause   cause
     */
    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Reflection exception
     *
     * @param cause cause
     */
    public ReflectionException(Throwable cause) {
        super(cause);
    }

}
