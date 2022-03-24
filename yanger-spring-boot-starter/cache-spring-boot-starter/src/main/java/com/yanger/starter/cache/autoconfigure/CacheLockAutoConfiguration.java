package com.yanger.starter.cache.autoconfigure;

import com.alicp.jetcache.autoconfigure.RedisLettuceAutoConfiguration;
import com.yanger.starter.basic.config.BaseAutoConfiguration;
import com.yanger.starter.cache.aop.CacheLockAspect;
import com.yanger.starter.cache.property.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * 基于 Redis 的分布式锁自动配置类
 * 注意: 只有配置了 redis 相关配置才会开启
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Slf4j
@Configuration
@Import(LettuceConnectionConfiguration.class)
@EnableConfigurationProperties(CacheProperties.class)
@Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
public class CacheLockAutoConfiguration implements BaseAutoConfiguration {

    /**
     * Redis lock registry redis lock registry
     */
    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "locks");
    }

    /**
     * Cache lock aop cache lock aop
     */
    @Bean
    public CacheLockAspect cacheLockAspect() {
        log.debug("已配置分布式缓存, 初始化 CacheLockAspect");
        return new CacheLockAspect();
    }

}
