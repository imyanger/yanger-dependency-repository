package com.yanger.starter.basic.constant;

import com.yanger.starter.basic.enums.TokenType;

/**
 * 常用的配置默认值
 * @Author yanger
 * @Date 2020/12/21 10:02
 */
public class ConfigDefaultValue {

    /** 启动标识, 所有项目只能使用 BasicApplication 启动 */
    public static final String APPLICATION_STARTER_FLAG = "yanger-spring-boot-starter";


    /** json 配置默认值 */
    public static class JsonConfigValue {

        /** JSON_DATE_FORMAT */
        public static final String JSON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        /** JSON_TIME_ZONE */
        public static final String JSON_TIME_ZONE = "GMT+8";

    }


    /** token 配置默认值 */
    public static class TokenConfigValue {

        /** 是否开启token验证，默认开启 */
        public static final Boolean ENABLE = true;

        /** jwt token 有效时长（分钟） */
        public static final Long TOKEN_AVAILABLE_TIME = 24 * 60L;

        /** jwt token 续期的剩余时长（分钟） */
        public static final Long TOKEN_RENEWAL_TIME_MINUTE = 60L;

        /**  jwt header 中的标志 */
        public static final String TOKEN_HEADER_KEY = "yanger-spring-boot-starter-login-token";

        /** request 中用户信息的属性 key */
        public static final String TOKEN_USER_KEY = "yanger-spring-boot-starter-login-user";

        /** session 中验证码的key值 */
        public static final String TOKEN_RANDOM_CODE = "yanger-spring-boot-token-random-code";

        /** 用户信息存储方式，session 还是 header 获取 */
        public static final TokenType TOKEN_TYPE = TokenType.HEADER;

    }

    /** SerializeEnumContainer 配置默认值 */
    public static class EnumConfigValue {

        /** 是否开启 SerializeEnumContainer，默认不开启 */
        public static final Boolean ENABLE = false;

        /** SerializeEnumContainer api 获取地址 */
        public static final String API_URL = "/yanger/enums";

    }

    /** SerializeEnumContainer 配置默认值 */
    public static class LogBookConfigValue {

        /** 是否开启 logbook 打印请求日志，默认 开启 */
        public static final Boolean ENABLE = true;

        /** logbook 日志级别 */
        public static final String LOGBOOK_WRITE_LEVEL = "info";

        /** logbook 日志风格， json curl http 可选 */
        public static final String LOGBOOK_FORMAT_STYLE = "json";

        /** logbook 请求级别 */
        public static final String LOGGING_LEVEL_ORG_ZALANDO_LOGBOOK = "trace";

    }

}
