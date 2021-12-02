package com.yanger.starter.test.controller;

import com.yanger.starter.cache.annotation.CacheLock;
import com.yanger.starter.cache.service.CacheService;
import com.yanger.starter.web.annotation.IgnoreLoginAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * id生成接口
 * @Author yanger
 * @Date 2021-01-08 16:58:04
 */
@Slf4j
@Api(tags={"cache接口"})
@RestController
@RequestMapping("cache")
@IgnoreLoginAuth
public class CacheController {

    @Autowired
    private CacheService cacheService;

    /**
     * 获取id
     * @Author yanger
     * @date 2021-01-08 16:58:04
     */
    @ApiOperation(value="打印日志", tags={"cache接口"}, notes="打印日志")
    @GetMapping("set")
    @CacheLock(prefix = "cache")
    public Object set(String key, String val, Integer sl) throws InterruptedException {
        cacheService.set(key, val);
        Thread.sleep(sl * 1000);
        return cacheService.get(key);
    }

}