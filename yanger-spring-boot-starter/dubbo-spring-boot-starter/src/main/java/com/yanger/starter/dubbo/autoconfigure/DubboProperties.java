package com.yanger.starter.dubbo.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @Description
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Data
@ConfigurationProperties(prefix = DubboProperties.PREFIX)
public class DubboProperties {
    /** PREFIX */
    public static final String PREFIX = "yanger.dubbo";
}
