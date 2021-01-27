package com.yanger.starter.basic.constant;

/**
 * @Description 所有的配置常量
 * @Author yanger
 * @Date 2020/12/21 10:01
 */
public final class ConfigKey {

    /** applicationClassName */
    public static final String APPLICATION_CLASS_NAME = "yanger.app.class-name";

    /** applicationClassName */
    public static final String  APPLICATION_NAME = "yanger.app.name";

    /** applicationType */
    public static final String APPLICATION_TYPE = "yanger.app.type";

    /** 启动方式, 用于区分是否通过 server.sh 脚本启动 */
    public static final String APPLICATION_START_TYPE = "yanger.app.start-type";

    /** server.sh 中的启动参数, 用于获取配置文件路径 */
    public static final String APP_CONFIG_PATH = "yanger.app.config-path";

    /** Config name */
    public static final String CONFIG_NAME = "yanger.app.config-file-name";


    /** xss 配置项 */
    public static class XssConfigKey {

        /** XSS_ENABLE_XSS_FILTER */
        public static final String XSS_ENABLE_XSS_FILTER = "yanger.xss.enable-xss-filter";

        /** XSS_ENABLE_XSS_FILTER */
        public static final String XSS_EXCLUDE_PATTERNS = "yanger.xss.exclude-patterns";

    }


    /** json 配置项 */
    public static class JsonConfigKey {

        /** JSON_DATE_FORMAT */
        public static final String JSON_DATE_FORMAT = "yanger.json.date-formate";

        /** JSON_TIME_ZONE */
        public static final String JSON_TIME_ZONE = "yanger.json.time-zone";

    }


    /** spring 配置项 */
    public static class SpringConfigKey {

        /** APPLICATION_NAME */
        public static final String APPLICATION_NAME = "spring.application.name";

        /** PROFILES_ACTIVE */
        public static final String PROFILES_ACTIVE = "spring.profiles.active";

        /** SERVER_CONTEXT_PATH */
        public static final String SERVER_CONTEXT_PATH = "server.servlet.context-path";

    }

    /** token 配置项 */
    public static class TokenConfigKey {

        /** jwt token 有效时长（分钟） */
        public static final String TOKEN_AVAILABLE_TIME_MINUTE = "yanger.token.available-time-minute";

        /** jwt token 续期的剩余时长（分钟） */
        public static final String TOKEN_RENEWAL_TIME_MINUTE = "yanger.token.renewal-time-minute";

        /**  jwt header 中的标志 */
        public static final String TOKEN_HEADER_KEY = "yanger.token.login-token";

        /** request 中用户信息的属性 key */
        public static final String TOKEN_USER_KEY = "yanger.token.login-user";

    }


    /** 系统配置项 */
    public static class SystemConfigKey {

        /** 依赖的 library */
        public static final String LIBRARY_NAME = "used.librarys";

    }

    /** 不可设置的配置项 */
    public static class UnmodifyConfigKey {

        /** 启动标识, 所有项目只能使用 BasicApplication 启动 */
        public static final String APPLICATION_STARTER_FLAG = "yanger.app.starter-flag";

        /** 项目地址 */
        public static final String APPLICATION_SERVER_URL = "yanger.app.server-url";

    }

}
