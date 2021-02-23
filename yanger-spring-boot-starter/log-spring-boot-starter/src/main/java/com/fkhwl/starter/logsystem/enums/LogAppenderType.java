package com.fkhwl.starter.logsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 日志输出位置 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.03 13:12
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum LogAppenderType {
    /** Console log appender type */
    CONSOLE("log4j2-console.xml"),
    /** File log appender type */
    FILE("log4j2-file.xml");

    /** Config */
    private final String config;
}
