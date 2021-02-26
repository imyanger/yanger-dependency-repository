package com.yanger.starter.mybatis.spi;

import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.tools.web.support.ChainMap;

import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Properties;

/**
 * @Description 通过 SPI 加载 mybatis 默认配置
 * @Author yanger
 * @Date 2021/1/29 9:40
 */
@AutoService(LauncherInitiation.class)
public class MybatisLauncherInitiation implements LauncherInitiation {

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
            // mybatis-plus
            .put(ConfigKey.MybatisConfigKey.MAPPER_LOCATIONS, "classpath*:/mapper/**/*.xml,classpath*:/mybatis/**/*.xml,classpath*:/mybatis-plus/**/*.xml")
            .put(ConfigKey.MybatisConfigKey.CONFIGURATION_CALL_SETTERS_ON_NULLS, Boolean.TRUE)
            .put(ConfigKey.MybatisConfigKey.CONFIGURATION_CACHE_ENABLED, Boolean.TRUE)
            .put(ConfigKey.MybatisConfigKey.CONFIGURATION_MAP_UNDERSCORE_TO_CAMEL_CASE, Boolean.TRUE)
            // 逻辑删除配置, 逻辑已删除值, 逻辑未删除值(默认为 0)
            .put(ConfigKey.MybatisConfigKey.GLOBAL_LOGIC_DELETE_VALUE, 1)
            .put(ConfigKey.MybatisConfigKey.GLOBAL_LOGIC_NOT_DELETE_VALUE, 0)
            // 主键类型, 设置为自增, 要求 DDL 使用 auto_increment
            .put(ConfigKey.MybatisConfigKey.GLOBAL_ID_TYPE, "auto")
            .put(ConfigKey.MybatisConfigKey.GLOBAL_LOGIC_BANNER, "false");
    }

    /**
     * Gets order *
     *
     * @return the order
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 200;
    }

    /**
     * Gets name *
     *
     * @return the name
     */
    @Override
    public String getName() {
        return "mybatis-spring-boot-starter:MybatisLauncherInitiation";
    }

}
