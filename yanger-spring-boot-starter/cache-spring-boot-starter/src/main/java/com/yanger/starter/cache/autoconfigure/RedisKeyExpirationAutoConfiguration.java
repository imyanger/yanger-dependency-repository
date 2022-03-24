package com.yanger.starter.cache.autoconfigure;

import com.alicp.jetcache.autoconfigure.RedisLettuceAutoConfiguration;
import com.yanger.starter.basic.config.BaseAutoConfiguration;
import com.yanger.starter.cache.handler.KeyExpirationListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * redis key 过期装配
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Configuration
@Import(LettuceConnectionConfiguration.class)
@Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
public class RedisKeyExpirationAutoConfiguration implements BaseAutoConfiguration {

    /**
     * 处理 RedisKeyExpiredEvent 事件
     * @return the key expiration listener
     */
    @Bean
    public KeyExpirationListenerAdapter keyExpirationListener() {
        return new KeyExpirationListenerAdapter();
    }

    /**
     * 开启监听器用于发送 RedisKeyExpiredEvent 事件
     * @param container container
     * @return the key expiration event message listener
     */
    @Bean
    public KeyExpirationEventMessageListener keyExpirationEventMessageListener(RedisMessageListenerContainer container) {
        return new KeyExpirationEventMessageListener(container);
    }

    /**
     * Container
     * @param connectionFactory connection factory
     * @return the redis message listener container
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

}
