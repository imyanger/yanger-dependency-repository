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
public class CacheNotSupportException extends BaseException {

    private static final long serialVersionUID = 4966471104899963847L;

    /**
     * Cache lock exception
     *
     * @param msg msg
     * @since 1.6.0
     */
    public CacheNotSupportException(String msg) {
        super(msg);
        this.resultCode = BaseCodes.SERVER_INNER_ERROR;
    }
}
