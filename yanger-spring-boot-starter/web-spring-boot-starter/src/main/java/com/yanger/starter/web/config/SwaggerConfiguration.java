package com.yanger.starter.web.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger配置类
 * @Author yanger
 * @Date 2020/7/15 16:27
 */
@Configuration
@EnableKnife4j
@EnableOpenApi
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
            .enable(true)
            .select()
            // 当前包路径
            .apis(RequestHandlerSelectors.basePackage("com.yanger"))
            .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            // 页面标题
            .title("Yanger SpringBoot Starter Swagger API接口").description("Yanger SpringBoot Starter Swagger API接口文档")
            .description("Yanger SpringBoot Starter")
            .termsOfServiceUrl("https://github.com/imyanger")
            // 创建人
            .contact(new Contact("yanger", "https://github.com/imyanger/", "550799932@qq.com")).version("1.0.0")
            .build();
    }

}
