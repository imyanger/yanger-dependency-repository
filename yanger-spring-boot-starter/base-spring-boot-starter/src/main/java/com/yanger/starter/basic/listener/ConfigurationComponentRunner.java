package com.yanger.starter.basic.listener;

import com.yanger.starter.basic.config.BaseAutoConfiguration;
import com.yanger.starter.basic.constant.OrderConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动完成后输出信息 Configuration Components
 * @Author yanger
 * @Date 2020/12/29 20:07
 */
@Slf4j
@Order(OrderConstant.CONFIGURATION_COMPONENT_RUNNER)
@Component
public class ConfigurationComponentRunner implements ApplicationRunner {

    /**
     * Run
     * @param args args
     */
    @Override
    public void run(ApplicationArguments args) {
        List<String> components = BaseAutoConfiguration.Constant.COMPONENTS;
        // 控制打印长度
        List<String> printComponents = new ArrayList<>(4);
        for (int i = 0; i < components.size(); i++) {
            printComponents.add(components.get(i));
            if(i == components.size() - 1 || printComponents.size() == 4) {
                log.info("Configuration Components Load:\n{}", printComponents);
                printComponents.clear();
            }
        }
    }

}
