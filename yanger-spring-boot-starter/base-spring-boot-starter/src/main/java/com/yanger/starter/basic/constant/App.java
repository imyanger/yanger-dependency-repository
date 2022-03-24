package com.yanger.starter.basic.constant;

import com.yanger.starter.basic.enums.ApplicationType;
import org.springframework.stereotype.Component;

/**
 * 应用参数
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

    /** 项目配置文件地址 */
    public static String configPath;

    public class Const {

        /** BASE_PACKAGES */
        public static final String BASE_PACKAGES = "com.yanger";

        /** file 启动 */
        public static final String START_FILE = "file";

        /** jar 启动 */
        public static final String START_JAR = "jar";

        /** JUNIT_FLAG */
        public static final String JUNIT_FLAG = "test-classes";

        /** yaml 类型的配置文件 */
        public static final String YAML_FILE_EXTENSION = "yml";

        /** spring cloud 启动配置文件名 */
        public static final String CLOUD_CONFIG_FILE_NAME = "bootstrap.yml";

        /** spring boot 启动配置文件名 */
        public static final String BOOT_CONFIG_FILE_NAME = "application.yml";

        /** spring boot 启动配置文件名 */
        public static final String BOOT_CONFIG_ACTIVE_FILE_NAME = "application-{}.yml";

        /** 用于打印 bean 信息 */
        public static final String DEBUG_MODEL = "debug";

    }

}
