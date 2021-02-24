package com.yanger.starter.nacos.spi;

import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.starter.basic.util.ConfigKit;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.web.support.ChainMap;
import com.yanger.tools.web.tools.NetUtils;
import com.yanger.tools.web.tools.ObjectUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description nacos 加载默认配置
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
@AutoService(LauncherInitiation.class)
public class NacosLauncherInitiation implements LauncherInitiation {

    private static final String DEFAULT_GROUP = "YANGER_DEFAULT_GROUP";

    /**
     * Launcher *
     *
     * @param env           env
     * @param appName       app name
     * @return the map
     */
    @Override
    public Map<String, Object> launcher(@NotNull Environment env, String appName) {
        PropertySource<?> propertySource = ConfigKit.getPropertySource(App.Const.CLOUD_CONFIG_FILE_NAME);

        ChainMap chainMap = ChainMap.build(8)
            .put(ConfigKey.NacosConfigKey.DISCOVERY_IP, NetUtils.getLocalHost())

            .put(ConfigKey.NacosConfigKey.CONFIG_FILE_EXTENSION, App.Const.YAML_FILE_EXTENSION)
            .put(ConfigKey.NacosConfigKey.CONFIG_ENCODE, StringPool.UTF_8);

        String nameSpace = env.getProperty(ConfigKey.NacosConfigKey.NAMESPACE);
        String address = env.getProperty(ConfigKey.NacosConfigKey.ADDRESS);

        if (StringUtils.isNotBlank(nameSpace)) {
            chainMap.put(ConfigKey.NacosConfigKey.CONFIG_NAMESPACE, nameSpace)
                .put(ConfigKey.NacosConfigKey.DISCOVERY_NAMESPACE, nameSpace);
        }
        if (StringUtils.isNotBlank(address)) {
            chainMap.put(ConfigKey.NacosConfigKey.CONFIG_SERVER_ADDRESS, address)
                .put(ConfigKey.NacosConfigKey.DISCOVERY_SERVER_ADDRESS, address);
        }

        this.processGroup(propertySource, chainMap);
        return chainMap;
    }

    /**
     * 处理 group
     * 1. 加载默认的 spring.cloud.nacos.config.group 和 spring.cloud.nacos.discovery.group;
     * 2. 相同的 discovery.group 下的服务才能通信, 如果未设置则默认为 DEFAULT_GROUP;
     * 3. 如果配置了 yanger.nacos.group, 则 config 和 discovery 都设置为此配置;
     *
     * @param propertySource property source
     * @param chainMap       chain map
     * @since 1.0.0
     */
    public void processGroup(@NotNull PropertySource<?> propertySource, ChainMap chainMap) {

        Object configGroup = propertySource.getProperty(ConfigKey.NacosConfigKey.CONFIG_GROUP);
        if (this.notBlank(configGroup)) {
            chainMap.put(ConfigKey.NacosConfigKey.CONFIG_GROUP, String.valueOf(configGroup));
        }

        Object discoveryGroup = propertySource.getProperty(ConfigKey.NacosConfigKey.DISCOVERY_GROUP);
        if (this.notBlank(discoveryGroup)) {
            chainMap.put(ConfigKey.NacosConfigKey.DISCOVERY_GROUP, String.valueOf(discoveryGroup));
        } else {
            chainMap.put(ConfigKey.NacosConfigKey.DISCOVERY_GROUP, DEFAULT_GROUP);
        }

        // 如果配置了 yanger.nacos.group, 则 config 和 discovery 都设置为此配置
        Object customGroup = propertySource.getProperty(ConfigKey.NacosConfigKey.GROUP);
        if (this.notBlank(customGroup)) {
            chainMap.put(ConfigKey.NacosConfigKey.CONFIG_GROUP, String.valueOf(customGroup));
            chainMap.put(ConfigKey.NacosConfigKey.DISCOVERY_GROUP, String.valueOf(customGroup));
        }

    }

    /**
     * Not blank
     *
     * @param object object
     * @return the boolean
     * @since 1.0.0
     */
    private boolean notBlank(Object object) {
        return ObjectUtils.isNotNull(object)
               && StringUtils.isNotBlank(String.valueOf(object));
    }

    /**
     * Gets order *
     *
     * @return the order
     * @since 1.0.0
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 102;
    }

    /**
     * Gets name *
     *
     * @return the name
     * @since 1.0.0
     */
    @Override
    public String getName() {
        return "nacos-spring-boot-starter:NacosLauncherInitiation";
    }

}
