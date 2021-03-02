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

    /** 启动方式, 用于区分是否通过 jar 或者 .sh 脚本启动 */
    public static final String APPLICATION_START_TYPE = "yanger.app.start-type";

    /** 启动标识, 所有项目只能使用 BasicApplication 启动 */
    public static final String APPLICATION_STARTER_FLAG = "yanger.app.starter-flag";

    /** 项目地址 */
    public static final String APPLICATION_SERVER_URL = "yanger.app.server-url";

    /** 启动参数, 用于获取配置文件路径，便于jar 启动时更改配置文件需要重新打包，eg：--yanger.app.config-path=D:\yangr\ */
    public static final String APP_CONFIG_PATH = "yanger.app.config-path";


    /** xss 配置项 web-spring-boot-starter/src/main/java/com/yanger/starter/web/config/XssProperties.java */
    public static class XssConfigKey {

        /** XSS_ENABLE_XSS_FILTER */
        public static final String XSS_ENABLE_XSS_FILTER = "yanger.xss.enable-xss-filter";

        /** XSS_ENABLE_XSS_FILTER */
        public static final String XSS_EXCLUDE_PATTERNS = "yanger.xss.exclude-patterns";

    }


    /** json 配置项 basic-spring-boot-starter/src/main/java/com/yanger/starter/basic/config/JsonProperties.java */
    public static class JsonConfigKey {

        /** JSON_DATE_FORMAT */
        public static final String JSON_DATE_FORMAT = "yanger.json.date-format";

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

        /** 存在相同的 bean name 时, 后一个覆盖前一个 */
        public static final String MAIN_ALLOW_BEAN_DEFINITION_OVERRIDING = "spring.main.allow-bean-definition-overriding";

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


    /** Jvm 配置项 */
    public static class JvmConfigKey {

        /** TMP_DIR */
        public static final String TMP_DIR = "java.io.tmpdir";

    }


    /** Druid数据源的配置项 */
    public static class DruidConfigKey {

        /** TYPE */
        public static final String TYPE = "spring.datasource.type";

        /** DRIVER_CLASS */
        public static final String DRIVER_CLASS = "spring.datasource.driver-class-name";

        /** INITIAL_SIZE */
        public static final String INITIAL_SIZE = "spring.datasource.druid.initialSize";

        /** MIN_IDLE */
        public static final String MIN_IDLE = "spring.datasource.druid.minIdle";

        /** MAX_ACTIVE */
        public static final String MAX_ACTIVE = "spring.datasource.druid.maxActive";

        /** MAX_WAIT */
        public static final String MAX_WAIT = "spring.datasource.druid.maxWait";

        /** TIME_BETWEEN_EVICTION_RUNS_MILLIS */
        public static final String TIME_BETWEEN_EVICTION_RUNS_MILLIS = "spring.datasource.druid.timeBetweenEvictionRunsMillis";

        /** MIN_EVICTABLE_IDLE_TIME_MILLIS */
        public static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "spring.datasource.druid.minEvictableIdleTimeMillis";

        /** VALIDATION_QUERY */
        public static final String VALIDATION_QUERY = "spring.datasource.druid.validationQuery";

        /** TEST_WHILE_IDLE */
        public static final String TEST_WHILE_IDLE = "spring.datasource.druid.testWhileIdle";

        /** TEST_ON_BORROW */
        public static final String TEST_ON_BORROW = "spring.datasource.druid.testOnBorrow";

        /** TEST_ON_RETURN */
        public static final String TEST_ON_RETURN = "spring.datasource.druid.testOnReturn";

        /** POOL_PREPARED_STATEMENTS */
        public static final String POOL_PREPARED_STATEMENTS = "spring.datasource.druid.poolPreparedStatements";

        /** MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE */
        public static final String MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE = "spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize";

        /** FILTERS */
        public static final String FILTERS = "spring.datasource.druid.filters";

        /** CONNECTION_PROPERTIES */
        public static final String CONNECTION_PROPERTIES = "spring.datasource.druid.connectionProperties";

        /** USE_GLOBAL_DATA_SOURCE_STAT */
        public static final String USE_GLOBAL_DATA_SOURCE_STAT = "spring.datasource.druid.useGlobalDataSourceStat";

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


    /** id生成 配置项 id-spring-boot-starter/src/main/java/com/yanger/starter/id/config/IdProperties.java */
    public static class IdConfigKey {

        /** Type */
        public static final String PROVIDER_TYPE = "yanger.id.provider-type";

        /** Id type */
        public static final String ID_TYPE = "yanger.id.id-type";

        /** Deploy type */
        public static final String DEPLOY_TYPE = "yanger.id.deploy-type";

        /** type = PROPERTY 需要的参数 Machine id */
        public static final String MACHINE_ID = "yanger.id.machine-id";

        /** Version */
        public static final String VERSION = "yanger.id.version";

        /** type = IP_CONFIGURABLE 需要的参数 ips */
        public static final String IPS = "yanger.id.ips";

        /** type = DB 需要的参数 Db driver calss name */
        public static final String DB_DRIVER_CLASS_NAME = "yanger.id.db-driver-class-name";

        /** type = DB 需要的参数 数据库连接 */
        public static final String DB_URL = "yanger.id.db-url";

        /** type = DB 需要的参数 Db user */
        public static final String DB_USERNAME = "yanger.id.db-username";

        /** type = DB 需要的参数 Db password */
        public static final String DB_PASSWORD = "yanger.id.db-password";

        /** 生成时间和序列号的算法的同步类型：synchronized、cas、lock */
        public static final String SYNC_TYPE = "yanger.id.sync-type";

    }


    /** log日志 配置项 log-spring-boot-starter/src/main/java/com/yanger/starter/log/config/LogbackProperties.java */
    public static class LogConfigKey {

        /** 日志应用名 */
        public static final String APP_NAME = "yanger.logger.app-name";

        /** 日志位置 */
        public static final String LOG_HOME = "yanger.logger.log-home";

        /** 日志等级 */
        public static final String LEVEL = "yanger.logger.level";

        /** 动态日志等级接口可修改的包 */
        public static final String DYNAMIC_LOG_PACKAGE = "yanger.logger.dynamic-log-package";

        /** 是否允许动态修改日志 */
        public static final String DYNAMIC_LOG_ENABLED = "yanger.logger.dynamic-log-enabled";

    }


    /** jetcache 配置项 cache-spring-boot-starter/src/main/java/com/yanger/starter/cache/autoconfigure/CacheProperties.java */
    public static class JetcacheConfigKey {

        /** 统计间隔, 0 表示不统计 */
        public static final String STAT_INTERVAL_MINUTES = "jetcache.statIntervalMinutes";

        /** jetcache-anno 把 cacheName 作为远程缓存 key 前缀 */
        public static final String AREA_IN_CACHE_NAME = "jetcache.areaInCacheName";

        /** Hide packages  @Cached 和 @CreateCache 自动生成 name 的时候,为了不让 name 太长,hiddenPackages 指定的包名前缀被截掉 */
        public static final String HIDE_PACKAGES = "jetcache.hidePackages";

        /** 缓存类型：
         * tair、redis（配置：redis.lettuce） 为当前支持的远程缓存;
         * linkedhashmap、caffeine 为当前支持的本地缓存类型
         * 本地使用：jetcache.local.default.type  远程使用：jetcache.remote.default.type
         */
        public static final String LOCAL_DEFAULT_TYPE = "jetcache.local.default.type";

        /**
         * 每个缓存实例的最大元素的全局配置, 仅 local 类型的缓存需要指定. 注意是每个缓存实例的限制, 而不是全部,
         * 比如这里指定 1000, 然后用 @CreateCache 创建了两个缓存实例（并且注解上没有设置 localLimit 属性）, 那么每个缓存实例的限制都是 100
         */
        public static final String LOCAL_DEFAULT_LIMIT = "jetcache.local.default.limit";

        /**
         * key 转换器的全局配置, 当前只有一个已经实现的 keyConvertor: fastjson.
         * 仅当使用 @CreateCache 且缓存类型为 LOCAL 时可以指定为 none, 此时通过 equals 方法来识别 key. 方法缓存必须指定 keyConvertor
         */
        public static final String LOCAL_DEFAULT_KEY_CONVERTOR = "jetcache.local.default.keyConvertor";

        /** remote.default key 转换方式 */
        public static final String REMOTE_DEFAULT_KEY_CONVERTOR = "jetcache.remote.default.keyConvertor";

        /** remote.default value 反序列化器 */
        public static final String REMOTE_DEFAULT_VALUE_DECODER = "jetcache.remote.default.valueDecoder";

        /** remote.default value 序列化器 */
        public static final String REMOTE_DEFAULT_VALUE_ENCODER = "jetcache.remote.default.valueEncoder";

        /** 连接池中的最小空闲连接 默认 0 */
        public static final String POOL_CONFIG_MIN_IDLE = "jetcache.remote.default.poolConfig.minIdle";

        /** 连接池中的最大空闲连接 默认 8 */
        public static final String POOL_CONFIG_MAX_IDLE = "jetcache.remote.default.poolConfig.maxIdle";

        /** 连接池最大连接数 */
        public static final String POOL_CONFIG_MAX_TOTAL = "jetcache.remote.default.poolConfig.maxTotal";

    }

    /** mongo 配置 */
    public static class MongoConfigKey {
        /** ENABLE_AUTO_INCREMENT_KEY */
        public static final String ENABLE_AUTO_INCREMENT_KEY = "yanger.mongo.enable-auto-increment-key";
        /** ENABLE_AUTO_CREATE_INDEX */
        public static final String ENABLE_AUTO_CREATE_INDEX = "yanger.mongo.enable-auto-create-index";
        /** ENABLE_AUTO_CREATE_KEY */
        public static final String ENABLE_AUTO_CREATE_KEY = "yanger.mongo.enable-auto-create-key";
        /** ENABLE_AUTO_CREATE_TIME */
        public static final String ENABLE_AUTO_CREATE_TIME = "yanger.mongo.enable-auto-create-time";
    }





    /** nacos 配置项 */
    public static class NacosConfigKey {

        /** nacos的分组 */
        public static final String GROUP = "yanger.nacos.group";

        /** nacos的命名空间 */
        public static final String NAMESPACE = "yanger.nacos.namespace";

        /** nacos 的地址 */
        public static final String ADDRESS = "yanger.nacos.address";

        /** 是否自动创建配置 */
        public static final String ENABLE_AUTO_CREATE_CONFIG = "yanger.nacos.enable-auto-create-config";

        /** 是否使用 Nacos 配置 */
        public static final String ENABLE_NACOS_CONFIG = "yanger.nacos.enable-nacos-config";

        /** 是否加载配置中心配置 */
        public static final String CONFIG_OVERRIDE_NONE = "spring.cloud.config.override-none";

        /** nacos config 的配置 key */
        public static final String CONFIG_SERVER_ADDRESS = "spring.cloud.nacos.config.server-addr";

        /** CONFIG_FILE_EXTENSION */
        public static final String CONFIG_FILE_EXTENSION = "spring.cloud.nacos.config.file-extension";

        /** CONFIG_ENCODE */
        public static final String CONFIG_ENCODE = "spring.cloud.nacos.config.encode";

        /** CONFIG_NAMESPACE */
        public static final String CONFIG_NAMESPACE = "spring.cloud.nacos.config.namespace";

        /** CONFIG_GROUP */
        public static final String CONFIG_GROUP = "spring.cloud.nacos.config.group";

        /** EXT_CONFIG_1_DATA_ID */
        public static final String EXT_CONFIG_1_DATA_ID = "spring.cloud.nacos.config.ext-config.data-id";

        /** EXT_CONFIG_1_GROUP */
        public static final String EXT_CONFIG_1_GROUP = "spring.cloud.nacos.config.ext-config.group";

        /** EXT_CONFIG_1_NAMESPACE */
        public static final String EXT_CONFIG_1_NAMESPACE = "spring.cloud.nacos.config.ext-config.namespace";

        /** EXT_CONFIG_1_REFRESH */
        public static final String EXT_CONFIG_1_REFRESH = "spring.cloud.nacos.config.ext-config.refresh";

        /** DISCOVERY_NAMESPACE */
        public static final String DISCOVERY_NAMESPACE = "spring.cloud.nacos.discovery.namespace";

        /** DISCOVERY_SERVER_ADDRESS */
        public static final String DISCOVERY_SERVER_ADDRESS = "spring.cloud.nacos.discovery.server-addr";

        /** DISCOVERY_GROUP */
        public static final String DISCOVERY_GROUP = "spring.cloud.nacos.discovery.group";

        /** DISCOVERY_IP */
        public static final String DISCOVERY_IP = "spring.cloud.nacos.discovery.ip";

    }

    /** dubbo 配置项 */
    public static class DubboConfigKey {

        /** APPLICATION_LOGGER */
        public static final String APPLICATION_LOGGER = "dubbo.application.logger";

        /** DUBBO_HOST */
        public static final String DUBBO_HOST = "dubbo.protocol.host";

        /** REGISTRY_ADDRESS */
        public static final String REGISTRY_ADDRESS = "dubbo.registry.address";

        /** PROTOCOL_NAME */
        public static final String PROTOCOL_NAME = "dubbo.protocol.name";

        /** CONSUMER_CHECK */
        public static final String CONSUMER_CHECK = "dubbo.consumer.check";

        /** CONSUMER_VALIDATION */
        public static final String CONSUMER_VALIDATION = "dubbo.consumer.validation";

        /** PROVIDER_TIMEOUT */
        public static final String PROVIDER_TIMEOUT = "dubbo.provider.timeout";

        /** PROTOCOL_PORT */
        public static final String PROTOCOL_PORT = "dubbo.protocol.port";

        /** 元数据中心地址 */
        public static final String METADATA_REPORT_ADDRESS = "dubbo.metadata-report.address";

        /** DUBBO_CONSUMER_FILTER */
        public static final String DUBBO_CONSUMER_FILTER = "dubbo.consumer.filter";

        /** DUBBO_PROVIDER_FILTER */
        public static final String DUBBO_PROVIDER_FILTER = "dubbo.provider.filter";

    }

}
