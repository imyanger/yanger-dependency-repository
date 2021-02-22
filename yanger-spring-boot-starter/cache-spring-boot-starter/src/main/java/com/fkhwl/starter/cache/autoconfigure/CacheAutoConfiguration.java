package com.fkhwl.starter.cache.autoconfigure;

import com.alicp.jetcache.AbstractCacheBuilder;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheBuilder;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.autoconfigure.AutoConfigureBeans;
import com.alicp.jetcache.autoconfigure.CaffeineAutoConfiguration;
import com.alicp.jetcache.autoconfigure.JetCacheAutoConfiguration;
import com.alicp.jetcache.autoconfigure.LinkedHashMapAutoConfiguration;
import com.alicp.jetcache.autoconfigure.RedisLettuceAutoConfiguration;
import com.alicp.jetcache.external.ExternalCacheBuilder;
import com.alicp.jetcache.support.StatInfo;
import com.alicp.jetcache.support.StatInfoLogger;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fkhwl.starter.basic.util.JsonUtils;
import com.fkhwl.starter.cache.KeyExpirationListenerAdapter;
import com.fkhwl.starter.cache.service.CacheService;
import com.fkhwl.starter.cache.service.impl.CaffeineCacheServiceImpl;
import com.fkhwl.starter.cache.service.impl.LinkedHashMapCacheServiceImpl;
import com.fkhwl.starter.cache.service.impl.RedisCacheServiceImpl;
import com.fkhwl.starter.cache.support.FastjsonKeyConvertor;
import com.fkhwl.starter.cache.support.JacksonValueDecoder;
import com.fkhwl.starter.cache.support.JacksonValueEncoder;
import com.fkhwl.starter.cache.support.JacksonValueSerialPolicy;
import com.fkhwl.starter.cache.support.ProtobufValueDecoder;
import com.fkhwl.starter.cache.support.ProtobufValueEncoder;
import com.fkhwl.starter.cache.support.ProtobufValueSerialPolicy;
import com.fkhwl.starter.common.start.FkhAutoConfiguration;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.function.Consumer;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.11 10:09
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableCreateCacheAnnotation
@Import(value = {
    LettuceConnectionConfiguration.class,
    RedisJeyExpirationAutoConfiguration.class
})
@EnableConfigurationProperties(CacheProperties.class)
@AutoConfigureAfter(JetCacheAutoConfiguration.class)
public class CacheAutoConfiguration implements FkhAutoConfiguration {

    /**
     * 自定义序列化/反序列化
     *
     * @param jacksonValueDecoder jackson value decoder
     * @param jacksonValueEncoder jackson value encoder
     * @return the spring config provider
     * @since 1.0.0
     */
    @Bean
    public SpringConfigProvider springConfigProvider(JacksonValueDecoder jacksonValueDecoder,
                                                     JacksonValueEncoder jacksonValueEncoder) {
        return new SpringConfigProvider() {
            @Override
            public Function<Object, byte[]> parseValueEncoder(String valueEncoder) {
                if (valueEncoder.equalsIgnoreCase(JacksonValueSerialPolicy.JACKSON)) {
                    return jacksonValueEncoder;
                } else {
                    return super.parseValueEncoder(valueEncoder);
                }
            }

            @Override
            public Function<byte[], Object> parseValueDecoder(String valueDecoder) {
                if (valueDecoder.equalsIgnoreCase(JacksonValueSerialPolicy.JACKSON)) {
                    return jacksonValueDecoder;
                } else {
                    return super.parseValueDecoder(valueDecoder);
                }
            }

            @Override
            public Consumer<StatInfo> statCallback() {
                // 实现自己的logger
                return new StatInfoLogger(false);
            }
        };
    }

