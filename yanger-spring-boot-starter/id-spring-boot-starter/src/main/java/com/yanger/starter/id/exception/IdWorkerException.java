package com.yanger.starter.id.exception;

import com.yanger.tools.web.exception.BasicException;

/**
 * @Description id 生成异常
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class IdWorkerException extends BasicException {

    private static final long serialVersionUID = -4445192684555401929L;

    public IdWorkerException(String msg) {
        super(msg);
    }

}
