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

}
