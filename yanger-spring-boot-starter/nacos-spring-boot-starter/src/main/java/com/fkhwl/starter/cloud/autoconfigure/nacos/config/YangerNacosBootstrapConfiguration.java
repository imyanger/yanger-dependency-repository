package com.fkhwl.starter.cloud.autoconfigure.nacos.config;

import com.alibaba.cloud.nacos.NacosConfigBootstrapConfiguration;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.client.NacosPropertySourceLocator;
import com.alibaba.nacos.api.config.ConfigService;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.util.ConfigKit;
import com.yanger.tools.general.constant.Charsets;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.web.tools.IoUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.cloud.bootstrap.config.PropertySourceBootstrapConfiguration;
import org.springframework.cloud.bootstrap.config.PropertySourceBootstrapProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.util.Map;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 在 NacosConfigBootstrapConfiguration 之前执行 </p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.05.21 16:38
 * @see NacosConfigBootstrapConfiguration
 * @see PropertySourceBootstrapConfiguration#initialize(org.springframework.context.ConfigurableApplicationContext)
 * @since 1.4.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(NacosConfigBootstrapConfiguration.class)
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", matchIfMissing = true)
public class YangerNacosBootstrapConfiguration {

    /** STRING_STRING_MAP */
    private static final Bindable<Map<String, String>> STRING_STRING_MAP = Bindable.mapOf(String.class, String.class);

    /**
     * Fkh nacos bootstrap configuration
     *
     * @since 1.5.0
     */
    public YangerNacosBootstrapConfiguration() {
        log.debug("加载 Nacos 配置处理器: {}", YangerNacosBootstrapConfiguration.class.getName());
    }

    /**
     * Nacos config properties
     *
     * @return the nacos config properties
     * @since 1.5.0
     */
    @Bean
    @ConditionalOnMissingBean
    public NacosConfigProperties nacosConfigProperties() {
        return new NacosConfigProperties();
    }

    /**
     * Nacos property source locator
     *
     * @param nacosConfigProperties nacos config properties
     * @return the nacos property source locator
     * @since 1.4.0
     */
    // @Bean
    // @Primary
    // @SneakyThrows
    // public NacosPropertySourceLocator nacosPropertySourceLocator(@NotNull NacosConfigProperties nacosConfigProperties) {
    //     // log.info("Nacos DNS Cache: {} -> {}: {}",
    //     //          nacosConfigProperties.getServerAddr(),
    //     //          InetAddress.getByName(ConfigDefaultValue.NACOS_HOST).getHostAddress(),
    //     //          DnsCacheManipulator.getDnsCache(ConfigDefaultValue.NACOS_HOST));
    //     return new FkhNacosPropertySourceLocator(new NacosConfigManager(nacosConfigProperties));
    // }

    /**
     * <p>Company: 成都返空汇网络技术有限公司 </p>
     * <p>Description: 在父类之前执行 </p>
     *
     * @author dong4j
     * @version 1.4.0
     * @email "mailto:dong4j@fkhwl.com"
     * @date 2020.05.21 16:56
     * @since 1.4.0
     */
    @Order(-1)
    public class FkhNacosPropertySourceLocator extends NacosPropertySourceLocator {
        /** Nacos config properties */
        private final NacosConfigProperties nacosConfigProperties;
        /** Config service */
        private final ConfigService configService;
        /** formatter:off 写入到 nacos 主配置, 本地开发时的配置优先于 nacos 配置 */
        private static final String CONFIG_OVERRIDE = "# 本地配置优先于 Nacos 配置(主要用于本地开发时使用)\n"
                                                      + "spring:\n"
                                                      + "  cloud:\n"
                                                      + "    config:\n"
                                                      + "      override-none: true\n\n";

        /** formatter:on 本地配置的默认名 */
        private static final String LOCAL_CONFIG_NAME = "application";

        /**
         * Fkh nacos property source locator
         *
         * @param nacosConfigManager nacos config manager
         * @since 1.4.0
         */
        FkhNacosPropertySourceLocator(NacosConfigManager nacosConfigManager) {
            super(nacosConfigManager);
            this.configService = nacosConfigManager.getConfigService();
            this.nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
        }

        /**
         * 在获取 nacos 配置之前, 向 nacos 写入当前应用的配置, 避免每次手动去 naocs 创建应用配置
         *
         * @param environment environment
         * @return the property source
         * @see PropertySourceBootstrapConfiguration#initialize PropertySourceBootstrapConfiguration#initialize
         * @since 1.4.0
         */
        @Override
        public PropertySource<?> locate(@NotNull Environment environment) {
            if (null == this.configService) {
                log.error("no instance of config service found, can't load config from nacos");
                return null;
            }

            NacosProperties nacosProperties = new NacosProperties();
            Binder.get(environment).bind(NacosProperties.PREFIX, Bindable.ofInstance(nacosProperties));

            // 是否优先从 Nacos 中读取配置 (默认为 true)
            if (nacosProperties.getEnableNacosConfig() == null) {
                nacosProperties.setEnableNacosConfig(true);
            }
            // 是否自动向 nacos 创建配置 (默认为 false)
            if (nacosProperties.getEnableAutoCreateConfig() == null) {
                nacosProperties.setEnableAutoCreateConfig(false);
            }

            if (!nacosProperties.getEnableAutoCreateConfig()) {
                log.warn("[{}=false], 关闭自动创建 Nacos 配置", ConfigKey.NacosConfigKey.ENABLE_AUTO_CREATE_CONFIG);
            } else {
                // 自动创建配置
                this.autoCreateNacosConfig(environment);
            }

            if (!nacosProperties.getEnableNacosConfig()) {
                log.warn("[{}=false], 未加载 Nacos 配置, 将使用本地配置", ConfigKey.NacosConfigKey.ENABLE_NACOS_CONFIG);
                return null;
            }

            // 加载 Nacos 配置
            PropertySource<?> propertySource = super.locate(environment);
            Environment newEnvironment = this.environment(propertySource);

            this.checkNacosConfig(newEnvironment);
            return propertySource;
        }

