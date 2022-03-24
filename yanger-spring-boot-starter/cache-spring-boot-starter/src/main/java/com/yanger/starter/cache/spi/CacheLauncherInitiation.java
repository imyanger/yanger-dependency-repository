package com.yanger.starter.cache.spi;

import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.tools.web.support.ChainMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * 默认配置
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@AutoService(LauncherInitiation.class)
public class CacheLauncherInitiation implements LauncherInitiation {

    /** REDIS_AUTO_CONFIGURATION */
    public static final String REDIS_AUTO_CONFIGURATION = "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration";

    /**
     * 启动之前关闭默认的 RedisAutoConfiguration 自动装配
     */
    @Override
    public void advance(String appName) {
        LOG.warn("默认的 {} 已被禁用", REDIS_AUTO_CONFIGURATION);
        String property = System.getProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE);

        String value;
        if (StringUtils.isBlank(property)) {
            value = REDIS_AUTO_CONFIGURATION;

        } else {
            value = String.join(",", property, REDIS_AUTO_CONFIGURATION);
        }
        System.setProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE, value);

    }

    /**
     * 加载默认配置
     * @param env               系统变量 Environment
     * @param appName           服务名
     * @return the chain map
     */
    @Override
    public Map<String, Object> launcher(Environment env, String appName) {

        return ChainMap.build(16)
            .put(ConfigKey.JetcacheConfigKey.STAT_INTERVAL_MINUTES, 30)
            .put(ConfigKey.JetcacheConfigKey.AREA_IN_CACHE_NAME, false)
            .put(ConfigKey.JetcacheConfigKey.HIDE_PACKAGES, App.Const.BASE_PACKAGES)
            // 缓存类型. tair、redis 为当前支持的远程缓存; linkedhashmap、caffeine 为当前支持的本地缓存类型
            .put(ConfigKey.JetcacheConfigKey.LOCAL_DEFAULT_TYPE, "caffeine")
            // 每个缓存实例的最大元素的全局配置, 仅 local 类型的缓存需要指定. 注意是每个缓存实例的限制, 而不是全部,
            // 比如这里指定 1000, 然后用 @CreateCache 创建了两个缓存实例（并且注解上没有设置 localLimit 属性）, 那么每个缓存实例的限制都是 100
            .put(ConfigKey.JetcacheConfigKey.LOCAL_DEFAULT_LIMIT, 1000)
            // key 转换器的全局配置, 当前只有一个已经实现的 keyConvertor: fastjson.
            // 仅当使用 @CreateCache 且缓存类型为 LOCAL 时可以指定为 none, 此时通过 equals 方法来识别 key. 方法缓存必须指定 keyConvertor
            .put(ConfigKey.JetcacheConfigKey.LOCAL_DEFAULT_KEY_CONVERTOR, "fastjson")
            .put(ConfigKey.JetcacheConfigKey.REMOTE_DEFAULT_KEY_CONVERTOR, "fastjson")
            .put(ConfigKey.JetcacheConfigKey.REMOTE_DEFAULT_VALUE_DECODER, "jackson")
            .put(ConfigKey.JetcacheConfigKey.REMOTE_DEFAULT_VALUE_ENCODER, "jackson")
            // 连接池中的最小空闲连接 默认 0
            .put(ConfigKey.JetcacheConfigKey.POOL_CONFIG_MIN_IDLE, 10)
            // 连接池中的最大空闲连接 默认 8
            .put(ConfigKey.JetcacheConfigKey.POOL_CONFIG_MAX_IDLE, 10)
            // 连接池最大连接数
            .put(ConfigKey.JetcacheConfigKey.POOL_CONFIG_MAX_TOTAL, 20);
    }

    @Override
    public String getName() {
        return "cache-spring-boot-starter:CacheLauncherInitiation";
    }

}
