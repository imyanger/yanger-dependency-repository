package com.yanger.starter.basic.spi;

import com.yanger.starter.basic.util.JsonUtils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

/**
 * @Description launcher 扩展，用于一些组件发现
 * @Author yanger
 * @Date 2020/12/29 18:57
 */
public interface LauncherInitiation extends Ordered {

    /** log */
    Logger LOG = LoggerFactory.getLogger(LauncherInitiation.class);

    /**
     * 启动时 处理 SpringApplicationBuilder
     *
     * @param env               系统变量 Environment
     * @param defaultProperties 默认配置
     * @param appName           服务名
     */
    default void launcherWrapper(Environment env, @NotNull Properties defaultProperties, String appName) {

        advance(appName);

        Map<String, Object> map = launcher(env, appName);

        LOG.info("SPI扩展组件[{}]默认配置：\n{}", getName(), JsonUtils.toJson(map, true));

        defaultProperties.putAll(map);
    }

    /**
     * 在启动 Spring Boot 之前执行自定义逻辑
     *
     * @param appName app name
     */
    default void advance(String appName) {}

    /**
     * 加载默认配置
     *
     * @param env               系统变量 Environment
     * @param appName           服务名
     * @return the chain map
     */
    Map<String, Object> launcher(Environment env, String appName);

    /**
     * 获取排列顺序
     *
     * @return order order
     */
    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 获取组件名
     *
     * @return the name
     */
    String getName();

}
