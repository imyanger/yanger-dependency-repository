package com.fkhwl.starter.cloud.autoconfigure.nacos.config;

import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.yanger.starter.basic.boost.YangerAutoConfiguration;
import com.yanger.starter.nacos.spi.NacosLauncherInitiation;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.22 19:26
 * @since 1.0.0
 */
@Slf4j
@Configuration
@Import(NacosProperties.class)
@ConditionalOnClass(NacosLauncherInitiation.class)
public class NacosAutoConfiguration implements YangerAutoConfiguration {

    /** Fkh nacos properties */
    @Resource
    private NacosProperties fkhNacosProperties;

    /**
     * Execute
     *
     * @since 1.0.0
     */
    @Override
    public void execute() {
        log.info("加载 Nacos 自定义配置: {}", this.fkhNacosProperties);
    }

    /**
     * Fkh nacos config manager fkh nacos config manager
     *
     * @param nacosProperties fkh nacos properties
     * @return the fkh nacos config manager
     * @since 1.0.0
     */
    @Bean
    public NacosConfigManager fkhNacosConfigManager(@NotNull NacosProperties nacosProperties) {
        List<Listener> listeners = nacosProperties.getListener();

        NacosConfigManager manager = new NacosConfigManager(nacosProperties);

        listeners.forEach(listener -> {
            try {
                manager.getConfigService().addListener(nacosProperties.getDataId(),
                                                       nacosProperties.getGroup(),
                                                       listener);
            } catch (NacosException e) {
                throw new RuntimeException("添加 Nacos Config 监听器失败", e);
            }
        });

        return manager;
    }

}
