package com.yanger.starter.id.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yanger.starter.basic.config.BaseAutoConfiguration;
import com.yanger.starter.id.enums.ProviderType;
import com.yanger.starter.id.property.IdProperties;
import com.yanger.starter.id.provider.DbMachineIdProvider;
import com.yanger.starter.id.provider.IpConfigurableMachineIdProvider;
import com.yanger.starter.id.provider.MachineIdProvider;
import com.yanger.starter.id.provider.PropertyMachineIdProvider;
import com.yanger.starter.id.service.IdService;
import com.yanger.starter.id.service.IdServiceImpl;
import com.yanger.starter.id.service.SnowflakeIdServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * id 生成配置装配
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(IdProperties.class)
public class IdAutoConfiguration implements BaseAutoConfiguration {

    /**
     * 基于配置生成 machineId，需要在部署的每一台机器上配置不同的机器号
     * @param idProperties id properties
     */
    @Bean
    @Conditional(value = PropertyTypeCondition.class)
    public IdService propertyIdService(@NotNull IdProperties idProperties) {
        log.info("Construct Property IdService machineId {}", idProperties.getMachineId());
        PropertyMachineIdProvider propertyMachineIdProvider = new PropertyMachineIdProvider();
        propertyMachineIdProvider.setMachineId(idProperties.getMachineId());
        return this.buildIdService(idProperties, propertyMachineIdProvider);
    }

    /**
     * 通过所有机器列表为每一个机器生成一个唯一的 id, 适合服务节点较少的情况
     * @param idProperties id properties
     */
    @Bean
    @Conditional(value = IpConfigurableCondition.class)
    public @NotNull IdService ipConfigurableIdService(@NotNull IdProperties idProperties) {
        log.info("Construct Ip Configurable IdService ips {}", idProperties.getIps());
        IpConfigurableMachineIdProvider ipConfigurableMachineIdProvider = new IpConfigurableMachineIdProvider(idProperties.getIps());
        return this.buildIdService(idProperties, ipConfigurableMachineIdProvider);
    }

    /**
     * 雪花算法生成 id
     * @param idProperties id properties
     */
    @Bean
    @Conditional(value = SnowFlakeCondition.class)
    public @NotNull IdService snowflakeIdService(@NotNull IdProperties idProperties) {
        log.info("Construct SnowflakeIdServiceImpl {}", idProperties.getMachineId());
        // 这里需要转化一下, 雪花算法使用的机器 id 不能超过 32
        return new SnowflakeIdServiceImpl(idProperties.getMachineId() % 32, ThreadLocalRandom.current().nextInt(32));
    }

    /**
     * 依赖数据库分配机器 id，生成 id
     * @param idProperties id properties
     */
    @Bean
    @Conditional(value = DbCondition.class)
    public @NotNull IdService dbIdService(@NotNull IdProperties idProperties) {
        log.info("Construct Db IdService driverClassName {} dbUrl {} dbUsername {} dbPassword {}",
                 idProperties.getDbDriverClassName(),
                 idProperties.getDbUrl(),
                 idProperties.getDbUsername(),
                 idProperties.getDbPassword());

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();

        try {
            comboPooledDataSource.setDriverClass(StringUtils.isNotBlank(idProperties.getDbDriverClassName()) ?
                                                 idProperties.getDbDriverClassName() : "com.mysql.cj.jdbc.Driver");
        } catch (PropertyVetoException e) {
            log.error("Wrong JDBC driver error: ", e);
            throw new IllegalStateException("Wrong JDBC driver ", e);
        }

        comboPooledDataSource.setMinPoolSize(5);
        comboPooledDataSource.setMaxPoolSize(30);
        comboPooledDataSource.setIdleConnectionTestPeriod(20);
        comboPooledDataSource.setMaxIdleTime(25);
        comboPooledDataSource.setBreakAfterAcquireFailure(false);
        comboPooledDataSource.setCheckoutTimeout(3000);
        comboPooledDataSource.setAcquireRetryAttempts(50);
        comboPooledDataSource.setAcquireRetryDelay(1000);

        comboPooledDataSource.setJdbcUrl(idProperties.getDbUrl());
        comboPooledDataSource.setUser(idProperties.getDbUsername());
        comboPooledDataSource.setPassword(idProperties.getDbPassword());

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setLazyInit(false);
        jdbcTemplate.setDataSource(comboPooledDataSource);

        DbMachineIdProvider dbMachineIdProvider = new DbMachineIdProvider();
        dbMachineIdProvider.setJdbcTemplate(jdbcTemplate);
        dbMachineIdProvider.init();

        return this.buildIdService(idProperties, dbMachineIdProvider);
    }

    /**
     * Build id service
     * @param idProperties      id properties
     * @param machineIdProvider machine id provider
     */
    @NotNull
    private IdServiceImpl buildIdService(@NotNull IdProperties idProperties, MachineIdProvider machineIdProvider) {
        IdServiceImpl idServiceImpl = new IdServiceImpl();
        idServiceImpl.setMachineIdProvider(machineIdProvider);

        if (idProperties.getDeployType() != null) {
            idServiceImpl.setDeployType(idProperties.getDeployType().getType());
        }
        if (idProperties.getIdType() != null) {
            idServiceImpl.setIdTypeValue(idProperties.getIdType().getVaule());
        }
        if (idProperties.getVersion() != null) {
            idServiceImpl.setVersion(idProperties.getVersion());
        }

        idServiceImpl.init();
        return idServiceImpl;
    }


    private static class PropertyTypeCondition extends ProviderTypeCondition {

        PropertyTypeCondition() {
            super(ProviderType.PROPERTY);
        }

    }

    private static class IpConfigurableCondition extends ProviderTypeCondition {

        IpConfigurableCondition() {
            super(ProviderType.IP_CONFIGURABLE);
        }

    }

    private static class DbCondition extends ProviderTypeCondition {

        DbCondition() {
            super(ProviderType.DB);
        }

    }

    private static class SnowFlakeCondition extends ProviderTypeCondition {

        SnowFlakeCondition() {
            super(ProviderType.SNOW_FLAKE);
        }

    }

}
