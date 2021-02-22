package com.fkhwl.starter.dubbo.autoconfigure;

import com.google.common.collect.Lists;

import com.fkhwl.starter.dubbo.check.RpcCheck;
import com.fkhwl.starter.dubbo.check.RpcCheckImpl;
import com.fkhwl.starter.dubbo.listener.DubboRegistryInvokerRebuildListener;
import com.fkhwl.starter.dubbo.spi.DubboLauncherInitiation;
import com.fkhwl.starter.dubbo.support.CustomInfo;
import com.yanger.starter.basic.boost.YangerAutoConfiguration;
import com.yanger.starter.basic.context.EarlySpringContext;
import com.yanger.starter.basic.enums.LibraryEnum;
import com.yanger.tools.general.constant.StringPool;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 使用此组件, 依赖了 feign 后可直接通过 dubbo rest 方式调用</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.11.20 01:25
 * @since 1.0.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(value = {ApplicationConfig.class, DubboLauncherInitiation.class})
@EnableConfigurationProperties(DubboProperties.class)
public class DubboAutoConfiguration implements YangerAutoConfiguration {

    /**
     * 获取 dubbo 的 port
     *
     * @see NetUtils#getAvailablePort() 获取系统可用端口
     * @since 1.4.0
     */
    @Override
    public void execute() {
        Map<String, ProtocolConfig> beansOfType = EarlySpringContext.getApplicationContext().getBeansOfType(ProtocolConfig.class);
        List<CustomInfo> dubboInfos = Lists.newArrayList();
        beansOfType.forEach((k, v) -> {
            String ip = v.getHost();
            if (StringUtils.isBlank(ip)) {
                ip = NetUtils.getLocalHost();
            }
            String finalIp = ip;
            dubboInfos.add(() -> StartUtils.padding(LibraryEnum.DUBBO.getName()).toString()
                                 + "http://"
                                 + finalIp
                                 + StringPool.COLON
                                 + v.getPort());
        });

        StartUtils.addCustomInfo(dubboInfos);
    }

    /**
     * Dubbo registry invoker rebuild listener
     *
     * @return the dubbo registry invoker rebuild listener
     * @since 1.5.0
     */
    @Bean
    public DubboRegistryInvokerRebuildListener dubboRegistryInvokerRebuildListener() {
        return new DubboRegistryInvokerRebuildListener();
    }

    /**
     * Rpc check
     *
     * @return the rpc check
     * @since 1.5.0
     */
    @Bean
    public RpcCheck rpcCheck() {
        return new RpcCheckImpl();
    }

}
