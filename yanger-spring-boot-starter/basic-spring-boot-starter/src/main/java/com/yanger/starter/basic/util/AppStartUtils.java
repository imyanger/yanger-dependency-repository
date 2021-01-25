package com.yanger.starter.basic.util;

import com.yanger.tools.general.constant.StringPool;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 应用启动工具类
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

}
