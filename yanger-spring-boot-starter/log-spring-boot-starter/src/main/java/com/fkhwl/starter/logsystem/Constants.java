package com.fkhwl.starter.logsystem;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.05.20 12:14
 */
public final class Constants {
    /** FILE_NAME_PROPERTY */
    public static final String DEFAULT_FILE_NAME = "all.log";
    /** DEPRECATED_FILE_NAME_PROPERTY */
    public static final String DEPRECATED_FILE_NAME_PROPERTY = "logging.file.name";
    /** DEPRECATED_FILE_PATH_PROPERTY */
    public static final String DEPRECATED_FILE_PATH_PROPERTY = "logging.file.path";

    /** 当前应用名 */
    public static final String APP_NAME = "APP_NAME";
    /** 日志输出跳转信息, 本地开发时默认开启 */
    public static final String SHOW_LOG_LOCATION = "SHOW_LOG_LOCATION";
    /** location 未开启时的 layout 配置 */
    public static final String SHOW_LOG_LOCATION_LAYOUT = "SHOW_LOG_LOCATION_LAYOUT";
    /** 日志输出到控制台的格式 */
    public static final String CONSOLE_LOG_PATTERN = "CONSOLE_LOG_PATTERN";
    /** 日志输出到文件的格式 */
    public static final String FILE_LOG_PATTERN = "FILE_LOG_PATTERN";
    /** 输出的日志等级格式 */
    public static final String LOG_LEVEL_PATTERN = "LOG_LEVEL_PATTERN";
    /** 日志时间格式 */
    public static final String LOG_DATEFORMAT_PATTERN = "LOG_DATEFORMAT_PATTERN";
    /** 日志回滚的文件名 */
    public static final String ROLLING_FILE_NAME_PATTERN = "ROLLING_FILE_NAME_PATTERN";
    /** marker 日志格式 */
    public static final String MARKER_PATTERN = "MARKER_PATTERN";
    /** 是否在应用启动时删除历史日志 */
    public static final String FILE_CLEAN_HISTORY_ON_START = "LOG_FILE_CLEAN_HISTORY_ON_START";
    /** 日志文件保留的最大时间 */
    public static final String FILE_MAX_HISTORY = "LOG_FILE_MAX_HISTORY";
    /** 日志文件最大容量 */
    public static final String FILE_MAX_SIZE = "LOG_FILE_MAX_SIZE";
    /** 日志文件总数量 */
    public static final String FILE_TOTAL_SIZE_CAP = "LOG_FILE_TOTAL_SIZE_CAP";
}
