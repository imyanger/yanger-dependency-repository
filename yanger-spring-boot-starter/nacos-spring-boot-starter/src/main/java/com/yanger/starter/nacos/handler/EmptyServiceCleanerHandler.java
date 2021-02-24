package com.yanger.starter.nacos.handler;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainFactory;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.yanger.starter.basic.event.BaseEventHandler;
import com.yanger.starter.basic.util.ConfigKit;
import com.yanger.tools.web.tools.TimeoutUtil;

import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class EmptyServiceCleanerHandler extends BaseEventHandler<HeartbeatEvent> {

    /** Discovery client */
    private final DiscoveryClient discoveryClient;

    /** Nacos discovery properties */
    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    /** Naming service */
    private final NamingService namingService;

    /**
     * Empty service cleaner handler
     *
     * @param discoveryClient          discovery client
     * @param nacosDiscoveryProperties nacos discovery properties
     */
    public EmptyServiceCleanerHandler(DiscoveryClient discoveryClient, NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.discoveryClient = discoveryClient;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.namingService = this.namingService();
    }

    /**
     * Handler.
     *
     * @param event the event
     */
    @Override
    @EventListener(HeartbeatEvent.class)
    public void handler(@NotNull HeartbeatEvent event) {
        if (this.namingService != null) {
            try {
                List<String> services = this.discoveryClient.getServices();
                log.debug("{}", services);
                for (String service : services) {
                    if (this.discoveryClient.getInstances(service).size() == 0) {
                        TimeoutUtil.process(() -> {
                            boolean result = false;
                            NamingMaintainService namingMaintainService = this.namingMaintainServiceInstance();
                            if (namingMaintainService != null) {
                                try {
                                    result = namingMaintainService.deleteService(service,
                                                                                 this.nacosDiscoveryProperties.getGroup());
                                    log.info("删除 Nacos 实例: {}:{}", ConfigKit.getAppName(), this.nacosDiscoveryProperties.getGroup());
                                } catch (Exception e) {
                                    log.warn("{}", e.getMessage());
                                }
                            }
                            return result;
                        }, 2);
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Naming maintain service instance
     *
     * @return the naming maintain service
     */
    public NamingMaintainService namingMaintainServiceInstance() {
        NamingMaintainService namingMaintainService = null;
        try {
            namingMaintainService = NamingMaintainFactory.createMaintainService(this.getNacosProperties());
        } catch (Exception var2) {
            log.error("create naming service error!properties={},e=,", this, var2);
        }
        return namingMaintainService;
    }

    /**
     * Naming service
     *
     * @return the naming service
     */
    public NamingService namingService() {
        NamingService namingService = null;
        try {
            namingService = NacosFactory.createNamingService(this.getNacosProperties());
        } catch (NacosException e) {
            log.error("", e);
        }

        return namingService;
    }

    /**
     * Gets nacos properties *
     *
     * @return the nacos properties
     */
    private @NotNull Properties getNacosProperties() {
        Properties properties = new Properties();
        properties.put("serverAddr", this.nacosDiscoveryProperties.getServerAddr());
        properties.put("namespace", this.nacosDiscoveryProperties.getNamespace());
        properties.put("clusterName", this.nacosDiscoveryProperties.getClusterName());
        properties.put("namingLoadCacheAtStart", this.nacosDiscoveryProperties.getNamingLoadCacheAtStart());
        log.debug("{}", properties);
        return properties;
    }

}


