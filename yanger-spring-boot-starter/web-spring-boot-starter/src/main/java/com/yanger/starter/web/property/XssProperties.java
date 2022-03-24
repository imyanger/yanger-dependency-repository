package com.yanger.starter.web.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Xss配置类
 * @Author yanger
 * @Date 2021/1/27 16:41
 */
@Data
@ConfigurationProperties(XssProperties.PREFIX)
public class XssProperties {

    /** PREFIX */
    public static final String PREFIX = "yanger.xss";

    /** xss 处理器 */
    private boolean enableXssFilter = Boolean.TRUE;

    /** 设置忽略的 url */
    private List<String> excludePatterns = new ArrayList<>();

}
