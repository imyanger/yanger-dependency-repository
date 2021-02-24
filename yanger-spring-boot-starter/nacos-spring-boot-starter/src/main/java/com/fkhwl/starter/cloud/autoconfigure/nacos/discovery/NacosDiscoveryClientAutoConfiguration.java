package com.fkhwl.starter.cloud.autoconfigure.nacos.discovery;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryClientConfiguration;

import com.yanger.starter.basic.boost.YangerAutoConfiguration;
import com.yanger.starter.nacos.handler.EmptyServiceCleanerHandler;

import org.jetbrains.annotations.Contract;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.client.ConditionalOnBlockingDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.07.02 23:36
 * @since 1.5.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
@ConditionalOnBlockingDiscoveryEnabled
@ConditionalOnNacosDiscoveryEnabled
@AutoConfigureAfter(value = {NacosDiscoveryClientConfiguration.class})
@Import(NacosDiscoveryClientConfiguration.class)
public class NacosDiscoveryClientAutoConfiguration implements YangerAutoConfiguration {

    /**
     * Fkh nacos discovery client auto configuration
     *
     * @since 1.5.0
     */
    @Contract(pure = true)
    public NacosDiscoveryClientAutoConfiguration() {
    }

    /**
     * Empty service cleaner handler
     *
     * @param discoveryClient          discovery client
     * @param nacosDiscoveryProperties nacos discovery properties
     * @return the empty service cleaner handler
     * @since 1.6.0
     */
    @Bean
    public EmptyServiceCleanerHandler emptyServiceCleanerHandler(DiscoveryClient discoveryClient,
                                                                 NacosDiscoveryProperties nacosDiscoveryProperties) {
        return new EmptyServiceCleanerHandler(discoveryClient, nacosDiscoveryProperties);
    }
}
