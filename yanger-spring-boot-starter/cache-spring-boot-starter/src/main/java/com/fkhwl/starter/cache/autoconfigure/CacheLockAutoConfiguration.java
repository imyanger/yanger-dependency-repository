package com.fkhwl.starter.cache.autoconfigure;

import com.alicp.jetcache.autoconfigure.RedisLettuceAutoConfiguration;
import com.fkhwl.starter.cache.aop.CacheLockAspect;
import com.fkhwl.starter.common.start.FkhAutoConfiguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 基于 Redis 的分布式锁自动配置类 </p>
 * 注意: 只有配置了 redis 相关配置才会开启
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.19 00:27
 * @since 1.0.0
 */
@Slf4j
@Configuration
@Import(LettuceConnectionConfiguration.class)
@EnableConfigurationProperties(CacheProperties.class)
@Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
public class CacheLockAutoConfiguration implements FkhAutoConfiguration {

    /**
     * Redis lock registry redis lock registry
     *
     * @param redisConnectionFactory redis connection factory
     * @return the redis lock registry
     * @since 1.0.0
     */
    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "locks");
    }

    /**
     * Cache lock aop cache lock aop
     *
     * @return the cache lock aop
     * @since 1.0.0
     */
    @Bean
    public CacheLockAspect cacheLockAspect() {
        log.debug("已配置分布式缓存, 初始化 CacheLockAspect");
        return new CacheLockAspect();
    }
}
