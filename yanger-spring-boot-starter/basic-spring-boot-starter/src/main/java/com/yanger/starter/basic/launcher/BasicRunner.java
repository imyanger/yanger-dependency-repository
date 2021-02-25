package com.yanger.starter.basic.launcher;

import com.google.common.collect.Maps;

import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigDefaultValue;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.enums.ApplicationType;
import com.yanger.starter.basic.enums.SpringApplicationType;
import com.yanger.starter.basic.env.DefaultEnvironment;
import com.yanger.starter.basic.spi.LauncherInitiation;
import com.yanger.starter.basic.util.ConfigKit;
import com.yanger.starter.basic.util.ConvertUtils;
import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.starter.basic.util.YmlUtils;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.format.StringFormatter;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.util.Assert;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 基础启动
 * @Author yanger
 * @Date 2020/12/29 16:56
 */
@Slf4j
public final class BasicRunner {

    private static final Properties DEFAULT_PROPERTIES = new Properties();

    private static final Properties CUSTOM_PROPERTIES = new Properties();

    /**
     * 应用名处理, 如果未显式设置则使用启动目录名作为应用名
     *
     * @param source          the source
     * @param applicationType application type
     * @param args            the args
     * @return the configurable application context
     * @throws Exception exception
     */
    public static ConfigurableApplicationContext start(Class<?> source, ApplicationType applicationType, String... args) throws Exception {
        // 获取应用名称
        loadApplicationName();
        // 加载 args 参数
        loadArgsProperties(args);
        // 加载 yaml 文件配置
        loadYamlProperties();
        // 设置 BasicApplication 启动标识
        App.applicationStarterFlag = ConfigDefaultValue.APPLICATION_STARTER_FLAG;
        DEFAULT_PROPERTIES.setProperty(ConfigKey.UnmodifyConfigKey.APPLICATION_STARTER_FLAG, ConfigDefaultValue.APPLICATION_STARTER_FLAG);
        // 启动应用
        return start(App.applicationName, source, applicationType, args);
    }

    /**
     * @Description 设置参数中的参数
     * @Author yanger
     * @Date 2021/1/25 10:00
     * @param: args
     * @throws
     */
    private static void loadArgsProperties(String... args) {
        for (String arg : args) {
            if (arg.startsWith("--") && arg.contains("=")) {
                String[] keyValue = arg.replace("--", "").split("=");
                CUSTOM_PROPERTIES.setProperty(keyValue[0].trim(), keyValue[1].trim());
            } else if (arg.startsWith("-") && arg.contains("=")) {
                String[] keyValue = arg.replace("-", "").split("=");
                CUSTOM_PROPERTIES.setProperty(keyValue[0].trim(), keyValue[1].trim());
            }
        }
    }

