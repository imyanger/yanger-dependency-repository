package com.yanger.starter.basic.util;

import com.google.common.collect.Maps;

import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.exception.PropertiesException;
import com.yanger.starter.basic.yml.YmlPropertyLoaderFactory;
import com.yanger.tools.web.exception.BasicException;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Description 全局配置工具类, 用于获取整个应用的配置
 *     生命周期:
 *     1. 初始化 {@link #init(ConfigurableEnvironment)}
 *     2. 运行期间 (特指应用已经启动完成, 输出了 start finished 之后)
 *     此工具类中的环境在系统环境准备完成后, 初始化日志配置之前初始化.
 *     因此在这之前, 通过此工具类获取配置都将直接从 Java 环境变量中获取 {@link #getProperty(String)}.
 * @Author yanger
 * @Date 2020/12/29 18:08
 */
public class ConfigKit {

    /** environment */
    private static ConfigurableEnvironment environment;

    /** 默认的配置文件解析器 */
    private static final PropertySourceFactory DEFAULT_PROPERTY_SOURCE_FACTORY = new DefaultPropertySourceFactory();

    /** 自定义配置 */
    private static final Map<String, Object> CONSUMER_MAP = Maps.newHashMapWithExpectedSize(16);

    /** yaml 类型的配置文件 */
    public static final String YAML_FILE_EXTENSION = "yml";

    /** spring cloud 启动配置文件名 */
    public static final String CLOUD_CONFIG_FILE_NAME = "bootstrap.yml";

    /** spring boot 启动配置文件名 */
    public static final String BOOT_CONFIG_FILE_NAME = "application.yml";

    /** properties 类型的配置文件 */
    public static final String PROPERTIES_FILE_EXTENSION = "properties";

    /** JUNIT_FLAG */
    private static final String JUNIT_FLAG = "test-classes";

    /**
     * 在初始化日志配置之后执行
     * 1. 从默认配置 map 中获取配置
     * 2. 解析 environment
     * 3. 缓存到 ctxPropertiesMap
     * 使用时, 优先从 map 缓存中取, 然后才是 environment {@link #getProperty(String)}
     *
     * @param env the environment
     * @since 1.0.0
     */
    public static void init(@NotNull ConfigurableEnvironment env) {
        ConfigKit.environment = env;
    }

    /**
     * 优先获取本地维护的配置, 因为会动态刷新, 然后才是 environment, 最后是环境变量
     *
     * @param key the key
     * @return the property
     * @since 1.0.0
     */
    public static String getProperty(String key) {
        return getProperty(environment, key);
    }

    /**
     * Gets property *
     *
     * @param environment environment
     * @param key         key
     * @return the property
     * @since 1.5.0
     */
    public static String getProperty(ConfigurableEnvironment environment, String key) {
        String value;
        if (environment != null) {
            value = environment.getProperty(key);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        } else {
            return System.getProperty(key);
        }
        return value;
    }

    /**
     * 设置 JVM 环境变量
     *
     * @param key   key
     * @param value value
     * @since 1.0.0
     */
    public static void setSystemProperties(String key, String value) {
        System.setProperty(key, value);
    }

    /**
     * 获取启动模式
     * 本地开发模式还是服务器部署模式
     *
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    public static Boolean isLocalLaunch() {
        return !notLocalLaunch();
    }

    /**
     * 非本地开发环境 (只要 start.type = shell 都认为是非开发环境)
     *
     * @return the boolean
     * @since 1.0.0
     */
    @NotNull
    public static Boolean notLocalLaunch() {
        return App.START_SHELL.equals(System.getProperty(App.START_TYPE));
    }

    /**
     * 获取配置文件路径, 兼容本地开发和 jar 运行
     * -D 参数使用 Systerm.getProperty 获取
     * <code>
     * nohup ${JAVA_HOME}/bin/java -jar \
     * ${JVM_OPTS} \
     * -Ddeploy.path=${DEPLOY_DIR} \
     * -Dstart.type=shell \
     * ${DEBUG_OPTS} \
     * ${JAR_FILE} \
     * --spring.profiles.active=${ENV} \
     * --spring.config.location=${DEPLOY_DIR}/config/ &
     * </code>
     *
     * @return the config path
     * @since 1.0.0
     */
    public static @NotNull String getConfigPath() {
        String configPath;
        String startType = System.getProperty(App.START_TYPE);
        // 脚本启动
        if (StringUtils.isNotBlank(startType) && App.START_SHELL.equals(startType)) {
            // 获取 config 路径
            configPath = System.getProperty(App.APP_CONFIG_PATH);
            // 这种情况是本地开发时设置了 -Dstart.path 参数的情况
            if (StringUtils.isBlank(configPath)) {
                configPath = AppStartUtils.getClasspath();
            }
        } else {
            // 本地运行
            configPath = AppStartUtils.getClasspath();
        }
        if (!configPath.endsWith(File.separator)) {
            configPath += File.separator;
        }
        return configPath;
    }

    /**
     * Get property source property source
     *
     * @param configFileName config file name
     * @return the property source
     * @since 1.0.0
     */
    @NotNull
    public static PropertySource<?> getPropertySource(@NotNull String configFileName) {
        String configPath = ConfigKit.getConfigPath();
        String propertiesPath = configPath + configFileName;

        if (configFileName.endsWith(YAML_FILE_EXTENSION)) {
            try {
                return YmlPropertyLoaderFactory.createPropertySource(propertiesPath);
            } catch (IOException e) {
                // 如果单元测试时在 test-classes 下不存在 configFileName 配置, 则查找 target/classes 下的 configFileName 配置
                if (!configPath.contains(JUNIT_FLAG)) {
                    throw new PropertiesException("classpath 下不存在 " + configFileName);
                }
                configPath = configPath.replace(JUNIT_FLAG, "classes");
                try {
                    return YmlPropertyLoaderFactory.createPropertySource(configPath + configFileName);
                } catch (Exception ex) {
                    // 如果单元测试时在 test-classes 下不存在 configFileName 配置, 则查找 target/classes 下的 configFileName 配置
                    throw new PropertiesException("classpath 下不存在 " + configFileName);
                }
            } catch (Exception e) {
                throw new PropertiesException(e);
            }
        } else if (configFileName.endsWith(PROPERTIES_FILE_EXTENSION)) {
            String resolvedLocation = environment.resolveRequiredPlaceholders(configFileName);
            Resource resource = new DefaultResourceLoader().getResource(resolvedLocation);
            try {
                return DEFAULT_PROPERTY_SOURCE_FACTORY.createPropertySource("", new EncodedResource(resource, StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new BasicException("未找到文件: [{}]", configFileName);
            }
        } else {
            throw new BasicException("不支持的配置文件类型: [{}]", configFileName);
        }
    }

    /**
     * 是否为 debug 模式
     *
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isDebugModel() {
        String debugModel = System.getProperty(App.DEBUG_MODEL);
        return StringUtils.isNotBlank(debugModel) && (Boolean.parseBoolean(debugModel) || App.DEBUG_MODEL.equalsIgnoreCase(debugModel));
    }

}
