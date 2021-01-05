package com.yanger.tools.general.tools;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.UtilityClass;

/**
 * @Description 系统工具类
 * @Author yanger
 * @Date 2020/12/22 10:53
 */
@UtilityClass
public class SystemUtils extends org.apache.commons.lang3.SystemUtils {

    /** NAMESPACE */
    public static final String USER_NAMESPACE = "user.namespace";

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

    /** 在启动参数中直接修改 namespace, 优先级最高 */
    @Nullable
    public static final String USER_NAMESPACE_VALUE = getProperty(USER_NAMESPACE);

    /**
     * 优先获取 JVM 参数, 然后才是系统环境变量
     *
     * @param property property
     * @return the system property
     */
    @Nullable
    public static String getProperty(String property) {
        String value = System.getProperty(property);
        return StringUtils.isBlank(value) ? System.getenv(property) : value;
    }

    /**
     * Is linux boolean
     *
     * @return boolean boolean
     */
    public static boolean isLinux() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
    }

    /**
     * Is mac boolean
     *
     * @return the boolean
     */
    @Contract(pure = true)
    public static boolean isMac() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_MAC;
    }

    /**
     * Is windows boolean
     *
     * @return the boolean
     */
    @Contract(pure = true)
    public static boolean isWindows() {
        return org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
    }

    public enum OsType {
        /** Linux os type */
        LINUX,

        /** Mac os type */
        MAC,

        /** Windows os type */
        WINDOWS
    }

}
