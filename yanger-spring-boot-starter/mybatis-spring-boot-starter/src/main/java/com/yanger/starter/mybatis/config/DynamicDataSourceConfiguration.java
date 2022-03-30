package com.yanger.starter.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.yanger.starter.basic.config.BaseAutoConfiguration;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.mybatis.dynamic.DataSourceContextHolder;
import com.yanger.starter.mybatis.dynamic.DynamicDataSource;
import com.yanger.starter.mybatis.plugins.DynamicPackageInterceptor;
import com.yanger.starter.mybatis.plugins.DynamicReadWriteInterceptor;
import com.yanger.starter.mybatis.property.DataSourceDetail;
import com.yanger.starter.mybatis.property.DynamicDataSourceProperties;
import com.yanger.starter.mybatis.utils.DruidDataSourceBuilder;
import com.yanger.tools.general.tools.StringTools;
import com.yanger.tools.web.exception.BasicException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@AutoConfigureAfter(DynamicDataSourceProperties.class)
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfiguration implements BaseAutoConfiguration {

    @Bean
    @Order(99)
    @ConditionalOnProperty(value = ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_PACKAGE_ENABLE, havingValue = "true", matchIfMissing = false)
    public DynamicPackageInterceptor dynamicPackageInterceptor() {
        return new DynamicPackageInterceptor();
    }

    @Bean
    @Order(100)
    @ConditionalOnProperty(value = ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_READ_WRITE_ENABLE, havingValue = "true", matchIfMissing = false)
    public DynamicReadWriteInterceptor dynamicReadWriteInterceptor() {
        return new DynamicReadWriteInterceptor();
    }

    @Bean
    @ConditionalOnProperty(value = ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_READ_WRITE_ENABLE, havingValue = "true", matchIfMissing = false)
    DataSource readDataSource(@NotNull DynamicDataSourceProperties dynamicDataSourceProperties){
        if(dynamicDataSourceProperties.getDynamicRead() == null) {
            throw new BasicException("已开启动态读写分离 spring.datasource.dynamic-read-write-enable = true， 但未设置 spring.datasource.dynamic-read 读库数据源信息");
        }
        return DruidDataSourceBuilder.build(dynamicDataSourceProperties.getDynamicRead());
    }

    @Bean
    @ConditionalOnProperty(value = ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_READ_WRITE_ENABLE, havingValue = "true", matchIfMissing = false)
    DataSource writeDataSource(@NotNull DynamicDataSourceProperties dynamicDataSourceProperties){
        if(dynamicDataSourceProperties.getDynamicWrite() == null) {
            throw new BasicException("已开启动态读写分离 spring.datasource.dynamic-read-write-enable = true， 但未设置 spring.datasource.dynamic-write 写库数据源信息");
        }
        return DruidDataSourceBuilder.build(dynamicDataSourceProperties.getDynamicWrite());
    }

    /**
     * 动态数据源: 通过AOP在不同数据源之间动态切换
     * @return
     */
    @Primary
    @Bean(name = "dynamicDataSource")
    @Conditional(DynamicCondition.class)
    //@ConditionalOnProperty(value = ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_ENABLE, havingValue = "true", matchIfMissing = false)
    public DataSource dynamicDataSource(@NotNull DynamicDataSourceProperties dynamicDataSourceProperties) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 多数据源 map
        Map<Object, Object> dsMap = new HashMap<>();

        // 读写数据源
        if(dynamicDataSourceProperties.getDynamicReadWriteEnable()) {
            if(dynamicDataSourceProperties.getDynamicRead() == null || dynamicDataSourceProperties.getDynamicWrite() == null) {
                throw new BasicException("已开启动态读写数据源，但未设置 spring.datasource.dynamic-read && dynamic-write 读写数据源信息");
            }
            DruidDataSource readDataSource = DruidDataSourceBuilder.build(dynamicDataSourceProperties.getDynamicRead());
            DruidDataSource writeDataSource = DruidDataSourceBuilder.build(dynamicDataSourceProperties.getDynamicWrite());
            dsMap.put(DataSourceContextHolder.READ_DATA_SOURCE, readDataSource);
            dsMap.put(DataSourceContextHolder.WRITE_DATA_SOURCE, writeDataSource);
            // 默认数据源
            dynamicDataSource.setDefaultTargetDataSource(writeDataSource);
            DataSourceContextHolder.setDefaultDB(DataSourceContextHolder.WRITE_DATA_SOURCE);
            // 配置多数据源
            dynamicDataSource.setTargetDataSources(dsMap);
        }

        // 多数据源
        if(dynamicDataSourceProperties.getDynamicEnable()) {
            if(dynamicDataSourceProperties.getDynamic() == null || dynamicDataSourceProperties.getDynamic().size() == 0) {
                throw new BasicException("已开启动态数据源 spring.datasource.dynamic-enable = true， 但未设置 spring.datasource.dynamic 多数据源信息");
            }
            Map<String, DataSourceDetail> defaultDataSourceMap = dynamicDataSourceProperties.getDynamic().entrySet()
                    .stream()
                    .filter(v -> v != null && v.getValue() != null && v.getValue().getDefaultEnable())
                    .collect(Collectors.toMap(v -> v.getKey(), v -> v.getValue()));
            if(defaultDataSourceMap.size() > 1) {
                throw BasicException.of("已开启动态数据源 spring.datasource.dynamic-enable = true，默认数据源数量为{}，请设置唯一", defaultDataSourceMap.size() );
            }
            Optional<Map.Entry<String, DataSourceDetail>> defaultDataSourceOp = defaultDataSourceMap.entrySet().stream().findFirst();
            if(DataSourceContextHolder.getDefaultDB() == null && !defaultDataSourceOp.isPresent()) {
                throw new BasicException("已开启动态数据源 spring.datasource.dynamic-enable = true， 但未设置默认数据源");
            }
            dynamicDataSourceProperties.getDynamic().forEach((k, v) -> {
                dsMap.put(k, DruidDataSourceBuilder.build(v));
                // 如果 effectPackage 存在，则添加 effectPackage 和 数据源名称到 DataSourceContextHolder.PACKAGE_DATA_SOURCE_MAP
                if(StringTools.isNotBlank(v.getEffectPackage())) {
                    DataSourceContextHolder.PACKAGE_DATA_SOURCE_MAP.put(v.getEffectPackage().endsWith("*") ? v.getEffectPackage() : v.getEffectPackage().concat("*"), k);
                }
            });
            // 默认数据源, 同时配置读写和多数据源，默认数据源取动态数据源默认数据源
            if(defaultDataSourceOp.isPresent()) {
                dynamicDataSource.setDefaultTargetDataSource(dsMap.get(defaultDataSourceOp.get().getKey()));
                DataSourceContextHolder.setDefaultDB(defaultDataSourceOp.get().getKey());
            }
            // 配置多数据源
            dynamicDataSource.setTargetDataSources(dsMap);
        }

        return dynamicDataSource;
    }

    @Primary
    @Bean
    @ConditionalOnProperty(value = ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_ENABLE, havingValue = "true", matchIfMissing = false)
    public DataSourceTransactionManager transactionManager(@NotNull DynamicDataSourceProperties dynamicDataSourceProperties) {
        return new DataSourceTransactionManager(dynamicDataSource(dynamicDataSourceProperties));
    }

}
