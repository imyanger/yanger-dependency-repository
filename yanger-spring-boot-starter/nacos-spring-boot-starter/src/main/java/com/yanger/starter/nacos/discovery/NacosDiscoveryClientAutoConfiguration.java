package com.yanger.starter.nacos.discovery;

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
 * @Description
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnDiscoveryEnabled
@ConditionalOnBlockingDiscoveryEnabled
@ConditionalOnNacosDiscoveryEnabled
@AutoConfigureAfter(value = {NacosDiscoveryClientConfiguration.class})
@Import(NacosDiscoveryClientConfiguration.class)
public class NacosDiscoveryClientAutoConfiguration implements YangerAutoConfiguration {

    /**
     * yanger nacos discovery client auto configuration
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
     */
    @Bean
    public EmptyServiceCleanerHandler emptyServiceCleanerHandler(DiscoveryClient discoveryClient,
                                                                 NacosDiscoveryProperties nacosDiscoveryProperties) {
        return new EmptyServiceCleanerHandler(discoveryClient, nacosDiscoveryProperties);
    }
}
