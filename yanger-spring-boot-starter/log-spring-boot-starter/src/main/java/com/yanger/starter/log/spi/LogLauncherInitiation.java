package com.yanger.starter.log.spi;

import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigDefaultValue;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.tools.web.support.ChainMap;
import org.springframework.boot.logging.LogLevel;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * log 的默认配置
 * @Author yanger
 * @Date 2021/2/25 18:04
 */
@AutoService(LauncherInitiation.class)
public class LogLauncherInitiation implements LauncherInitiation {

    /**
     * 加载默认配置
     * @param env               系统变量 Environment
     * @param appName           服务名
     * @return the chain map
     */
    @Override
    public Map<String, Object> launcher(Environment env, String appName) {
        return ChainMap.build(8)
                .put(ConfigKey.LogConfigKey.APP_NAME, App.applicationName)
                .put(ConfigKey.LogConfigKey.LEVEL, LogLevel.INFO.name())
                // log 配置
                .put(ConfigKey.LogConfigKey.LOGBOOK_FILTER_ENABLED, ConfigDefaultValue.LogBookConfigValue.ENABLE)
                .put(ConfigKey.LogConfigKey.LOGBOOK_WRITE_LEVEL, ConfigDefaultValue.LogBookConfigValue.LOGBOOK_WRITE_LEVEL)
                .put(ConfigKey.LogConfigKey.LOGBOOK_FORMAT_STYLE, ConfigDefaultValue.LogBookConfigValue.LOGBOOK_FORMAT_STYLE)
                .put(ConfigKey.LogConfigKey.LOGGING_LEVEL_ORG_ZALANDO_LOGBOOK, ConfigDefaultValue.LogBookConfigValue.LOGGING_LEVEL_ORG_ZALANDO_LOGBOOK);
    }

    /**
     * Gets order
     * @return the order
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

    /**
     * Gets name
     * @return the name
     */
    @Override
    public String getName() {
        return "log-spring-boot-starter:LogLauncherInitiation";
    }

}

