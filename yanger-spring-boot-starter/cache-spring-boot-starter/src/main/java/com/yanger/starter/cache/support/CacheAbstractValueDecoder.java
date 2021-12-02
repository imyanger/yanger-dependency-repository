package com.yanger.starter.cache.support;

import com.alicp.jetcache.support.AbstractValueDecoder;

import lombok.extern.slf4j.Slf4j;

/**
 * 反序列化器
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Slf4j
abstract class CacheAbstractValueDecoder extends AbstractValueDecoder {

    CacheAbstractValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

}
