package com.fkhwl.starter.cloud.nacos.spi;



import com.yanger.starter.basic.annotation.AutoService;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigDefaultValue;
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
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: cloud 加载默认配置 </p>
 * todo-dong4j : (2019年10月22日 8:47 下午) [使用 {@link org.springframework.boot.env.YamlPropertySourceLoader} 重构]
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 02:23
 * @since 1.0.0
 */
@Slf4j
@AutoService(LauncherInitiation.class)
public class CloudNacosLauncherInitiation implements LauncherInitiation {

    /** GROUP */
    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    /**
     * Launcher *
     *
     * @param env           env
     * @param appName       app name
     * @return the map
     * @since 1.0.0
     */
    @Override
    public Map<String, Object> launcher(@NotNull Environment env, String appName) {
        PropertySource<?> propertySource = ConfigKit.getPropertySource(ConfigKit.CLOUD_CONFIG_FILE_NAME);

        ChainMap chainMap = ChainMap.build(8)
            .put(ConfigKey.NacosConfigKey.DISCOVERY_IP, NetUtils.getLocalHost())

            .put(ConfigKey.NacosConfigKey.CONFIG_FILE_EXTENSION, ConfigKit.YAML_FILE_EXTENSION)
            .put(ConfigKey.NacosConfigKey.CONFIG_ENCODE, StringPool.UTF_8)
            .put(ConfigKey.NacosConfigKey.CONFIG_NAMESPACE, App.FKH_NAME_SPACE)

            .put(ConfigKey.NacosConfigKey.CONFIG_SERVER_ADDRESS, ConfigDefaultValue.NACOS_SERVER)
            .put(ConfigKey.NacosConfigKey.DISCOVERY_SERVER_ADDRESS, ConfigDefaultValue.NACOS_SERVER)
            .put(ConfigKey.NacosConfigKey.DISCOVERY_NAMESPACE, App.FKH_NAME_SPACE);

        this.processGroup(propertySource, chainMap);
        return chainMap;
    }

    /**
     * 处理 group
     * 1. 优先使用 spring.cloud.nacos.config.group 和 spring.cloud.nacos.discovery.group;
     * 2. 相同的 discovery.group 下的服务才能通信, 如果未设置则默认为 DEFAULT_GROUP;
     * 3. 如果只配置了 fkh.app.group, 则 config 和 discovery 都设置为此配置;
     * 4. 如果配置了 fkh.app.config-group, 则将此配置设置为 spring.cloud.nacos.config.group;
     * 5. 如果配置了 fkh.app.discovery-group, 则将此配置设置为 spring.cloud.nacos.discovery.group;
     *
     * @param propertySource property source
     * @param chainMap       chain map
     * @since 1.0.0
     */
    public void processGroup(@NotNull PropertySource<?> propertySource, ChainMap chainMap) {
        Object configGroup = propertySource.getProperty(ConfigKey.NacosConfigKey.CONFIG_GROUP);
        if (this.notBlank(configGroup)) {
            chainMap.put(ConfigKey.NacosConfigKey.CONFIG_GROUP, String.valueOf(configGroup));
            log.warn("可使用 [fkh.app.config-group] 代替 [spring.cloud.nacos.config.group] 配置.");
        }

        Object discoveryGroup = propertySource.getProperty(ConfigKey.NacosConfigKey.DISCOVERY_GROUP);
        if (this.notBlank(discoveryGroup)) {
            chainMap.put(ConfigKey.NacosConfigKey.DISCOVERY_GROUP, String.valueOf(discoveryGroup));
            log.warn("可使用 [fkh.app.discovery-group] 代替 [spring.cloud.nacos.discovery.group] 配置.");
        } else {
            chainMap.put(ConfigKey.NacosConfigKey.DISCOVERY_GROUP, DEFAULT_GROUP);
        }

        Object customGroup = propertySource.getProperty(ConfigKey.FKH_APP_GROUP);
        if (this.notBlank(customGroup)) {
            chainMap.put(ConfigKey.NacosConfigKey.CONFIG_GROUP, String.valueOf(customGroup));
            chainMap.put(ConfigKey.NacosConfigKey.DISCOVERY_GROUP, String.valueOf(customGroup));
        } else {
            Object customConfigGroup = propertySource.getProperty(ConfigKey.FKH_APP_CONFIG_GROUP);
            if (this.notBlank(customConfigGroup)) {
                chainMap.put(ConfigKey.NacosConfigKey.CONFIG_GROUP, String.valueOf(customConfigGroup));
            }

            Object customDiscoveryGroup = propertySource.getProperty(ConfigKey.FKH_APP_DISCOVERY_GROUP);
            if (this.notBlank(customDiscoveryGroup)) {
                chainMap.put(ConfigKey.NacosConfigKey.DISCOVERY_GROUP, String.valueOf(customDiscoveryGroup));
            }
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
        return "fkh-starter-cloud-nacos";
    }

}
