package com.yanger.starter.cache.autoconfigure;

import com.alicp.jetcache.autoconfigure.LettuceFactory;
import com.alicp.jetcache.autoconfigure.RedisLettuceAutoConfiguration;
import com.yanger.starter.basic.boost.YangerAutoConfiguration;
import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.tools.general.format.StringFormatter;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration.LettuceClientConfigurationBuilder;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.net.*;
import java.util.List;
import java.util.stream.Collectors;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;

/**
 * @Description Lettuce 装配
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
@Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
public class LettuceConnectionConfiguration extends RedisConnectionConfiguration implements YangerAutoConfiguration {

    /** 使用远程默认的 area 来生成 template */
    private final CacheProperties.Area defaultArea;

    /** Builder customizers */
    private final ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers;

    /**
     * Lettuce connection configuration
     *
     * @param cacheProperties    cache properties
     * @param builderCustomizers builder customizers
     */
    LettuceConnectionConfiguration(@NotNull CacheProperties cacheProperties,
                                   ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers) {
        super(cacheProperties.getRemote().get("default"));
        this.defaultArea = cacheProperties.getRemote().get("default");
        this.builderCustomizers = builderCustomizers;
    }


    /**
     * Default client lettuce factory
     */
    @Bean(name = "defaultClient")
    @DependsOn(RedisLettuceAutoConfiguration.AUTO_INIT_BEAN_NAME)
    public LettuceFactory defaultClient() {
        return new LettuceFactory("remote.default", RedisClient.class);
    }

    /**
     * 此 bean 可不用配置, 此处为了防止 idea 找不到 bean 这里手动注入 IoC (Spring 可自动通过 LettuceFactory 注入)
     *
     * @param lettuceFactory lettuce factory
     * @return the redis client
     * @throws Exception exception
     */
    @Bean
    public RedisClient redisClient(@NotNull LettuceFactory lettuceFactory) throws Exception {
        return (RedisClient) lettuceFactory.getObject();
    }

    /**
     * Lettuce client resources client resources
     *
     * @param redisClient redis client
     * @return the client resources
     * @see LettuceConnectionConfiguration#defaultClient() LettuceConnectionConfiguration#defaultClient()
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(ClientResources.class)
    public ClientResources lettuceClientResources(@NotNull RedisClient redisClient) {
        return redisClient.getResources();
    }

    /**
     * Redis connection factory lettuce connection factory
     *
     * @param clientResources client resources
     * @return the lettuce connection factory
     * @throws UnknownHostException unknown host exception
     */
    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public LettuceConnectionFactory redisConnectionFactory(ClientResources clientResources) {
        LettuceClientConfiguration clientConfig = this.getLettuceClientConfiguration(clientResources, this.defaultArea.getPoolConfig());
        return this.createLettuceConnectionFactory(clientConfig, this.defaultArea.getUri());
    }

    /**
     * Create lettuce connection factory lettuce connection factory
     *
     * @param clientConfiguration client configuration
     * @param uris                uris
     * @return the lettuce connection factory
     */
    @Contract("_, _ -> new")
    @NotNull
    private LettuceConnectionFactory createLettuceConnectionFactory(LettuceClientConfiguration clientConfiguration, @NotNull List<String> uris) {

        List<RedisURI> uriList = uris.stream().map((u) -> RedisURI.create(URI.create(u))).collect(Collectors.toList());

        // 单机
        if (uriList.size() == 1 && uriList.get(0).getSentinels().isEmpty()) {
            return new LettuceConnectionFactory(this.getStandaloneConfig(uriList.get(0)), clientConfiguration);
        } else if (uriList.size() == 1 && !uriList.get(0).getSentinels().isEmpty()) {
            // 哨兵 (redis-sentinel://[password@]127.0.0.1:26379,127.0.0.1:26380,127.0.0.1:26381[/db]?sentinelMasterId=mymaster)
            return new LettuceConnectionFactory(this.getSentinelConfig(uriList.get(0)), clientConfiguration);
        } else if (uriList.size() > 1) {
            // 集群
            return new LettuceConnectionFactory(this.getClusterConfiguration(uriList), clientConfiguration);
        } else {
            throw new IllegalStateException(StringFormatter.format("url 配置错误: {}", JsonUtils.toJson(uriList)));
        }

    }

    /**
     * Gets lettuce client configuration *
     *
     * @param clientResources client resources
     * @param pool            pool
     * @return the lettuce client configuration
     */
    @NotNull
    private LettuceClientConfiguration getLettuceClientConfiguration(ClientResources clientResources, CacheProperties.Pool pool) {
        LettuceClientConfigurationBuilder builder = this.createBuilder(pool);
        builder.clientResources(clientResources);
        this.customize(builder);
        return builder.build();
    }

    /**
     * Create builder lettuce client configuration builder
     *
     * @param pool pool
     * @return the lettuce client configuration builder
     */
    @Contract("null -> !null")
    private LettuceClientConfigurationBuilder createBuilder(CacheProperties.Pool pool) {
        if (pool == null) {
            return LettuceClientConfiguration.builder();
        }
        return new PoolBuilderFactory().createBuilder(pool);
    }

    /**
     * Customize *
     *
     * @param builder builder
     */
    private void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
        this.builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
    }

    /**
     * Inner class to allow optional commons-pool2 dependency.
     */
    private static class PoolBuilderFactory {

        /**
         * Create builder lettuce client configuration builder
         *
         * @param properties properties
         * @return the lettuce client configuration builder
         */
        LettuceClientConfigurationBuilder createBuilder(CacheProperties.Pool properties) {
            return LettucePoolingClientConfiguration.builder().poolConfig(this.getPoolConfig(properties));
        }

        /**
         * Gets pool config *
         *
         * @param properties properties
         * @return the pool config
         */
        private GenericObjectPoolConfig<?> getPoolConfig(@NotNull CacheProperties.Pool properties) {
            GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
            config.setMaxTotal(properties.getMaxTotal());
            config.setMaxIdle(properties.getMaxIdle());
            config.setMinIdle(properties.getMinIdle());
            if (properties.getTimeBetweenEvictionRuns() != null) {
                config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
            }
            if (properties.getMaxWait() != null) {
                config.setMaxWaitMillis(properties.getMaxWait().toMillis());
            }
            return config;
        }

    }

}
