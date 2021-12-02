package com.yanger.starter.basic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Json 配置
 * @Author yanger
 * @Date 2021/2/26 10:22
 */
@Data
@Component
@ConfigurationProperties(prefix = JsonProperties.PREFIX)
public class JsonProperties {

    /** PREFIX */
    public static final String PREFIX = "yanger.json";

    /** JSON 时间格式化 格式 */
    private String dateFormat;

    /** JSON 时间格式化 时区 */
    private String timeZone;

}