    /**
     * 获取应用名
     * 1. 默认通过 jar 启动应用, 从 MANIFEST.MF 中获取 Project-Name 的值
     * 2. File 启动，spring.application.name ->文件名
     *
     * @return the properties
     */
    private static void loadApplicationName() {
        // 获取配置文件路径, 有多种情况 (本地运行, jar 运行)
        String configFilePath = ConfigKit.getConfigPath();
        String applicationName;
        // shell 脚本启动, 优先从 jar 的 MANIFEST.MF 读取
        if (ConfigKit.isJarStart()) {
            try {
                // 优先解析 jar 文件中的 MANIFEST.MF 文件, jar.file 环境变量通过 server.sh 启动脚本设置
                JarFile jarFile = new JarFile(System.getProperty("jar.file"));
                Manifest manifest = jarFile.getManifest();
                manifest.getMainAttributes().forEach((k, v) -> log.info("[{}:{}]", k, v));
                applicationName = manifest.getMainAttributes().getValue("Project-Name");
            } catch (Exception e) {
                // 如果在 IDE 中指定 start.type=shell 时(本地模拟部署时使用), 则会抛出 NPE 异常, 这时则通过解析 jar 来获取默认应用名
                log.warn("Launcher the app via shell in idea, can not get Project-Name, using jar name.");
                String startPath = BasicRunner.class.getResource(StringPool.SLASH).getPath().split("!")[0];
                // 非 jar 启动
                File file = new File(startPath);
                applicationName = file.getParentFile().getParentFile().getName();
            }
        } else {
            Object name = null;
            try {
                // 检查配置文件是否配置相关属性
                PropertySource<?> propertySource;
                SpringApplicationType type = SpringApplicationType.deduceFromClasspath();
                if (type == SpringApplicationType.CLOUD) {
                    // 如果是 cloud 应用, 解析 bootstrap.yml
                    propertySource = ConfigKit.getPropertySource(App.Const.CLOUD_CONFIG_FILE_NAME);
                } else {
                    // 如果是 boot 应用, 解析 application.yml
                    propertySource = ConfigKit.getPropertySource(App.Const.BOOT_CONFIG_FILE_NAME);
                }
                name = propertySource.getProperty(ConfigKey.SpringConfigKey.APPLICATION_NAME);
            } catch (Exception ignored) {}

            if (name != null) {
                applicationName = name.toString();
            } else {
                File file = new File(configFilePath);
                // 直接解析文件目录, 使用当前目录名作为应用名 (target 上一级目录)
                applicationName = file.getParentFile().getParentFile().getName();
                log.warn("未显式设置服务名 spring.application.name, 读取当前模块名作为服务名： [{}]", applicationName);
            }
        }
        // 环境变量是最高级别
        applicationName = System.getProperty(ConfigKey.SpringConfigKey.APPLICATION_NAME, applicationName);
        if (StringUtils.isNotBlank(applicationName)) {
            App.applicationName = applicationName;
        } else {
            App.applicationName = App.applicationClass.getSimpleName();
        }
    }

    /**
     * @Description 加载 yaml 文件配置
     * @Author yanger
     * @Date 2021/2/3 15:47
     * @throws
     */
    private static void loadYamlProperties() {
        try {
            SpringApplicationType type = SpringApplicationType.deduceFromClasspath();
            Map<String, Object> bootProperties = null;
            if(type == SpringApplicationType.BOOT) {
                bootProperties = YmlUtils.getYamlProperties(App.Const.BOOT_CONFIG_FILE_NAME);
            } else if (type == SpringApplicationType.CLOUD) {
                bootProperties = YmlUtils.getYamlProperties(App.Const.BOOT_CONFIG_FILE_NAME);
                Map<String, Object> cloudProperties = YmlUtils.getYamlProperties(App.Const.CLOUD_CONFIG_FILE_NAME);
                loadMapCustomProperties(cloudProperties);
            }
            loadMapCustomProperties(bootProperties);
            if (bootProperties != null && bootProperties.get(ConfigKey.SpringConfigKey.PROFILES_ACTIVE) != null) {
                String activeName = StringFormatter.format(App.Const.BOOT_CONFIG_ACTIVE_FILE_NAME, bootProperties.get(ConfigKey.SpringConfigKey.PROFILES_ACTIVE));
                Map<String, Object> activeProperties = YmlUtils.getYamlProperties(activeName);
                loadMapCustomProperties(activeProperties);
            }
        } catch (Exception ignore) {}
    }

