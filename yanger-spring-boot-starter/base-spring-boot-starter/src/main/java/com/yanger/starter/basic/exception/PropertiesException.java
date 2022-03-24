package com.yanger.starter.basic.exception;

import com.yanger.tools.web.exception.BasicException;

/**
 * 配置 异常
 * @Author yanger
 * @Date 2020/12/29 18:37
 */
public class PropertiesException extends BasicException {

    private static final long serialVersionUID = -6498727260647427447L;

    public PropertiesException(String msg, Object... args) {
        super(BasicException.DEFAULT_ERROR_CODE, msg, args);
    }

    public PropertiesException(Throwable cause) {
        super(cause);
    }

}
