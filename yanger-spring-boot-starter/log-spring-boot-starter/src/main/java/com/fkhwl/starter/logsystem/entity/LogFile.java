package com.fkhwl.starter.logsystem.entity;

import org.jetbrains.annotations.Contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.26 13:51
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFile {
    /** 日志文件名, 此配置只对 log4j2-flie.xml 有效. */
    private String name;
    /** 日志保存路径, 此配置只对 log4j2-flie.xml 有效. */
    private String path;
    /** 启动应用时清理历史日志, 此配置只对 log4j2-flie.xml 有效. */
    private boolean cleanHistoryOnStart = false;
    /** 历史日志最大保留时间(天), 此配置只对 log4j2-flie.xml 有效. */
    private Integer maxHistory = 90;
    /** 日志文件最大容量, 此配置只对 log4j2-flie.xml 有效. */
    private String maxSize = "50MB";
    /** 日志总数量, 此配置只对 log4j2-flie.xml 有效. */
    private Integer totalSizeCap = 50;

    /**
     * Log file
     *
     * @param name name
     * @param path path
     * @since 1.4.0
     */
    @Contract(pure = true)
    public LogFile(String name, String path) {
        this.name = name;
        this.path = path;
    }
}