        /**
         * 检查当前应用的 Nacos 是否配置了 spring.cloud.config.override-none, 如果为 true 将优先使用 本地配置
         *
         * @param newEnvironment new environment
         * @since 1.5.0
         */
        private void checkNacosConfig(Environment newEnvironment) {
            PropertySourceBootstrapProperties remoteProperties = new PropertySourceBootstrapProperties();
            Binder.get(newEnvironment).bind("spring.cloud.config",
                                            Bindable.ofInstance(remoteProperties));

            log.warn(remoteProperties.isOverrideNone()
                     ? "Nacos: [{}={}], 优先使用本地配置"
                     : "Nacos: [{}={}], 优先使用 Nacos 配置",
                     ConfigKey.NacosConfigKey.CONFIG_OVERRIDE_NONE,
                     remoteProperties.isOverrideNone());
        }

        /**
         * Environment
         *
         * @param composite composite
         * @return the environment
         * @since 1.5.0
         */
        private @NotNull Environment environment(PropertySource<?> composite) {
            MutablePropertySources incoming = new MutablePropertySources();
            incoming.addFirst(composite);

            StandardEnvironment environment = new StandardEnvironment();
            for (PropertySource<?> source : environment.getPropertySources()) {
                environment.getPropertySources().remove(source.getName());
            }
            for (PropertySource<?> source : incoming) {
                environment.getPropertySources().addLast(source);
            }
            return environment;
        }

        /**
         * 当 Nacos 没有当前应用配置时自动创建.
         *
         * @param environment environment
         * @since 1.5.0
         */
        private void autoCreateNacosConfig(@NotNull Environment environment) {
            String fileExtension = this.nacosConfigProperties.getFileExtension();
            String nacosGroup = this.nacosConfigProperties.getGroup();
            String namespace = this.nacosConfigProperties.getNamespace();

            String name = this.getName(environment);
            String dataId = name + StringPool.DOT + fileExtension;
            String primaryConfig = LOCAL_CONFIG_NAME + StringPool.DOT + fileExtension;
            // 主配置
            this.publishConfig(namespace, nacosGroup, dataId, true, primaryConfig);

            for (String profile : environment.getActiveProfiles()) {
                dataId = name + StringPool.DASH + profile + StringPool.DOT + fileExtension;
                primaryConfig = LOCAL_CONFIG_NAME + StringPool.DASH + profile + StringPool.DOT + fileExtension;
                // 环境配置
                this.publishConfig(namespace, nacosGroup, dataId, false, primaryConfig);
            }
        }

        /**
         * 直接通过 nacos API 推送应用配置, 如果存在则不创建.
         * 注意: 创建的配置可能存在相同的配置, 需要开发者手动去修改.
         *
         * @param nacosGroup      nacos group
         * @param dataId          data id
         * @param primary         primary
         * @param localConfigName local config name
         * @since 1.5.0
         */
        private void publishConfig(String namespace, String nacosGroup, String dataId, boolean primary, String localConfigName) {
            try {
                long timeout = this.nacosConfigProperties.getTimeout();
                String data = this.configService.getConfig(dataId, nacosGroup, timeout);
                if (StringUtils.isEmpty(data)) {
                    String finalContent = null;

                    Resource resource = ConfigKit.getResource(localConfigName);
                    String originalConfig = IoUtils.copyToString(resource.getInputStream(), Charsets.UTF_8);

                    if (primary) {
                        finalContent = CONFIG_OVERRIDE;
                        PropertySource<?> propertySource = ConfigKit.getPropertySource(localConfigName);
                        // 如果本地主配置没有配置 spring.cloud.config.override-none=true 则添加到本地配置然后写入到 Nacos 配置
                        if (propertySource.getProperty(ConfigKey.NacosConfigKey.CONFIG_OVERRIDE_NONE) == null) {
                            log.info("{}", propertySource.toString());
                            finalContent += originalConfig;
                        } else {
                            finalContent = originalConfig;
                        }
                    }

                    this.configService.publishConfig(dataId, nacosGroup, finalContent);
                    log.info("创建 {}:{}:{} 初始化配置: \n{}", namespace, dataId, nacosGroup, finalContent);
                }
            } catch (Exception e) {
                log.warn("自动创建 {} 配置失败: [{}]", dataId, e.getMessage());
            }
        }

        /**
         * 应用名
         *
         * @param environment environment
         * @return the name
         * @since 1.5.0
         */
        private String getName(Environment environment) {
            String name = this.nacosConfigProperties.getName();

            String dataIdPrefix = this.nacosConfigProperties.getPrefix();
            if (StringUtils.isEmpty(dataIdPrefix)) {
                dataIdPrefix = name;
            }

            if (StringUtils.isEmpty(dataIdPrefix)) {
                dataIdPrefix = environment.getProperty(ConfigKey.SpringConfigKey.APPLICATION_NAME);
            }

            return dataIdPrefix;
        }
    }

}
