package com.fkhwl.starter.cloud.autoconfigure.nacos.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
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
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.07.02 23:45
 * @see NacosAutoServiceRegistration
 * @since 1.5.0
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
     * Fkh nacos discovery watch
     *
     * @param nacosDiscoveryProperties nacos discovery properties
     * @since 1.5.0
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
     * @since 1.5.0
     */
    @Override
    public void setApplicationEventPublisher(@NotNull ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * Destroy
     *
     * @since 1.5.0
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
     * @since 1.5.0
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
     * @since 1.5.0
     */
    public boolean isRunning() {
        return false;
    }

    /**
     * Is enabled
     *
     * @return the boolean
     * @since 1.5.0
     */
    protected boolean isEnabled() {
        return this.nacosDiscoveryProperties.isRegisterEnabled();
    }

    /**
     * Naming maintain service instance
     *
     * @return the naming maintain service
     * @since 1.5.0
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
     * @since 1.5.0
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
