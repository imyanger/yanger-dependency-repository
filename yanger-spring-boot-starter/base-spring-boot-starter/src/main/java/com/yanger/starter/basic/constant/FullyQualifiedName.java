package com.yanger.starter.basic.constant;

/**
 * 类常量
 * @Author yanger
 * @Date 2020/12/29 17:14
 */
public class FullyQualifiedName {

    /** spring boot application */
    public static final String SPRING_BOOT_APPLICATION = "org.springframework.boot.autoconfigure.SpringBootApplication";

    /** enable auto configuration */
    public static final String ENABLE_AUTOCONFIGURATION = "org.springframework.boot.autoconfigure.EnableAutoConfiguration";

    /** spring-cloud-context 中的 class */
    public static final String CLOUD_CLASS = "org.springframework.cloud.bootstrap.BootstrapImportSelectorConfiguration";

    /** spring-bbot 中的 class */
    public static final String BOOT_CLASS = "org.springframework.boot.SpringBootVersion";

    /** datasource auto configuration */
    public static final String DATASOURCE_AUTOCONFIGURATION = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration";

    /** mybatis-plus auto configuration */
    public static final String MYBATISPLUS_AUTOCONFIGURATION = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration";

    /** druid datasource auto configure */
    public static final String DRUIDDATASOURCE_AUTOCONFIGURE = "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure";

    /** mybatis auto configuration */
    public static final String MYBATIS_AUTOCONFIGURATION = "com.yanger.starter.mybatis.config.MybatisAutoConfiguration";

}
