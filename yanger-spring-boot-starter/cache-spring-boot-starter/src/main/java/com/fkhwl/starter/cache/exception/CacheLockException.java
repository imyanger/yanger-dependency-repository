package com.fkhwl.starter.cache.exception;

import com.fkhwl.starter.core.api.BaseCodes;
import com.fkhwl.starter.core.exception.BaseException;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version x.x.x
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.10.28 16:28
 * @since x.x.x
 */
public class CacheLockException extends BaseException {

    private static final long serialVersionUID = 2157282953672508633L;

    /**
     * Cache lock exception
     *
     * @param msg msg
     * @since 1.6.0
     */
    public CacheLockException(String msg) {
        super(msg);
        this.resultCode = BaseCodes.SERVER_INNER_ERROR;
    }
}
