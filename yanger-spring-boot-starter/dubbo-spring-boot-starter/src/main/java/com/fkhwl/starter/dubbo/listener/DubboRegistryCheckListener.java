package com.fkhwl.starter.dubbo.listener;

import com.fkhwl.starter.dubbo.constant.DubboConstant;
import com.fkhwl.starter.dubbo.check.RpcCheck;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.context.EarlySpringContext;
import com.yanger.starter.basic.listen.YangerApplicationListener;
import com.yanger.starter.basic.util.ConfigKit;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.07.07 22:16
 * @since 1.5.0
 */
@Slf4j
public class DubboRegistryCheckListener implements YangerApplicationListener {

    /**
     * On application ready event
     *
     * @param event event
     * @since 1.5.0
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
