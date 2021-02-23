package com.fkhwl.starter.logsystem.entity;

import lombok.Data;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 日志输出格式. </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.26 13:51
 * @since 1.0.0
 */
@Data
@SuppressWarnings("checkstyle:LineLength")
public class Pattern {
    /** 日志等级输出格式. */
    private String level = "%5p";
    /** Marker 日志格式. */
    private String marker = "%m%n";
    /** 日志时间输出格式. */
    private String dateformat = "yyyy-MM-dd HH:mm:ss.SSS";
    /** 日志回滚的文件名格式. */
    private String rollingFileName = "%d{yyyyMMdd-HH}.%i.log.gz";
    /** 输出到文件的格式, 此配置只对 log4j2-flie.xml 有效. */
    private String file = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p [%traceId] - [%15.15t] %c{1.} :: %m%n%xwEx";
    /** formatter:off 输出到控制台的格式, 此配置只对 log4j2-console.xml 有效. */
    private String console = "%clr{%d{yyyy-MM-dd HH:mm:ss.SSS}}{faint} %clr{[%5p]} [%appType] [%traceId] %clr{-}{faint} %clr{[%15.15t]}{faint} %location{.} %clr{::}{faint} %m%n%xwEx";
}
