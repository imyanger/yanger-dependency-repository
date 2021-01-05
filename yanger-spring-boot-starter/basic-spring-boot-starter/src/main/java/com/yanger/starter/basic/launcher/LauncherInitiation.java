package com.yanger.starter.basic.launcher;

import com.yanger.starter.basic.util.ConfigKit;
import com.yanger.starter.basic.util.JsonUtils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

/**
 * @Description launcher 扩展 用于一些组件发现
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
     * @param isLocalLaunch     is local launch
     * @since 1.0.0
     */
    default void launcherWrapper(Environment env, @NotNull Properties defaultProperties, String appName, boolean isLocalLaunch) {
        advance(appName);
        Map<String, Object> map = launcher(env, appName, isLocalLaunch);

        if (ConfigKit.isDebugModel()) {
            LOG.debug("{} 组件默认配置:\n{}", getName(), JsonUtils.toJson(map, true));
        }

        defaultProperties.putAll(map);
    }

    /**
     * 在启动 Spring Boot 之前执行自定义逻辑
     *
     * @param appName app name
     * @since 1.0.0
     */
    default void advance(String appName) { }

    /**
     * Launcher wrapper *
     *
     * @param env           env
     * @param appName       app name
     * @param isLocalLaunch is local launch
     * @return the chain map
     * @since 1.0.0
     */
    Map<String, Object> launcher(Environment env, String appName, boolean isLocalLaunch);

    /**
     * 获取排列顺序
     *
     * @return order order
     * @since 1.0.0
     */
    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 获取组件名
     *
     * @return the name
     * @since 1.0.0
     */
    String getName();

}
