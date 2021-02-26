package com.yanger.starter.basic.listen;

import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.constant.OrderConstant;

import org.slf4j.MDC;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.*;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 启动完成后输出信息
 * @Author yanger
 * @Date 2020/12/29 20:07
 */
@Slf4j
@Order(OrderConstant.ORDER_STARTER_INFO_RUNNER)
@Component
public class StarterInfoRunner implements ApplicationRunner, ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    /**
     * Run *
     *
     * @param args args
     */
    @Override
    public void run(ApplicationArguments args) {

        String str = MDC.get(ConfigKey.SystemConfigKey.LIBRARY_NAME);
        MDC.remove(ConfigKey.SystemConfigKey.LIBRARY_NAME);

        String serverUrl = getServerUrl();
        App.serverUrl = serverUrl;
        System.setProperty(ConfigKey.APPLICATION_SERVER_URL, serverUrl);

        log.info("{} 服务启动成功，访问地址：{}", App.applicationName, serverUrl);
    }

    private String getServerUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://" + address.getHostAddress() + ":" + this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

}
