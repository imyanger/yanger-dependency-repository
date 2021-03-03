package com.yanger.starter.nacos.config;

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
 * @Description
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
@Configuration
@Import(YangerNacosProperties.class)
@ConditionalOnClass(NacosLauncherInitiation.class)
public class NacosAutoConfiguration implements YangerAutoConfiguration {

    /** nacos properties */
    @Resource
    private YangerNacosProperties yangerNacosProperties;

    /**
     * Execute
     */
    @Override
    public void execute() {
        log.info("加载 Nacos 自定义配置: {}", this.yangerNacosProperties);
    }

    /**
     * nacos config manager nacos config manager
     *
     * @param yangerNacosProperties nacos properties
     * @return the nacos config manager
     */
    @Bean
    public NacosConfigManager nacosConfigManager(@NotNull YangerNacosProperties yangerNacosProperties) {
        List<Listener> listeners = yangerNacosProperties.getListener();

        NacosConfigManager manager = new NacosConfigManager(yangerNacosProperties);

        listeners.forEach(listener -> {
            try {
                manager.getConfigService().addListener(yangerNacosProperties.getDataId(),
                                                       yangerNacosProperties.getGroup(),
                                                       listener);
            } catch (NacosException e) {
                throw new RuntimeException("添加 Nacos Config 监听器失败", e);
            }
        });

        return manager;
    }

}
