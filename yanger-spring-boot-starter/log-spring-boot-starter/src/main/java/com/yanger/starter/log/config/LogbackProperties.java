package com.yanger.starter.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @Description logback
 * @Author yanger
 * @Date 2021/2/25 17:56
 */
@Data
@Component
@ConfigurationProperties(prefix = LogbackProperties.PREFIX)
public class LogbackProperties {

    /** PREFIX */
    public static final String PREFIX = "yanger.logger";

    /** 日志级别 */
    private LogLevel level;

    /** 项目名称 */
    private String appName;

    /** 日志目录 */
    private String logHome = "/yanger/log";

    /** 是否允许动态修改日志 */
    private boolean dynamicLogEnabled = false;

    /** 动态日志等级接口可修改的包 */
    private String dynamicLogPackage;

    /** 是否打印api调用日志：线上建议关闭 */
    private boolean apiLogEnabled = false;

}
