package com.yanger.starter.log.spi;

import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.tools.web.support.ChainMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.logging.LogLevel;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @Description log 的默认配置
 * @Author yanger
 * @Date 2021/2/25 18:04
 */
@AutoService(LauncherInitiation.class)
public class LogLauncherInitiation implements LauncherInitiation {

    /**
     * 加载默认配置
     *
     * @param env               系统变量 Environment
     * @param appName           服务名
     * @return the chain map
     */
    @Override
    public Map<String, Object> launcher(Environment env, String appName) {
        return ChainMap.build(8)
            .put(ConfigKey.LogConfigKey.APP_NAME, App.applicationName)
            .put(ConfigKey.LogConfigKey.LEVEL, LogLevel.INFO.name());
    }

    /**
     * Gets order *
     *
     * @return the order
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

    /**
     * Gets name *
     *
     * @return the name
     */
    @Override
    public String getName() {
        return "log-spring-boot-starter:LogLauncherInitiation";
    }

}

