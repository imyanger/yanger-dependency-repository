package com.yanger.starter.cache.autoconfigure;

import com.yanger.starter.cache.property.CacheProperties;
import io.lettuce.core.RedisURI;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.connection.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis 连接 配置
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Slf4j
abstract class RedisConnectionConfiguration {

    /** Cache properties */
    private final CacheProperties.Area cacheProperties;

    /**
     * Redis connection configuration
     * @param cacheProperties cache properties
     */
    @Contract(pure = true)
    RedisConnectionConfiguration(CacheProperties.Area cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    /**
     * 单机配置
     * @param redisURI redis uri
     * @return the standalone config
     */
    @SuppressWarnings("PMD.LowerCamelCaseVariableNamingRule")
    final @NotNull RedisStandaloneConfiguration getStandaloneConfig(@NotNull RedisURI redisURI) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisURI.getHost());
        config.setPort(redisURI.getPort());
        config.setPassword(RedisPassword.of(redisURI.getPassword()));
        config.setDatabase(redisURI.getDatabase());
        return config;
    }

    /**
     * 哨兵配置
     * @param redisURI redis uri
     * @return the sentinel config
     */
    @Contract(pure = true)
    @SuppressWarnings("PMD.LowerCamelCaseVariableNamingRule")
    final @NotNull RedisSentinelConfiguration getSentinelConfig(@NotNull RedisURI redisURI) {
        RedisSentinelConfiguration config = new RedisSentinelConfiguration();
        config.master(redisURI.getSentinelMasterId());
        List<RedisNode> redisNodes =
            redisURI.getSentinels().stream().map((n) -> new RedisNode(n.getHost(), n.getPort())).collect(Collectors.toList());
        config.setSentinels(redisNodes);
        config.setPassword(RedisPassword.of(redisURI.getPassword()));
        config.setDatabase(redisURI.getDatabase());
        return config;
    }

    /**
     * 集群配置
     * @param uriList uri list
     * @return {@literal null} if no cluster settings are set.
     */
    @Contract(pure = true)
    final @NotNull RedisClusterConfiguration getClusterConfiguration(@NotNull List<RedisURI> uriList) {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        List<RedisNode> redisNodes = uriList.stream().map((n) -> new RedisNode(n.getHost(), n.getPort())).collect(Collectors.toList());
        config.setClusterNodes(redisNodes);
        config.setMaxRedirects(this.cacheProperties.getMaxRedirects());
        // 集群统一使用一个 password
        config.setPassword(RedisPassword.of(this.cacheProperties.getPassword()));
        return config;
    }

}
