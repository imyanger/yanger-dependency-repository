package com.yanger.starter.cache.exception;

import com.yanger.tools.general.format.StringFormat;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.support.ResultCode;

/**
 * 缓存支持异常
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class CacheNotSupportException extends BasicException {

    private static final long serialVersionUID = 4966471104899963847L;

    /**
     * Cache lock exception
     */
    public CacheNotSupportException(String msg) {
        super(msg);
        this.code = String.valueOf(ResultCode.REDIS_ERROR_PERCH.getCode());
        this.message = StringFormat.format(ResultCode.REDIS_ERROR_PERCH.getMessage(), msg);
    }

}
