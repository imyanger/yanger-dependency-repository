package com.yanger.starter.cache.exception;

import com.yanger.tools.general.format.StringFormatter;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.support.ResultCode;

/**
 * @Description 分布式锁异常
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class CacheLockException extends BasicException {

    private static final long serialVersionUID = 2157282953672508633L;

    /**
     * Cache lock exception
     */
    public CacheLockException(String msg) {
        super(msg);
        this.code = String.valueOf(ResultCode.REDIS_ERROR_PERCH.getCode());
        this.message = StringFormatter.format(ResultCode.REDIS_ERROR_PERCH.getMessage(), msg);
    }

}
