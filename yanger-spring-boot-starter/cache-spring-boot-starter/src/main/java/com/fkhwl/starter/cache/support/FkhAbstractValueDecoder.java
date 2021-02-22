package com.fkhwl.starter.cache.support;

import com.alicp.jetcache.support.AbstractValueDecoder;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.12 00:20
 * @since 1.0.0
 */
@Slf4j
abstract class FkhAbstractValueDecoder extends AbstractValueDecoder {

    /**
     * Fkh abstract value decoder
     *
     * @param useIdentityNumber use identity number
     * @since 1.0.0
     */
    FkhAbstractValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

}
