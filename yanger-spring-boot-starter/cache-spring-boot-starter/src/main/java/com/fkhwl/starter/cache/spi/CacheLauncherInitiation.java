package com.fkhwl.starter.cache.spi;

import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.common.constant.App;
import com.fkhwl.starter.common.start.LauncherInitiation;
import com.fkhwl.starter.core.support.ChainMap;
import com.fkhwl.starter.processor.annotation.AutoService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 12:23
 * @since 1.0.0
 */
@AutoService(LauncherInitiation.class)
public class CacheLauncherInitiation implements LauncherInitiation {
    /** REDISAUTOCONFIGURATION */
    public static final String REDISAUTOCONFIGURATION = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration";

    /**
     * 启动之前关闭默认的 RedisAutoConfiguration 自动装配
     *
     * @param appName app name
     * @since 1.0.0
     */
    @Override
    public void advance(String appName) {
        LOG.warn("默认的 {} 已被禁用", REDISAUTOCONFIGURATION);
        String property = System.getProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE);

        String value;
        if (StringUtils.isBlank(property)) {
            value = REDISAUTOCONFIGURATION;

        } else {
            value = String.join(",",
                                property,
                                REDISAUTOCONFIGURATION);
        }
        System.setProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE, value);

    }

    /**
     * Launcher map
     *
     * @param env           env
     * @param appName       app name
     * @param isLocalLaunch is local launch
     * @return the map
     * @since 1.0.0
     */
    @Override
    public Map<String, Object> launcher(Environment env, String appName, boolean isLocalLaunch) {

        return ChainMap.build(8)
            .put("jetcache.statIntervalMinutes", 30)
            .put("jetcache.areaInCacheName", false)
            .put("jetcache.hidePackages", App.BASE_PACKAGES)
            // 缓存类型. tair、redis 为当前支持的远程缓存; linkedhashmap、caffeine 为当前支持的本地缓存类型
            .put("jetcache.local.default.type", "caffeine")
            // 每个缓存实例的最大元素的全局配置, 仅 local 类型的缓存需要指定. 注意是每个缓存实例的限制, 而不是全部,
            // 比如这里指定 1000, 然后用 @CreateCache 创建了两个缓存实例（并且注解上没有设置 localLimit 属性）, 那么每个缓存实例的限制都是 100
            .put("jetcache.local.default.limit", 1000)
            // key 转换器的全局配置, 当前只有一个已经实现的 keyConvertor: fastjson.
            // 仅当使用 @CreateCache 且缓存类型为 LOCAL 时可以指定为 none, 此时通过 equals 方法来识别 key. 方法缓存必须指定 keyConvertor
            .put("jetcache.local.default.keyConvertor", "fastjson")
            .put("jetcache.remote.default.keyConvertor", "fastjson")
            .put("jetcache.remote.default.valueDecoder", "jackson")
            .put("jetcache.remote.default.valueEncoder", "jackson")
            // 连接池中的最小空闲连接 默认 0
            .put("jetcache.remote.default.poolConfig.minIdle", 10)
            // 连接池中的最大空闲连接 默认 8
            .put("jetcache.remote.default.poolConfig.maxIdle", 10)
            // 连接池最大连接数
            .put("jetcache.remote.default.poolConfig.maxTotal", 20);
    }

    /**
     * Gets name *
     *
     * @return the name
     * @since 1.0.0
     */
    @Override
    public String getName() {
        return "fkh-starter-cache";
    }
}
