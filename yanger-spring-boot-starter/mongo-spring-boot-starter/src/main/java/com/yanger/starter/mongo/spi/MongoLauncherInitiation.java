package com.yanger.starter.mongo.spi;

import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.web.support.ChainMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * @Description 通过 SPI 加载 mongo 默认配置
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@AutoService(LauncherInitiation.class)
public class MongoLauncherInitiation implements LauncherInitiation {

    /** MONGO_AUTO_CONFIGURATION */
    public static final String MONGO_AUTO_CONFIGURATION = "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration";

    /** MONGO_DATA_AUTO_CONFIGURATION */
    public static final String MONGO_DATA_AUTO_CONFIGURATION = "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration";

    /**
     * 启动之前关闭默认的 RedisAutoConfiguration 自动装配
     *
     * @param appName app name
     */
    @Override
    public void advance(String appName) {
        LOG.warn("禁用 {} 和 {}", MONGO_AUTO_CONFIGURATION, MONGO_DATA_AUTO_CONFIGURATION);

        String property = System.getProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE);

        String value;
        if (StringUtils.isBlank(property)) {
            value = String.join(StringPool.COMMA, MONGO_AUTO_CONFIGURATION, MONGO_DATA_AUTO_CONFIGURATION);

        } else {
            value = String.join(StringPool.COMMA, property, MONGO_AUTO_CONFIGURATION, MONGO_DATA_AUTO_CONFIGURATION);
        }
        System.setProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE, value);
    }

    /**
     * Launcher *
     *
     * @param env     env
     * @param appName app name
     * @return the map
     */
    @Override
    public Map<String, Object> launcher(Environment env, String appName) {
        return ChainMap.build(2)
            .put("mongo", "test");

    }

    /**
     * Gets order *
     *
     * @return the order
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    /**
     * Gets name *
     *
     * @return the name
     */
    @Override
    public String getName() {
        return "mongo-spring-boot-starter:MongoLauncherInitiation";
    }
}
