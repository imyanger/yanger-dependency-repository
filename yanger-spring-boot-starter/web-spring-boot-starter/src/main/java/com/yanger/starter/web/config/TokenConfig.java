package com.yanger.starter.web.config;

import com.yanger.starter.basic.constant.ConfigDefaultValue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @description jwt 常量类
 * @author YangHao
 * @date 2018年9月23日-下午4:28:57
 */
@Data
@Component
@ConfigurationProperties(prefix = TokenConfig.PREFIX)
public class TokenConfig {

    /** PREFIX */
    public static final String PREFIX = "yanger.token";

    /** jwt token 有效时长（分钟），默认24小时 */
    private Long availableTimeMinute = ConfigDefaultValue.TokenConfigValue.TOKEN_AVAILABLE_TIME;

    /** jwt token 续期的剩余时长（分钟），默认1小时 */
    private Long renewalTimeMinute = ConfigDefaultValue.TokenConfigValue.TOKEN_RENEWAL_TIME_MINUTE;

    /**  jwt header 中的标志 */
    private String headerKey = ConfigDefaultValue.TokenConfigValue.TOKEN_HEADER_KEY;

    /** request 中用户信息的属性 key */
    private String userKey = ConfigDefaultValue.TokenConfigValue.TOKEN_USER_KEY;

}
