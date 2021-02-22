package com.fkhwl.starter.cache.autoconfigure;

import com.alicp.jetcache.autoconfigure.RedisLettuceAutoConfiguration;
import com.fkhwl.starter.cache.KeyExpirationListenerAdapter;
import com.fkhwl.starter.common.start.FkhAutoConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.5.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.07.09 10:46
 * @since 1.5.0
 */
@Configuration
@Import(LettuceConnectionConfiguration.class)
@Conditional(RedisLettuceAutoConfiguration.RedisLettuceCondition.class)
public class RedisJeyExpirationAutoConfiguration implements FkhAutoConfiguration {

    /**
     * 处理 RedisKeyExpiredEvent 事件
     *
     * @return the key expiration listener
     * @since 1.5.0
     */
    @Bean
    public KeyExpirationListenerAdapter keyExpirationListener() {
        return new KeyExpirationListenerAdapter();
    }

    /**
     * 开启监听器用于发送 RedisKeyExpiredEvent 事件
     *
     * @param container container
     * @return the key expiration event message listener
     * @since 1.5.0
     */
    @Bean
    public KeyExpirationEventMessageListener keyExpirationEventMessageListener(RedisMessageListenerContainer container) {
        return new KeyExpirationEventMessageListener(container);
    }

    /**
     * Container
     *
     * @param connectionFactory connection factory
     * @return the redis message listener container
     * @since 1.0.0
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