    /**
     * Redis template redis template
     *
     * @param factory                  factory
     * @param jacksonValueSerialPolicy jackson value serial policy
     * @return the redis template
     * @since 1.0.0
     */
    @Bean
    @Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory,
                                                       JacksonValueSerialPolicy jacksonValueSerialPolicy) {
        log.debug("已配置分布式缓存, 初始化 RedisTemplate");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        ObjectMapper objectMapper = JsonUtils.getCopyMapper();
        // 设置 Jackson 序列化时只包含不为空的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 序列化时加上 class
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        // key采用String的序列化方式
        template.setKeySerializer(FastjsonKeyConvertor.INSTANCE);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // value序列化方式采用jackson
        template.setValueSerializer(jacksonValueSerialPolicy);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jacksonValueSerialPolicy);
        template.setDefaultSerializer(jacksonValueSerialPolicy);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis CacheService 实现, 如果配置为多级缓存, 优先使用 RedisCacheService 作为 CacheService 的实现
     *
     * @param redisTemplate                redis template
     * @param keyExpirationListenerAdapter key expiration listener adapter
     * @param redisClient                  redis client
     * @param jacksonValueDecoder          jackson value decoder
     * @param jacksonValueEncoder          jackson value encoder
     * @return the redis service
     * @since 1.0.0
     */
    @Bean
    @Primary
    @SuppressWarnings("all")
    @Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
    public CacheService redisCacheServiceImpl(@NotNull RedisTemplate<String, Object> redisTemplate,
                                              KeyExpirationListenerAdapter keyExpirationListenerAdapter,
                                              @NotNull AutoConfigureBeans autoConfigureBeans) {
        log.debug("已配置分布式缓存, 初始化 {}", RedisCacheServiceImpl.class);

        CacheBuilder cacheBuilder = autoConfigureBeans.getRemoteCacheBuilders().get("default");
        ExternalCacheBuilder acb = (ExternalCacheBuilder) cacheBuilder;
        // 重新设置为自定义 FastjsonKeyConvertor
        acb.setKeyConvertor(FastjsonKeyConvertor.INSTANCE);
        acb.setKeyPrefix("");
        return new RedisCacheServiceImpl(cacheBuilder.buildCache(), redisTemplate, keyExpirationListenerAdapter);

    }

    /**
     * 使用全局配置生成一个默认的本地 cache 实现包装实例
     *
     * @param cacheProperties    cache properties
     * @param autoConfigureBeans auto configure beans
     * @return the cache service
     * @since 1.6.0
     */
    @Bean
    @SuppressWarnings("all")
    @Conditional(CaffeineAutoConfiguration.CaffeineCondition.class)
    public CacheService caffeineCacheServiceImpl(@NotNull AutoConfigureBeans autoConfigureBeans) {
        log.debug("未配置分布式缓存, 使用本地缓存实现: {}", CaffeineCacheServiceImpl.class);

        Cache<String, Object> cache = buildCache(autoConfigureBeans);
        return new CaffeineCacheServiceImpl(cache);
    }

    /**
     * 使用全局配置生成一个默认的本地 cache 实现包装实例
     *
     * @return the cache service
     * @since 1.6.0
     */
    @Bean
    @SuppressWarnings("all")
    @Conditional(LinkedHashMapAutoConfiguration.LinkedHashMapCondition.class)
    public CacheService linkedHashMapCacheServiceImpl(@NotNull AutoConfigureBeans autoConfigureBeans) {
        log.debug("未配置分布式缓存, 使用本地缓存实现: {}", LinkedHashMapCacheServiceImpl.class);

        Cache<String, Object> cache = buildCache(autoConfigureBeans);
        return new LinkedHashMapCacheServiceImpl(cache);
    }

    /**
     * Build cache
     *
     * @param autoConfigureBeans auto configure beans
     * @return the cache
     * @since 1.6.0
     */
    @SuppressWarnings("all")
    private Cache<String, Object> buildCache(@NotNull AutoConfigureBeans autoConfigureBeans) {
        CacheBuilder cacheBuilder = autoConfigureBeans.getLocalCacheBuilders().get("default");
        AbstractCacheBuilder acb = (AbstractCacheBuilder) cacheBuilder;
        // 重新设置为自定义 FastjsonKeyConvertor
        acb.setKeyConvertor(FastjsonKeyConvertor.INSTANCE);
        return cacheBuilder.buildCache();
    }

    /**
     * <p>Company: 成都返空汇网络技术有限公司 </p>
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.3.0
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.04.19 00:35
     * @since 1.0.0
     */
    @Configuration
    static class JacksonSerializeAutoConfiguration {
        /**
         * Jackson value decoder jackson value decoder
         *
         * @return the jackson value decoder
         * @since 1.0.0
         */
        @Bean
        public JacksonValueDecoder jacksonValueDecoder() {
            return new JacksonValueDecoder();
        }

        /**
         * Jackson value encoder jackson value encoder
         *
         * @return the jackson value encoder
         * @since 1.0.0
         */
        @Bean
        public JacksonValueEncoder jacksonValueEncoder() {
            return new JacksonValueEncoder();
        }

        /**
         * Jackson value serial policy jackson value serial policy
         *
         * @param jacksonValueDecoder jackson value decoder
         * @param jacksonValueEncoder jackson value encoder
         * @return the jackson value serial policy
         * @since 1.0.0
         */
        @Bean
        public JacksonValueSerialPolicy jacksonValueSerialPolicy(JacksonValueDecoder jacksonValueDecoder,
                                                                 JacksonValueEncoder jacksonValueEncoder) {
            return new JacksonValueSerialPolicy(jacksonValueDecoder, jacksonValueEncoder);
        }
    }

    /**
     * <p>Company: 成都返空汇网络技术有限公司 </p>
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.3.0
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.04.19 00:36
     * @since 1.0.0
     */
    @Configuration
    static class ProtobufSerializeAutoConfiguration {
        /**
         * Protobuf value decoder protobuf value decoder
         *
         * @return the protobuf value decoder
         * @since 1.0.0
         */
        @Bean
        public ProtobufValueDecoder protobufValueDecoder() {
            return new ProtobufValueDecoder();
        }

        /**
         * Jackson value encoder jackson value encoder
         *
         * @return the jackson value encoder
         * @since 1.0.0
         */
        @Bean
        public ProtobufValueEncoder protobufValueEncoder() {
            return new ProtobufValueEncoder();
        }


        /**
         * Protobuf value serial policy protobuf value serial policy
         *
         * @param protobufValueDecoder protobuf value decoder
         * @param protobufValueEncoder protobuf value encoder
         * @return the protobuf value serial policy
         * @since 1.0.0
         */
        @Bean
        public ProtobufValueSerialPolicy protobufValueSerialPolicy(ProtobufValueDecoder protobufValueDecoder,
                                                                   ProtobufValueEncoder protobufValueEncoder) {
            return new ProtobufValueSerialPolicy(protobufValueDecoder, protobufValueEncoder);
        }
    }

}
