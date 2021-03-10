package com.yanger.starter.log.config;

import com.yanger.starter.log.aspect.BusinessLogAspect;
import com.yanger.starter.log.dynamic.LevelApi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 装在基础 Bean 对象
 * @Author yanger
 * @Date 2021/2/1 17:38
 */
@Configuration
public class BaseBeanConfiguration {

    @Bean
    public LogbackProperties logbackProperties() {
        return new LogbackProperties();
    }

    @Bean
    public LevelApi levelApi(){
        return new LevelApi();
    }

    @Bean
    public BusinessLogAspect businessLogAspect(){
        return new BusinessLogAspect();
    }

}
