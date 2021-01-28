package com.fkhwl.starter.mybatis.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.IllegalSQLInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import com.fkhwl.starter.basic.constant.ConfigDefaultValue;
import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.common.constant.App;
import com.fkhwl.starter.common.enums.LibraryEnum;
import com.fkhwl.starter.common.start.FkhAutoConfiguration;
import com.fkhwl.starter.mybatis.handler.GeneralEnumTypeHandler;
import com.fkhwl.starter.mybatis.handler.SerializableIdTypeHandler;
import com.fkhwl.starter.mybatis.handler.TimeMetaHandler;
import com.fkhwl.starter.mybatis.injector.MybatisSqlInjector;
import com.fkhwl.starter.mybatis.plugins.PerformanceInterceptor;
import com.fkhwl.starter.mybatis.plugins.SensitiveFieldDecryptIntercepter;
import com.fkhwl.starter.mybatis.plugins.SensitiveFieldEncryptIntercepter;
import com.fkhwl.starter.mybatis.util.SqlUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: mybatis 自动配置类 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.06 22:26
 * @since 1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisAutoConfiguration implements FkhAutoConfiguration {

    /**
     * 非法 SQL 语句拦截器
     * 1.必须使用到索引,包含left jion连接字段,符合索引最左原则
     * 必须使用索引好处,
     * 1.1 如果因为动态SQL,bug导致update的where条件没有带上,全表更新上万条数据
     * 1.2 如果检查到使用了索引,SQL性能基本不会太差
     * <p>
     * 2.SQL尽量单表执行,有查询left jion的语句,必须在注释里面允许该SQL运行,否则会被拦截,有left jion的语句,如果不能拆成单表执行的SQL,请leader商量在做
     * http://gaoxianglong.github.io/shark/
     * SQL尽量单表执行的好处
     * 2.1 查询条件简单、易于开理解和维护;
     * 2.2 扩展性极强; （可为分库分表做准备）
     * 2.3 缓存利用率高;
     * <p>
     * 2.在字段上使用函数
     * 3.where条件为空
     * 4.where条件使用了 !=
     * 5.where条件使用了 not 关键字
     * 6.where条件使用了 or 关键字
     * 7.where条件使用了 使用子查询
     *
     * @return the illegal sql interceptor
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean(IllegalSQLInterceptor.class)
    @Profile(value = {App.ENV_LOCAL, App.ENV_DEV, App.ENV_TEST, App.ENV_PREV})
    @ConditionalOnProperty(value = ConfigKey.MYBATIS_ENABLE_ILLEGAL_SQL_INTERCEPTOR, havingValue = ConfigDefaultValue.TRUE_STRING)
    public IllegalSQLInterceptor illegalSqlInterceptor() {
        return new IllegalSQLInterceptor();
    }

    /**
     * SQL执行分析插件, 拦截一些整表操作,在生产环境需要关闭.
     *
     * @return the sql explain interceptor
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean(SqlExplainInterceptor.class)
    @Profile(value = {App.ENV_LOCAL, App.ENV_DEV, App.ENV_TEST, App.ENV_PREV})
    @ConditionalOnProperty(value = ConfigKey.MYBATIS_ENABLE_SQL_EXPLAIN_INTERCEPTOR, havingValue = ConfigDefaultValue.TRUE_STRING)
    public SqlExplainInterceptor sqlExplainInterceptor() {
        return new SqlExplainInterceptor();
    }

    /**
     * 分页插件, 不需要设置方言, mybatis-plus 自动判断
     *
     * @param mybatisProperties mybatis properties
     * @return the pagination interceptor
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    public PaginationInterceptor paginationInterceptor(@NotNull MybatisProperties mybatisProperties) {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置默认最大分页数 (fkh.mybatis.single-page-limit)
        paginationInterceptor.setLimit(mybatisProperties.getSinglePageLimit());
        return paginationInterceptor;
    }

    /**
     * sql 注入
     *
     * @return the sql injector
     * @since 1.0.0
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new MybatisSqlInjector();
    }

    /**
     * mybatis-plus SQL执行效率插件 (生产环境可以关闭)
     *
     * @return the performance interceptor
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean(PerformanceInterceptor.class)
    @Profile(value = {App.ENV_LOCAL, App.ENV_DEV, App.ENV_TEST, App.ENV_PREV})
    @ConditionalOnProperty(value = ConfigKey.MybatisConfigKey.MYBATIS_ENABLE_LOG, havingValue = "true")
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

    /**
     * Sensitive field decrypt intercepter
     *
     * @param mybatisProperties mybatis properties
     * @return the sensitive field decrypt intercepter
     * @since 1.5.0
     */
    @Bean
    @ConditionalOnMissingBean(SensitiveFieldDecryptIntercepter.class)
    @ConditionalOnProperty(value = ConfigKey.MybatisConfigKey.MYBATIS_ENABLE_SENSITIVE, havingValue = "true", matchIfMissing = true)
    public SensitiveFieldDecryptIntercepter sensitiveFieldDecryptIntercepter(@NotNull MybatisProperties mybatisProperties) {
        return new SensitiveFieldDecryptIntercepter(mybatisProperties.getSensitiveKey());
    }

    /**
     * Sensitive field encrypt intercepter
     *
     * @param mybatisProperties mybatis properties
     * @return the sensitive field encrypt intercepter
     * @since 1.5.0
     */
    @Bean
    @ConditionalOnMissingBean(SensitiveFieldEncryptIntercepter.class)
    @ConditionalOnProperty(value = ConfigKey.MybatisConfigKey.MYBATIS_ENABLE_SENSITIVE, havingValue = "true", matchIfMissing = true)
    public SensitiveFieldEncryptIntercepter sensitiveFieldEncryptIntercepter(@NotNull MybatisProperties mybatisProperties) {
        SqlUtils.setSensitiveKey(mybatisProperties.getSensitiveKey());
        return new SensitiveFieldEncryptIntercepter(mybatisProperties.getSensitiveKey());
    }

    /**
     * 使用 MybatisEnumTypeHandler 代替默认的 EnumTypeHandler, 实现 EntityEnum 子类的类型转换(数据库存 value, 返回 Entity)
     *
     * @return the configuration customizer
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean(MybatisEnumTypeHandler.class)
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // 通用枚举转换器
            configuration.setDefaultEnumTypeHandler(GeneralEnumTypeHandler.class);
        };
    }

    /**
     * id 类型转换
     *
     * @return the configuration customizer
     * @since 1.0.0
     */
    @Bean
    public ConfigurationCustomizer idTypeHandlerCustomizer() {
        return configuration -> {
            // id 转换器
            configuration.getTypeHandlerRegistry().register(new SerializableIdTypeHandler(Serializable.class));
        };
    }

    /**
     * 自动创建时间和更新时间
     *
     * @return global config
     * @since 1.0.0
     */
    @Bean
    @ConditionalOnMissingBean(TimeMetaHandler.class)
    public MetaObjectHandler timeMetaHandler() {
        return new TimeMetaHandler();
    }

    /**
     * Gets library type *
     *
     * @return the library type
     * @since 1.0.0
     */
    @Override
    public LibraryEnum getLibraryType() {
        return LibraryEnum.DRUID;
    }

}
