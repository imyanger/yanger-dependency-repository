package com.yanger.starter.basic.util;

import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.exception.PropertiesException;
import com.yanger.starter.basic.yml.YmlPropertyLoaderFactory;
import com.yanger.tools.web.exception.BasicException;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

    /** yaml 类型的配置文件 */
    private static final String YAML_FILE_EXTENSION = "yml";

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
     */
    public static void init(@NotNull ConfigurableEnvironment env) {
        ConfigKit.environment = env;
    }

    /**
     * 优先获取本地维护的配置, 因为会动态刷新, 然后才是 environment, 最后是环境变量
     *
     * @param key the key
     * @return the property
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
     * Gets int value.
     *
     * @param key          the key
     * @param defaultValue the default value
     * @return the int value
     */
    public static long getLongValue(String key, long defaultValue) {
        long i = getLongValue(key);
        return i == 0L ? defaultValue : i;
    }

    /**
     * Gets int value.
     *
     * @param key the key
     * @return the int value
     */
    public static long getLongValue(String key) {
        Object value = getProperty(key);
        if (value == null) {
            return 0L;
        } else {
            return DataTypeUtils.convert(long.class, value);
        }
    }

    /**
     * Get app name string.
     *
     * @return the string
     */
    public static String getAppName() {
        return getProperty(ConfigKey.SpringConfigKey.APPLICATION_NAME);
    }

    /**
     * 获取配置文件路径
     *
     * @return the config path
     */
    public static @NotNull String getConfigPath() {
        // 如果设置了配置文件路径，则使用配置的
        String appConfigPath = App.configPath;
        if (StringUtils.isNotBlank(appConfigPath)) {
            if (!appConfigPath.endsWith(File.separator)) {
                appConfigPath += File.separator;
            }
            return appConfigPath;
        }
        return getClasspath();
    }

    /**
     * 获取 classpath 路径
     *
     * @return the config path
     */
    public static @NotNull String getClasspath() {
        String configPath = AppStartUtils.getClasspath();
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
     */
    public static boolean isDebugModel() {
        String debugModel = System.getProperty(App.Const.DEBUG_MODEL);
        return StringUtils.isNotBlank(debugModel) && (Boolean.parseBoolean(debugModel) || App.Const.DEBUG_MODEL.equalsIgnoreCase(debugModel));
    }

    /**
     * 通过文件全路径名获取资源文件
     *
     * @param configFileName config file name
     * @return the resource
     */
    public static Resource getResource(String configFileName) {
        String configPath = ConfigKit.getConfigPath();
        String fullPathFileName = configPath + configFileName;
        Resource resource;
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(new File(fullPathFileName));
            resource = new InputStreamResource(inputStream);
        } catch (IOException ex) {
            throw new PropertiesException("未找到文件: [{}]", fullPathFileName);
        }
        return resource;
    }

    /**
     * 是否是jar启动
     * @Date 2021/2/25 9:53
     * @return: boolean
     */
    public static boolean isJarStart() {
        return App.Const.START_JAR.equals(App.applicationStartType);
    }

    /**
     * 是否本地启动
     * @Date 2021/2/25 9:53
     * @return: boolean
     */
    public static boolean isLocalLaunch() {
        return !isJarStart();
    }

}
