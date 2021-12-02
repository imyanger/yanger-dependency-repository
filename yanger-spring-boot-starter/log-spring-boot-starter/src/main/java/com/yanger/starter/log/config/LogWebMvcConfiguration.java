package com.yanger.starter.log.config;

import com.yanger.starter.log.interceptor.LogInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

/**
 * web配置类
 * @Author yanger
 * @Date 2020/7/17 18:13
 */
@Slf4j
@Configuration
public class LogWebMvcConfiguration implements WebMvcConfigurer {

    /** 时间打印拦截器 */
    @Autowired(required = false)
    private LogInterceptor logInterceptor;

    /**
     * 注册拦截器
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (logInterceptor != null) {
            registry.addInterceptor(this.logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*.css", "/**/*.js")
                .excludePathPatterns("/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/api-docs", "/v3/api-docs", "/favicon.ico")
                .excludePathPatterns("/swagger-ui/index.html", "/doc.html");
        }
    }

}
