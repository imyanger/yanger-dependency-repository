package com.yanger.starter.basic.launcher;

import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.util.ConfigKit;

import org.slf4j.MDC;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 启动完成后输出信息
 * @Author yanger
 * @Date 2020/12/29 20:07
 */
@Slf4j
@Order
@Component
public class StarterInfoRunner implements ApplicationRunner {

    /**
     * Run *
     *
     * @param args args
     * @since 1.0.0
     */
    @Override
    public void run(ApplicationArguments args) {
        // 打印配置信息

        if (App.START_JUNIT.equals(ConfigKit.getProperty(App.START_TYPE))) {
            return;
        }

        String str = MDC.get(App.LIBRARY_NAME);
        MDC.remove(App.LIBRARY_NAME);
    }
}
