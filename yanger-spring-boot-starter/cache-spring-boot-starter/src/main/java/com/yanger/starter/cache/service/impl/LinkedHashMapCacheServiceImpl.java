package com.yanger.starter.cache.service.impl;

import com.alicp.jetcache.Cache;
import com.yanger.starter.cache.service.AbstractCacheService;

/**
 * LinkedHashMap 缓存实现
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class LinkedHashMapCacheServiceImpl extends AbstractCacheService {

    /**
     * Abstract cache service
     */
    public LinkedHashMapCacheServiceImpl(Cache<String, Object> cache) {
        super(cache);
    }

}
