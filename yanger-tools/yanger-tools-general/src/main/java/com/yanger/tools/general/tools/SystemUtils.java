package com.yanger.tools.general.tools;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * 系统工具类
 * @Author yanger
 * @Date 2020/12/22 10:53
 */
@UtilityClass
public class SystemUtils extends org.apache.commons.lang3.SystemUtils {

    /** 获取 user home */
    @Nullable
    public static final String USER_HOME = getProperty("user.home");

    /** 获取用户地址 */
    @Nullable
    public static final String USER_DIR = getProperty("user.dir");

    /** 获取用户名 */
    @Nullable
    public static final String USER_NAME = getProperty("user.name");

    /** os 名 */
    @Nullable
    public static final String OS_NAME = getProperty("os.name");

    /**
     * 优先获取 JVM 参数, 然后才是系统环境变量
     * @param property
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 22:49
     */
    @Nullable
    public static String getProperty(String property) {
        String value = System.getProperty(property);
        return StringUtils.isBlank(value) ? System.getenv(property) : value;
    }

    /**
     * 是否 linux
     * @return {@link boolean}
     * @Author yanger
     * @Date 2022/01/21 22:49
     */
    public static boolean isLinux() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
    }

    /**
     * 是否 mac
     * @return {@link boolean}
     * @Author yanger
     * @Date 2022/01/21 22:49
     */
    @Contract(pure = true)
    public static boolean isMac() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_MAC;
    }

    /**
     * 是否 windows
     * @return {@link boolean}
     * @Author yanger
     * @Date 2022/01/21 22:49
     */
    @Contract(pure = true)
    public static boolean isWindows() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
    }

    public enum OsType {
        LINUX, MAC, WINDOWS
    }

}