    /**
     * @Description 添加map属性到自定义配置
     * @Author yanger
     * @Date 2021/2/3 14:53
     * @param: map
     * @throws
     */
    private static void loadMapCustomProperties(Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            map.forEach((key, value) -> CUSTOM_PROPERTIES.put(key, value));
        }
    }



    /**
     * 启动 Spring 应用程序
     * java -jar app.jar --spring.profiles.active=prod --server.port=2333
     *
     * @param appName         application name
     * @param source          The sources
     * @param applicationType application type
     * @param args            the args
     * @return an application context created from the current state
     * @throws Exception exception
     */
    public static ConfigurableApplicationContext start(String appName, Class<?> source, ApplicationType applicationType, String... args) throws Exception {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(appName, source, applicationType, args);
        builder.registerShutdownHook(true);
        return builder.run(args);
    }

    /**
     * 设置 默认配置和 profiles, 用过 SPI 加载其他包的组件
     *
     * @param appName         the app name
     * @param source          the source
     * @param applicationType application type
     * @param args            the args
     * @return the spring application builder
     * @throws Exception exception
     */
    @NotNull
    private static SpringApplicationBuilder createSpringApplicationBuilder(String appName, Class<?> source,
                                                                           @NotNull ApplicationType applicationType, String... args) throws Exception {
        loadPropertySource(appName, args);

        log.info("应用类型：ApplicationType = {}", applicationType.name());

        // 转换类型
        if (applicationType == ApplicationType.SERVICE) {
            applicationType = ApplicationType.NONE;
        }

        SpringApplicationBuilder builder = new SpringApplicationBuilder(source)
            .web(ConvertUtils.convert(applicationType.name(), org.springframework.boot.WebApplicationType.class))
            .main(source);
        builder.properties(DEFAULT_PROPERTIES);
        builder.properties(CUSTOM_PROPERTIES);

        return builder;
    }

    private static void loadPropertySource(String appName, String... args) {
        Assert.hasText(appName, "[spring.application.name] 服务名不能为空");

        // 读取环境变量,使用 spring boot 的规则 (获取系统参数和 JVM 参数)
        ConfigurableEnvironment environment = new DefaultEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource(args));

        // 添加到环境变量中
        putProperties();

        // 加载自定义 SPI 组件
        ServiceLoader<LauncherInitiation> loader = ServiceLoader.load(LauncherInitiation.class);
        List<LauncherInitiation> list = IteratorUtils.toList(loader.iterator());
        list.stream().sorted(Comparator.comparingInt(LauncherInitiation::getOrder))
            .forEach(launcherService -> launcherService.launcherWrapper(environment, DEFAULT_PROPERTIES, appName));

        propertySources.addLast(new MapPropertySource(DefaultEnvironment.DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME, getMapFromProperties(DEFAULT_PROPERTIES)));
        propertySources.addLast(new MapPropertySource(DefaultEnvironment.CUSTOM_PROPERTIES_PROPERTY_SOURCE_NAME, getMapFromProperties(CUSTOM_PROPERTIES)));

        // 打印输出配置
        outProperties();

        ConfigKit.init(environment);
    }

    /**
     * Properties -> map
     *
     * @param properties properties
     * @return the map from properties
     */
    private static @NotNull Map<String, Object> getMapFromProperties(@NotNull Properties properties) {
        Map<String, Object> map = Maps.newHashMap();
        for (Object key : Collections.list(properties.propertyNames())) {
            map.put((String) key, properties.get(key));
        }
        return map;
    }


    /**
     * @Description 将配置属性添加到系统环境变量System中
     * @Author yanger
     * @Date 2021/2/3 14:57
     * @throws
     */
    private static void putProperties() {
        DEFAULT_PROPERTIES.setProperty(ConfigKey.APPLICATION_CLASS_NAME, App.applicationClassName);
        DEFAULT_PROPERTIES.setProperty(ConfigKey.APPLICATION_NAME, App.applicationName);
        DEFAULT_PROPERTIES.setProperty(ConfigKey.APPLICATION_TYPE, App.applicationType.toString());
        DEFAULT_PROPERTIES.setProperty(ConfigKey.APPLICATION_START_TYPE, App.applicationStartType);
        // 添加到环境变量中
        DEFAULT_PROPERTIES.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
        CUSTOM_PROPERTIES.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
    }

    private static void outProperties() {
        log.info("全部的默认配置：\n{}", JsonUtils.toJson(DEFAULT_PROPERTIES, true));
        if (CUSTOM_PROPERTIES.getProperty(ConfigKey.SpringConfigKey.PROFILES_ACTIVE) != null) {
            log.info("当前 Spring 生效的环境变量：{}", CUSTOM_PROPERTIES.getProperty(ConfigKey.SpringConfigKey.PROFILES_ACTIVE));
        }
        log.info("全部的自定义配置：\n{}", JsonUtils.toJson(CUSTOM_PROPERTIES, true));
    }

}
