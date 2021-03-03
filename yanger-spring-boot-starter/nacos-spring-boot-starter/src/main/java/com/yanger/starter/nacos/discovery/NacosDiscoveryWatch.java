package com.yanger.starter.nacos.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.NamingMaintainFactory;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.client.naming.net.NamingProxy;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.util.ConfigKit;
import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.tools.web.tools.TimeoutUtil;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class NacosDiscoveryWatch implements ApplicationEventPublisherAware {

    /** Nacos discovery properties */
    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    /** Running */
    private final AtomicBoolean running;

    /** Publisher */
    private ApplicationEventPublisher publisher;

    /**
     * nacos discovery watch
     *
     * @param nacosDiscoveryProperties nacos discovery properties
     */
    @Contract(pure = true)
    public NacosDiscoveryWatch(NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.running = new AtomicBoolean(false);
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.running.compareAndSet(false, true);
    }

    /**
     * Sets application event publisher *
     *
     * @param publisher publisher
     */
    @Override
    public void setApplicationEventPublisher(@NotNull ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Destroy
     */
    @PreDestroy
    public void destroy() {
        this.stop(App.applicationName);
    }

    /**
     * Stop
     *
     * @param appName app name
     * @see NamingProxy#reqAPI(String, java.util.Map, java.util.List, String)
     */
    public void stop(String appName) {
        log.info("应用关闭, 删除实例");
        if (this.running.compareAndSet(true, false) && this.isEnabled()) {

            TimeoutUtil.process(() -> {
                boolean result = false;
                NamingMaintainService namingMaintainService = this.namingMaintainServiceInstance();
                if (namingMaintainService != null) {
                    log.info("{}", JsonUtils.toJson(namingMaintainService, true));
                    try {
                        result = namingMaintainService.deleteService(appName,
                                                                     this.nacosDiscoveryProperties.getGroup());
                        log.info("应用关闭, 删除 Nacos 实例: {}:{}", ConfigKit.getAppName(), this.nacosDiscoveryProperties.getGroup());
                    } catch (Exception e) {
                        log.warn("{}", e.getMessage());
                    }
                }
                return result;
            }, 2);
        }
    }

    /**
     * Is running
     *
     * @return the boolean
     */
    public boolean isRunning() {
        return false;
    }

    /**
     * Is enabled
     *
     * @return the boolean
     */
    protected boolean isEnabled() {
        return this.nacosDiscoveryProperties.isRegisterEnabled();
    }

    /**
     * Naming maintain service instance
     *
     * @return the naming maintain service
     */
    public NamingMaintainService namingMaintainServiceInstance() {
        NamingMaintainService namingMaintainService;
        try {
            namingMaintainService = NamingMaintainFactory.createMaintainService(this.getNacosProperties());
        } catch (Exception var2) {
            log.error("create naming service error!properties={},e=,", this, var2);
            return null;
        }

        return namingMaintainService;
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
