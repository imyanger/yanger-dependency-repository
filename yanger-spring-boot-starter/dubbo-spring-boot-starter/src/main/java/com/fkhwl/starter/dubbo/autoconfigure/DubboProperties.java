package com.fkhwl.starter.dubbo.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.11.18 23:44
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = DubboProperties.PREFIX)
public class DubboProperties {
    /** PREFIX */
    public static final String PREFIX = "fkh.dubbo";
}
