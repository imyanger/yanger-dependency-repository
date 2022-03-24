package com.yanger.starter.basic.util;

import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.tools.general.constant.StringPool;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.io.File;
import java.util.Objects;

/**
 * 应用启动工具类
 * @Author yanger
 * @Date 2020/12/29 18:16
 */
@Slf4j
@UtilityClass
public class AppStartUtils {

    /**
     * Get classpath.
     * @return the classpath
     */
    @NotNull
    public static String getClasspath() {
        String classPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        return classPath.replace(StringPool.SLASH, File.separator).replace("\\", File.separator);
    }

    /**
     * 向 MDC 中设置组件名, 当应用启动完成后根据相应模块名输出信息
     * @param componentName the componet name
     */
    public static void loadComponent(String componentName) {
        log.debug("初始化自动装配器: {}", componentName);
        String components = MDC.get(ConfigKey.SystemConfigKey.LIBRARY_NAME);
        String str = StringUtils.isNotBlank(components) ? componentName + StringPool.AT + components : componentName + StringPool.AT;
        MDC.put(ConfigKey.SystemConfigKey.LIBRARY_NAME, str);
    }

}
