package com.yanger.starter.web.listener;

import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.EndpointConst;
import com.yanger.starter.basic.constant.OrderConstant;
import com.yanger.starter.basic.listener.StarterInfoRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 启动完成后输出信息
 * @Author yanger
 * @Date 2020/12/29 20:07
 */
@Slf4j
@Order(OrderConstant.ORDER_SWAGGER_INFO_RUNNER)
@Component
@AutoConfigureAfter(StarterInfoRunner.class)
public class SwaggerInfoRunner implements ApplicationRunner {

    /**
     * Run
     * @param args args
     */
    @Override
    public void run(ApplicationArguments args) {
        if (App.serverUrl != null) {
            log.info("{} 服务已集成Swagger3： {}", App.applicationName, App.serverUrl + EndpointConst.SWAGGER3_REST_URL);
            log.info("{} 服务已集成Knife4j： {}", App.applicationName, App.serverUrl + EndpointConst.KNIFE4J_REST_URL);
        }
    }

}
