package com.yanger.starter.dubbo.listener;

import com.yanger.starter.basic.context.EarlySpringContext;
import com.yanger.starter.basic.listen.YangerApplicationListener;
import com.yanger.starter.dubbo.check.RpcCheck;
import com.yanger.starter.dubbo.constant.DubboConstant;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class DubboRegistryCheckListener implements YangerApplicationListener {

    /**
     * On application ready event
     *
     * @param event event
     */
    @Override
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {

        YangerApplicationListener.Runner.executeAtNext(this.getClass().getName(), () -> {
            RpcCheck genericService = EarlySpringContext.getInstance(RpcCheck.class);
            ServiceConfig<RpcCheck> service = new ServiceConfig<>();
            service.setApplication(EarlySpringContext.getInstance(ApplicationConfig.class));
            service.setInterface(RpcCheck.class);
            service.setRef(genericService);
            service.setVersion(System.getProperty("yanger-spring-boot.version"));
            service.setGroup(DubboConstant.RPC_CHECK_GROUP);
            service.export();
            log.info("发布 RPC Check 服务: {}", service);
        });
    }

}
