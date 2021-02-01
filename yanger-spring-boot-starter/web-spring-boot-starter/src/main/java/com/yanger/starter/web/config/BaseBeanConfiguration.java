package com.yanger.starter.web.config;

import com.yanger.starter.web.api.IndexApi;
import com.yanger.starter.web.wx.agent.WxAgent;

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
    public IndexApi indexApi() {
        return new IndexApi();
    }

    @Bean
    public WxAgent wxAgent() {
        return new WxAgent();
    }

    @Bean
    public TokenConfig tokenConfig() {
        return new TokenConfig();
    }

}
