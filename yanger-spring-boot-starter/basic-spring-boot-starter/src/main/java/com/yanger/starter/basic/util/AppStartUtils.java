package com.yanger.starter.basic.util;

import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.tools.general.constant.StringPool;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.io.*;
import java.util.Objects;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 应用启动工具类
 * @Author yanger
 * @Date 2020/12/29 18:16
 */
@Slf4j
@UtilityClass
public class AppStartUtils {

    /**
     * Gets classpath.
     *
     * @return the classpath
     */
    @NotNull
    public static String getClasspath() {
        String classPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        return classPath.replace(StringPool.SLASH, File.separator).replace("\\", File.separator);
    }

    /**
     * 向 MDC 中设置组件名, 当应用启动完成后根据相应模块名输出信息
     *
     * @param componetName the componet name
     */
    public static void loadComponent(String componetName) {
        log.debug("初始化自动装配器: {}", componetName);
        String components = MDC.get(ConfigKey.SystemConfigKey.LIBRARY_NAME);
        String str = StringUtils.isNotBlank(components) ? componetName + StringPool.AT + components : componetName + StringPool.AT;
        MDC.put(ConfigKey.SystemConfigKey.LIBRARY_NAME, str);
    }

}
