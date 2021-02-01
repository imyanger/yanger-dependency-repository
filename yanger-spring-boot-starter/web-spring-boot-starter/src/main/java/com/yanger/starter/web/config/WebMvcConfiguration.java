package com.yanger.starter.web.config;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.convert.EntityEnumDeserializer;
import com.yanger.starter.basic.convert.EntityEnumSerializer;
import com.yanger.starter.basic.convert.GlobalEnumConverterFactory;
import com.yanger.starter.basic.convert.StringToDateConverter;
import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.starter.web.handler.LoginInterceptor;
import com.yanger.starter.web.handler.LoginUserResolver;
import com.yanger.starter.web.jackson.MappingApiJackson2HttpMessageConverter;
import com.yanger.starter.web.support.FormdataBodyArgumentResolver;
import com.yanger.starter.web.support.RequestAbstractFormMethodArgumentResolver;
import com.yanger.starter.web.support.RequestSingleParamHandlerMethodArgumentResolver;
import com.yanger.starter.web.xss.XssFilter;
import com.yanger.tools.general.constant.Charsets;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.tools.StringTools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.ResourceRegionHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.Filter;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description web配置类
 * @Author yanger
 * @Date 2020/7/17 18:13
 */
@Slf4j
@Configuration
@AutoConfigureAfter(JacksonConfiguration.class)
@EnableConfigurationProperties(value = {ServerProperties.class, XssProperties.class})
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private Environment environment;

    @Resource
    private ObjectMapper objectMapper;

    /** 登录拦截器 */
    @Resource
    private LoginInterceptor loginInterceptor;

    /** 用户信息解析器 */
    @Resource
    private LoginUserResolver loginUserResolver;

    /** GLOBAL_ENUM_CONVERTER_FACTORY */
    private static final ConverterFactory<String, SerializeEnum<?>> GLOBAL_ENUM_CONVERTER_FACTORY = new GlobalEnumConverterFactory();

    /** Xss properties */
    private final XssProperties xssProperties;

    @Contract(pure = true)
    public WebMvcConfiguration(XssProperties xssProperties) {
        this.xssProperties = xssProperties;
    }

    /**
     * 资源映射配置
     */
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
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.loginInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/**/*.css", "/**/*.js")
            .excludePathPatterns("/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/fonts/*", "/**/*.svg")
            .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/api-docs", "/v3/api-docs")
            .excludePathPatterns("/swagger-ui/index.html", "/doc.html")
            .excludePathPatterns("/login", "/wxMiniLogin", "/wxAppLogin", "/randomCode", "/register");
    }

    /**
     * 注册解析器
     *
     * @param resolvers resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 解析用户信息对象
        resolvers.add(loginUserResolver);
        // 参数解析
        log.debug("注册 @RequestSingleParam 注解处理器: [{}]", RequestSingleParamHandlerMethodArgumentResolver.class);
        resolvers.add(new RequestSingleParamHandlerMethodArgumentResolver(this.objectMapper, GLOBAL_ENUM_CONVERTER_FACTORY));
        resolvers.add(new RequestAbstractFormMethodArgumentResolver(this.objectMapper, GLOBAL_ENUM_CONVERTER_FACTORY));
        resolvers.add(new FormdataBodyArgumentResolver(this.objectMapper, GLOBAL_ENUM_CONVERTER_FACTORY));
    }

    /**
     * 设置字符过滤器
     */
    @Bean
    @ConditionalOnMissingBean(CharacterEncodingFilter.class)
    public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
        FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(StringPool.UTF_8);
        characterEncodingFilter.setForceEncoding(true);
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }

    /**
     * 防 XSS 注入 Filter
     */
    @Bean
    @ConditionalOnProperty(value = ConfigKey.XssConfigKey.XSS_ENABLE_XSS_FILTER, havingValue = "true")
    public FilterRegistrationBean<Filter> xssFilterRegistration() {
        log.debug("加载防 XSS 注入 Filter: {}", XssFilter.class);
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new XssFilter(this.xssProperties.getExcludePatterns()));
        this.setUrlPatterns(bean, Ordered.LOWEST_PRECEDENCE);
        return bean;
    }

    /**
     * 前端传入的时间字符串, 自动转换为 Date 类型, 只针对普通的字段,
     * 如果是 @RequestBody 中的字段, 将使用 {@link MappingApiJackson2HttpMessageConverter} 使用 jackson 进行转换
     *
     * @param registry the registry
     */
    @Override
    public void addFormatters(@NotNull FormatterRegistry registry) {
        log.debug("注册 String -> Date 转换器 :[{}] 格式: [{}]", StringToDateConverter.class, StringToDateConverter.PATTERN);
        registry.addConverter(new StringToDateConverter());
        log.debug("注册通用枚举转换器: [{}]", GlobalEnumConverterFactory.class);
        registry.addConverterFactory(GLOBAL_ENUM_CONVERTER_FACTORY);
    }

    /**
     * 使用 JACKSON 作为JSON MessageConverter
     *
     * @param converters converters
     */
    @Override
    public void configureMessageConverters(@NotNull List<HttpMessageConverter<?>> converters) {
        log.debug("加载自定义消息增强转换器 [{}]", MappingApiJackson2HttpMessageConverter.class);
        converters.removeIf(x -> x instanceof StringHttpMessageConverter || x instanceof AbstractJackson2HttpMessageConverter);
        // Content-Type = text/plain 消息转换器, 强制使用 UTF-8
        converters.add(new StringHttpMessageConverter(Charsets.UTF_8));
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new BufferedImageHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new ResourceRegionHttpMessageConverter());
        this.config();
        converters.add(new MappingApiJackson2HttpMessageConverter(this.objectMapper));
    }

    /**
     * 自定义枚举序列化与反序列化方式
     */
    private void config() {
        log.debug("加载枚举自定义序列化/反序列化处理器: [{}] [{}]", EntityEnumSerializer.class, EntityEnumDeserializer.class);
        SimpleModule simpleModule = new SimpleModule("EntityEnum-Converter", PackageVersion.VERSION);
        simpleModule.addDeserializer(SerializeEnum.class, new EntityEnumDeserializer<>());
        simpleModule.addSerializer(SerializeEnum.class, new EntityEnumSerializer<>());
        this.objectMapper.registerModule(simpleModule);
        this.objectMapper.findAndRegisterModules();
    }

    /**
     * Build url patterns
     *
     * @return the list
     */
    private @NotNull @Unmodifiable List<String> buildUrlPatterns() {
        String contextPath = environment.getProperty(ConfigKey.SpringConfigKey.SERVER_CONTEXT_PATH, StringPool.SLASH);
        contextPath = StringTools.removeSuffix(contextPath, StringPool.SLASH);
        return Collections.singletonList(contextPath + StringPool.ANY_URL_PATTERNS);
    }

    /**
     * Sets url patterns *
     *
     * @param filter logging rb
     * @param order  order
     */
    private void setUrlPatterns(@NotNull FilterRegistrationBean<Filter> filter, int order) {
        this.setUrlPatterns(filter, order, this.buildUrlPatterns());
    }

    /**
     * Sets url patterns *
     *
     * @param filter      filter
     * @param order       order
     * @param urlPatterns url patterns
     */
    private void setUrlPatterns(@NotNull FilterRegistrationBean<Filter> filter, int order, Collection<String> urlPatterns) {
        filter.setUrlPatterns(urlPatterns);
        filter.setOrder(order);
    }

}
