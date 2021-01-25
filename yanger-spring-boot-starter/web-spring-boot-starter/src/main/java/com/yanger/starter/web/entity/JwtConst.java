package com.yanger.starter.web.entity;

/**
 * @description jwt 常量类
 * @author YangHao
 * @date 2018年9月23日-下午4:28:57
 */
public class JwtConst {

    /** jwt token 有效时长（毫秒） */
    public static final Long TOKEN_AVAILABLE_TIME = 24 * 60 * 60 * 1000L;

    /** jwt token 续期时长（分钟） */
    public static final Long TOKEN_RENEWAL_TIME_MINUTE = 60 * 24L;

    /**  jwt header 中的标志 */
    public static final String HEADER_TOKEN_KEY = "yanger-general-backend-token";

    /** request 中用户信息的属性 key */
    public static final String REQUEST_ATTR_KEY = "yanger-general-backend-user";

}
