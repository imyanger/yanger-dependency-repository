package com.yanger.starter.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanger.starter.basic.boost.YangerAutoConfiguration;
import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.starter.web.jackson.JavaTimeModule;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description Jackson配置类
 * @Author yanger
 * @Date 2021/1/27 19:27
 */
@Slf4j
@Configuration
@ConditionalOnClass(value = {ObjectMapper.class})
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration implements YangerAutoConfiguration {

    /** Jackson properties */
    private JacksonProperties jacksonProperties;

    /**
     * Jackson configuration
     *
     * @param jacksonPropertiesObjectProvider jackson properties object provider
     */
    public JacksonConfiguration(@NotNull ObjectProvider<JacksonProperties> jacksonPropertiesObjectProvider) {
        if (jacksonPropertiesObjectProvider.getIfAvailable() != null) {
            this.jacksonProperties = jacksonPropertiesObjectProvider.getIfAvailable();
        }
    }

    /**
     * Object mapper object mapper.
     *
     * @return the object mapper
     */
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper globalObjectMapper = JsonUtils.getCopyMapper();
        if (this.jacksonProperties.getDefaultPropertyInclusion() != null) {
            globalObjectMapper.setSerializationInclusion(this.jacksonProperties.getDefaultPropertyInclusion());
        }
        // 添加 JDK8 的时间转换器
        globalObjectMapper.registerModule(new JavaTimeModule());
        globalObjectMapper.findAndRegisterModules();
        log.debug("初始化自定义 Jackson 配置 [{}]", globalObjectMapper);
        return globalObjectMapper;
    }

}
