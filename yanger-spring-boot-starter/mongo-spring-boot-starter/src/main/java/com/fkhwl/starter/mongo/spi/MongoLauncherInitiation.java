package com.fkhwl.starter.mongo.spi;

import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.basic.util.StringPool;
import com.fkhwl.starter.common.start.LauncherInitiation;
import com.fkhwl.starter.core.support.ChainMap;
import com.fkhwl.starter.core.util.StringUtils;
import com.fkhwl.starter.mongo.constant.MongoConstant;
import com.fkhwl.starter.processor.annotation.AutoService;

import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 通过 SPI 加载 mongo 默认配置</p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:41
 * @since 1.0.0
 */
@AutoService(LauncherInitiation.class)
public class MongoLauncherInitiation implements LauncherInitiation {
    /** MONGOAUTOCONFIGURATION */
    public static final String MONGOAUTOCONFIGURATION = "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration";
    /** MONGODATAAUTOCONFIGURATION */
    public static final String MONGODATAAUTOCONFIGURATION = "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration";

    /**
     * 启动之前关闭默认的 RedisAutoConfiguration 自动装配
     *
     * @param appName app name
     * @since 1.0.0
     */
    @Override
    public void advance(String appName) {
        LOG.warn("禁用 {} 和 {}", MONGOAUTOCONFIGURATION, MONGODATAAUTOCONFIGURATION);

        String property = System.getProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE);

        String value;
        if (StringUtils.isBlank(property)) {
            value = String.join(StringPool.COMMA, MONGOAUTOCONFIGURATION, MONGODATAAUTOCONFIGURATION);

        } else {
            value = String.join(StringPool.COMMA, property, MONGOAUTOCONFIGURATION, MONGODATAAUTOCONFIGURATION);
        }
        System.setProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE, value);
    }

    /**
     * Launcher *
     *
     * @param env           env
     * @param appName       app name
     * @param isLocalLaunch is local launch
     * @return the map
     * @since 1.0.0
     */
    @Override
    public Map<String, Object> launcher(Environment env,
                                        String appName,
                                        boolean isLocalLaunch) {

        return ChainMap.build(2)
            .put("mongo", "test");

    }

    /**
     * Gets order *
     *
     * @return the order
     * @since 1.0.0
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    /**
     * Gets name *
     *
     * @return the name
     * @since 1.0.0
     */
    @Override
    public String getName() {
        return MongoConstant.MODULE_NAME;
    }
}
