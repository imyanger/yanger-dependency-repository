package com.yanger.starter.nacos.config;

import com.alibaba.cloud.nacos.diagnostics.analyzer.NacosConnectionFailureException;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 管理 ConfigService, 用于操作自定义配置
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class NacosConfigManager {

    /** service */
    private static ConfigService service = null;

    /** Nacos config properties */
    private final YangerNacosProperties yangerNacosProperties;

    /**
     * Nacos config manager
     *
     * @param yangerNacosProperties nacos properties
     */
    public NacosConfigManager(YangerNacosProperties yangerNacosProperties) {
        this.yangerNacosProperties = yangerNacosProperties;
        service = createConfigService(yangerNacosProperties);
    }

    /**
     * Compatible with old design,It will be perfected in the future.
     *
     * @param yangerNacosProperties nacos properties
     * @return the config service
     */
    static ConfigService createConfigService(YangerNacosProperties yangerNacosProperties) {
        if (Objects.isNull(service)) {
            synchronized (NacosConfigManager.class) {
                try {
                    if (Objects.isNull(service)) {
                        service = NacosFactory.createConfigService(yangerNacosProperties.assembleConfigServiceProperties());
                    }
                } catch (NacosException e) {
                    throw new NacosConnectionFailureException(yangerNacosProperties.getAddress(), e.getMessage(), e);
                }
            }
        }
        return service;
    }

    /**
     * Gets config service *
     *
     * @return the config service
     */
    public ConfigService getConfigService() {
        if (Objects.isNull(service)) {
            createConfigService(this.yangerNacosProperties);
        }
        return service;
    }

    /**
     * Gets nacos config properties *
     *
     * @return the nacos config properties
     */
    public YangerNacosProperties getNacosConfigProperties() {
        return this.yangerNacosProperties;
    }

}
