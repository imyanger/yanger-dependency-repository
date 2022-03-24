package com.yanger.starter.log.config;

import com.yanger.starter.basic.config.BaseAutoConfiguration;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.log.dynamic.LevelApi;
import com.yanger.starter.log.interceptor.LogInterceptor;
import com.yanger.starter.log.property.LogbackProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 装在基础 Bean 对象
 * @Author yanger
 * @Date 2021/2/1 17:38
 */
@Configuration
public class DefaultLogBeanConfiguration implements BaseAutoConfiguration {

    @Bean
    public LogbackProperties logbackProperties() {
        return new LogbackProperties();
    }

    @Bean
    public LevelApi levelApi(){
        return new LevelApi();
    }

    @Bean
    @ConditionalOnProperty(value = ConfigKey.LogConfigKey.API_LOG_ENABLED, havingValue = "true", matchIfMissing = false)
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

}
