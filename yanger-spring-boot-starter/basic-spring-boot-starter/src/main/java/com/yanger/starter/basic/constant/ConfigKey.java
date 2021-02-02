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


    /** xss 配置项 web-spring-boot-starter/src/main/java/com/yanger/starter/web/config/XssProperties.java */
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

        /** 排除的自动装配类 */
        public static final String AUTOCONFIGURE_EXCLUDE = "spring.autoconfigure.exclude";



    }

    /** token 配置项 web-spring-boot-starter/src/main/java/com/yanger/starter/web/config/TokenConfig.java */
    public static class TokenConfigKey {

        /** jwt token 有效时长（分钟） */
        public static final String TOKEN_AVAILABLE_TIME_MINUTE = "yanger.token.available-time-minute";

        /** jwt token 续期的剩余时长（分钟） */
        public static final String TOKEN_RENEWAL_TIME_MINUTE = "yanger.token.renewal-time-minute";

        /**  jwt header 中的标志 */
        public static final String TOKEN_HEADER_KEY = "yanger.token.login-token";

        /** request 中用户信息的属性 key */
        public static final String TOKEN_USER_KEY = "yanger.token.login-user";

        /** session 中验证码的key值 */
        public static final String TOKEN_RANDOM_CODE = "yanger.token.random.code";

    }

    /** 微信授权配置项 web-spring-boot-starter/src/main/java/com/yanger/starter/web/wx/config/WxMaProperties.java */
    public static class WeiXinConfigKey {

        /** 微信授权信息配置，集合形式 */
        public static final String WX_MINIAPP_CONFIGS = "yanger.wx.miniapp.configs";

    }

    /** mybatis配置项 mybatis-spring-boot-starter/src/main/java/com/yanger/starter/mybatis/config/MybatisProperties.java */
    public static class MybatisConfigKey {

        /** sql 日志 */
        public static final String ENABLE_LOG = "yanger.mybatis.enable.log";

        /** 分页默认起始页 */
        public static final String PAGE = "yanger.mybatis.page";

        /** 分页默认大小 */
        public static final String LIMIT = "yanger.mybatis.limit";

        /** 单页限制 默认不限制 */
        public static final String SINGLE_PAGE_LIMIT  = "yanger.mybatis.single.page.limit";

        /** 是否开启敏感数据加密 */
        public static final String ENABLE_SENSITIVE = "yanger.mybatis.enable-sensitive";

        /** 敏感数据加密 AES_KEY */
        public static final String SENSITIVE_KEY  = "yanger.mybatis.sensitive.key";

        /** sql 检查插件 */
        public static final String ENABLE_ILLEGAL_SQL_INTERCEPTOR = "yanger.mybatis.enable-illegal-sql-interceptor";

        /** SQL执行分析插件, 拦截一些整表操作 */
        public static final String ENABLE_SQL_EXPLAIN_INTERCEPTOR = "yanger.mybatis.enable-sql-explain-interceptor";

        /** MAPPER_LOCATIONS */
        public static final String MAPPER_LOCATIONS = "mybatis-plus.mapper-locations";
        /** CONFIGURATION_LOG_IMPL */
        public static final String CONFIGURATION_LOG_IMPL = "mybatis-plus.configuration.log-impl";
        /** CONFIGURATION_CALL_SETTERS_ON_NULLS */
        public static final String CONFIGURATION_CALL_SETTERS_ON_NULLS = "mybatis-plus.configuration.call-setters-on-nulls";
        /** CONFIGURATION_CACHE_ENABLED */
        public static final String CONFIGURATION_CACHE_ENABLED = "mybatis-plus.configuration.cache-enabled";
        /** CONFIGURATION_MAP_UNDERSCORE_TO_CAMEL_CASE */
        public static final String CONFIGURATION_MAP_UNDERSCORE_TO_CAMEL_CASE = "mybatis-plus.configuration.map-underscore-to-camel-case";
        /** GLOBAL_LOGIC_DELETE_VALUE */
        public static final String GLOBAL_LOGIC_DELETE_VALUE = "mybatis-plus.global-config.db-config.logic-delete-value";
        /** GLOBAL_LOGIC_NOT_DELETE_VALUE */
        public static final String GLOBAL_LOGIC_NOT_DELETE_VALUE = "mybatis-plus.global-config.db-config.logic-not-delete-value";
        /** 组件类型 */
        public static final String GLOBAL_ID_TYPE = "mybatis-plus.global-config.db-config.id-type";
        /** GLOBAL_LOGIC_BANNER */
        public static final String GLOBAL_LOGIC_BANNER = "mybatis-plus.global-config.banner";

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

    /** Druid数据源的配置项 */
    public static class DruidConfigKey {
        /** TYPE */
        public static final String TYPE = "spring.datasource.type";
        /** DRIVER_CLASS */
        public static final String DRIVER_CLASS = "spring.datasource.driver-class-name";
        /** INITIALSIZE */
        public static final String INITIALSIZE = "spring.datasource.druid.initialSize";
        /** MINIDLE */
        public static final String MINIDLE = "spring.datasource.druid.minIdle";
        /** MAXACTIVE */
        public static final String MAXACTIVE = "spring.datasource.druid.maxActive";
        /** MAXWAIT */
        public static final String MAXWAIT = "spring.datasource.druid.maxWait";
        /** TIMEBETWEENEVICTIONRUNSMILLIS */
        public static final String TIMEBETWEENEVICTIONRUNSMILLIS = "spring.datasource.druid.timeBetweenEvictionRunsMillis";
        /** MINEVICTABLEIDLETIMEMILLIS */
        public static final String MINEVICTABLEIDLETIMEMILLIS = "spring.datasource.druid.minEvictableIdleTimeMillis";
        /** VALIDATIONQUERY */
        public static final String VALIDATIONQUERY = "spring.datasource.druid.validationQuery";
        /** TESTWHILEIDLE */
        public static final String TESTWHILEIDLE = "spring.datasource.druid.testWhileIdle";
        /** TESTONBORROW */
        public static final String TESTONBORROW = "spring.datasource.druid.testOnBorrow";
        /** TESTONRETURN */
        public static final String TESTONRETURN = "spring.datasource.druid.testOnReturn";
        /** POOLPREPAREDSTATEMENTS */
        public static final String POOLPREPAREDSTATEMENTS = "spring.datasource.druid.poolPreparedStatements";
        /** MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE */
        public static final String MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE = "spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize";
        /** FILTERS */
        public static final String FILTERS = "spring.datasource.druid.filters";
        /** CONNECTIONPROPERTIES */
        public static final String CONNECTIONPROPERTIES = "spring.datasource.druid.connectionProperties";
        /** USUEGLOBALDATASOURCESTAT */
        public static final String USUEGLOBALDATASOURCESTAT = "spring.datasource.druid.useGlobalDataSourceStat";
        /** WEB_FILTER */
        public static final String WEB_FILTER = "spring.datasource.druid.web-stat-filter.enabled";
        /** WEB_FILTER_URL_PATTERN */
        public static final String WEB_FILTER_URL_PATTERN = "spring.datasource.druid.web-stat-filter.url-pattern";
        /** WEB_FILTER_EXCLUSIONS */
        public static final String WEB_FILTER_EXCLUSIONS = "spring.datasource.druid.web-stat-filter.exclusions";
        /** STAT */
        public static final String STAT = "spring.datasource.druid.stat-view-servlet.enabled";
        /** STAT_URL_PATTERN */
        public static final String STAT_URL_PATTERN = "spring.datasource.druid.stat-view-servlet.url-pattern";
        /** STAT_ALLOW */
        public static final String STAT_ALLOW = "spring.datasource.druid.stat-view-servlet.allow";
        /** STAT_DENY */
        public static final String STAT_DENY = "spring.datasource.druid.stat-view-servlet.deny";
        /** STAT_RESET */
        public static final String STAT_RESET = "spring.datasource.druid.stat-view-servlet.reset-enable";
        /** STAT_USERNAME */
        public static final String STAT_USERNAME = "spring.datasource.druid.stat-view-servlet.login-username";
        /** STAT_PASSWORD */
        public static final String STAT_PASSWORD = "spring.datasource.druid.stat-view-servlet.login-password";
    }

}
