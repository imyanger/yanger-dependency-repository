package com.yanger.starter.basic.constant;

import com.yanger.starter.basic.enums.ApplicationType;

import org.springframework.stereotype.Component;

/**
 * @Description 应用参数
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Component
public class App {

    /** applicationClassName */
    public static String applicationClassName;

    /** applicationClassName */
    public static String applicationName;

    /** applicationClass */
    public static Class<?> applicationClass;

    /** applicationType */
    public static ApplicationType applicationType = ApplicationType.deduceFromClasspath();

    /** 启动方式, 用于区分是否通过 server.sh 脚本启动 */
    public static String applicationStartType = App.class.getResource("").getProtocol();

    /** 启动标识, 所有项目只能使用 BasicApplication 启动 */
    public static String applicationStarterFlag;

    /** 项目地址 */
    public static String serverUrl;

    public class Const {

        /** file 启动 */
        public static final String START_FILE = "file";

        /** jar 启动 */
        public static final String START_JAR = "jar";

        /** JUNIT_FLAG */
        public static final String JUNIT_FLAG = "test-classes";

        /** spring cloud 启动配置文件名 */
        public static final String CLOUD_CONFIG_FILE_NAME = "bootstrap.yml";

        /** spring boot 启动配置文件名 */
        public static final String BOOT_CONFIG_FILE_NAME = "application.yml";

        /** spring boot 启动配置文件名 */
        public static final String BOOT_CONFIG_ACTIVE_FILE_NAME = "application-{}.yml";

    }

}
