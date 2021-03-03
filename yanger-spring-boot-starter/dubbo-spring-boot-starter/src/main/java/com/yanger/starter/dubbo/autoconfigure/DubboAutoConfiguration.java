package com.yanger.starter.dubbo.autoconfigure;

import com.google.common.collect.Lists;

import com.yanger.starter.basic.boost.YangerAutoConfiguration;
import com.yanger.starter.basic.context.EarlySpringContext;
import com.yanger.starter.basic.enums.LibraryEnum;
import com.yanger.starter.dubbo.check.RpcCheck;
import com.yanger.starter.dubbo.check.RpcCheckImpl;
import com.yanger.starter.dubbo.listener.DubboRegistryInvokerRebuildListener;
import com.yanger.starter.dubbo.spi.DubboLauncherInitiation;
import com.yanger.starter.dubbo.support.CustomInfo;
import com.yanger.starter.dubbo.support.DubboStartInfo;
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
 * @Description 使用此组件, 依赖了 feign 后可直接通过 dubbo rest 方式调用
 * @Author yanger
 * @Date 2021/1/28 19:08
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
            dubboInfos.add(() -> DubboStartInfo.padding(LibraryEnum.DUBBO.getName()).toString()
                                 + "http://"
                                 + finalIp
                                 + StringPool.COLON
                                 + v.getPort());
        });

        DubboStartInfo.addCustomInfo(dubboInfos);
    }

    /**
     * Dubbo registry invoker rebuild listener
     *
     * @return the dubbo registry invoker rebuild listener
     */
    @Bean
    public DubboRegistryInvokerRebuildListener dubboRegistryInvokerRebuildListener() {
        return new DubboRegistryInvokerRebuildListener();
    }

    /**
     * Rpc check
     *
     * @return the rpc check
     */
    @Bean
    public RpcCheck rpcCheck() {
        return new RpcCheckImpl();
    }

}
