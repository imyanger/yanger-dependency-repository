package com.yanger.starter.cache.service.impl;

import com.alicp.jetcache.Cache;
import com.yanger.starter.cache.service.AbstractCacheService;

/**
 * Caffeine 缓存实现
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class CaffeineCacheServiceImpl extends AbstractCacheService {

    /**
     * Abstract cache service
     * @param cache cache
     */
    public CaffeineCacheServiceImpl(Cache<String, Object> cache) {
        super(cache);
    }

}
