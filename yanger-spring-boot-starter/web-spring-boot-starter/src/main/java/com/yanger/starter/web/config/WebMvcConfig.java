package com.yanger.starter.web.config;

import com.yanger.starter.web.handler.LoginInterceptor;
import com.yanger.starter.web.handler.LoginUserResolver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import javax.annotation.Resource;

/**
 * @Description web配置类
 * @Author yanger
 * @Date 2020/7/17 18:13
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /** 登录拦截器 */
    @Resource
    private LoginInterceptor loginInterceptor;

    /** 用户信息解析器 */
    @Resource
    private LoginUserResolver loginUserResolver;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 注册拦截器
     *
     * @param registry registry
     * @since 1.0.0
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.loginInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/**/*.css", "/**/*.js")
            .excludePathPatterns("/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg")
            .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html", "/favicon.ico")
            .excludePathPatterns("/login", "/wxLogin", "/wxAppLogin", "/register");
    }

    /**
     * 注册解析器
     *
     * @param resolvers resolvers
     * @since 1.0.0
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserResolver);
    }

}
