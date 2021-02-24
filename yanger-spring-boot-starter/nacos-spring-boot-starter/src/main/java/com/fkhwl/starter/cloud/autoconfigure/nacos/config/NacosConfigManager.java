package com.fkhwl.starter.cloud.autoconfigure.nacos.config;

import com.alibaba.cloud.nacos.diagnostics.analyzer.NacosConnectionFailureException;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 管理 ConfigService, 用于操作自定义配置 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.20 18:01
 * @since 1.0.0
 */
@Slf4j
public class NacosConfigManager {

    /** service */
    private static ConfigService service = null;

    /** Nacos config properties */
    private final NacosProperties fkhNacosProperties;

    /**
     * Nacos config manager
     *
     * @param fkhNacosProperties fkh nacos properties
     * @since 1.0.0
     */
    public NacosConfigManager(NacosProperties fkhNacosProperties) {
        this.fkhNacosProperties = fkhNacosProperties;
        service = createConfigService(fkhNacosProperties);
    }

    /**
     * Compatible with old design,It will be perfected in the future.
     *
     * @param nacosProperties fkh nacos properties
     * @return the config service
     * @since 1.0.0
     */
    static ConfigService createConfigService(NacosProperties nacosProperties) {
        if (Objects.isNull(service)) {
            synchronized (NacosConfigManager.class) {
                try {
                    if (Objects.isNull(service)) {
                        service = NacosFactory.createConfigService(nacosProperties.assembleConfigServiceProperties());
                    }
                } catch (NacosException e) {
                    throw new NacosConnectionFailureException(nacosProperties.getAddress(), e.getMessage(), e);
                }
            }
        }
        return service;
    }

    /**
     * Gets config service *
     *
     * @return the config service
     * @since 1.0.0
     */
    public ConfigService getConfigService() {
        if (Objects.isNull(service)) {
            createConfigService(this.fkhNacosProperties);
        }
        return service;
    }

    /**
     * Gets nacos config properties *
     *
     * @return the nacos config properties
     * @since 1.0.0
     */
    public NacosProperties getNacosConfigProperties() {
        return this.fkhNacosProperties;
    }

}
