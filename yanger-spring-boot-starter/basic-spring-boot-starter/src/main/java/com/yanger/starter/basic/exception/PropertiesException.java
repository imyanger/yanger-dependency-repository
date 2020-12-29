package com.yanger.starter.basic.exception;

import com.yanger.tools.web.exception.BasicException;

/**
 * @Description 配置 异常
 * @Author yanger
 * @Date 2020/12/29 18:37
 */
public class PropertiesException extends BasicException {

    /** serialVersionUID */
    private static final long serialVersionUID = -6498727260647427447L;

    /**
     * Properties exception
     *
     * @param msg  msg
     * @param args args
     */
    public PropertiesException(String msg, Object... args) {
        super(msg, args);
    }

    /**
     * Properties exception
     *
     * @param cause cause
     */
    public PropertiesException(Throwable cause) {
        super(cause);
    }

}
