package com.fkhwl.starter.logsystem.handler;

import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.common.util.ConfigKit;
import com.fkhwl.starter.common.util.JustOnceLogger;
import com.fkhwl.starter.core.util.FileUtils;
import com.fkhwl.starter.core.util.StringUtils;
import com.fkhwl.starter.logsystem.AbstractPropertiesProcessor;
import com.fkhwl.starter.logsystem.Constants;
import com.fkhwl.starter.logsystem.constant.LogSystem;
import com.fkhwl.starter.logsystem.entity.LogFile;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.logging.LoggingSystemProperties;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;

import lombok.Getter;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: fkh.logging.file 配置处理</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.26 12:18
 * @since 1.0.0
 */
public final class LogFileProcessor extends AbstractPropertiesProcessor {
    /** File */
    @Getter
    private String name;
    /** Path */
    @Getter
    private String path;

    /**
     * 初始化处理器, 第一步需要处理 name 和 path
     *
     * @param environment environment
     * @since 1.0.0
     */
    public LogFileProcessor(ConfigurableEnvironment environment) {
        super(environment);
        this.processorNameAndPath(environment);
    }

    /**
     * 处理 file name 和 path, 需要返回给日志系统用于初始化
     *
     * @param environment environment
     * @since 1.4.0
     */
    private void processorNameAndPath(ConfigurableEnvironment environment) {
        String tempName = this.getProperty(ConfigKey.LogSystemConfigKey.LOG_FILE_NAME,
                                           Constants.DEPRECATED_FILE_NAME_PROPERTY,
                                           Constants.DEFAULT_FILE_NAME);
        String tempPath = this.getProperty(ConfigKey.LogSystemConfigKey.LOG_FILE_PATH,
                                           Constants.DEPRECATED_FILE_PATH_PROPERTY,
                                           LogSystem.DEFAULT_LOGGING_LOCATION);

        // 本地开发且没有配置 path 时输出到临时文件
        if (ConfigKit.isLocalLaunch() && LogSystem.DEFAULT_LOGGING_LOCATION.equals(tempPath)) {
            tempPath = FileUtils.getTempDirPath();
            // 应用退出时删除临时日志文件
            File file = new File(tempPath, tempName);
            file.deleteOnExit();
        }

        this.name = tempName;
        this.path = FileUtils.toPath(tempPath);

        JustOnceLogger.printOnce(LogFileProcessor.class.getName(),
                                 "logging file: "
                                 + FileUtils.appendPath(this.path,
                                                        ConfigKit.getProperty(environment,
                                                                              ConfigKey.SpringConfigKey.APPLICATION_NAME),
                                                        this.name));

    }

    /**
     * 将自定义配置注入到日志系统, 这里首先读取出应用环境的日志配置, 然后注入到系统环境中, 日志系统在读取日志配置文件时, 会从系统环境中替换日志配置中的变量
     *
     * @since 1.0.0
     */
    @Override
    public void apply() {
        // 提前将配置绑定到配置类
        Binder binder = Binder.get(this.environment);
        LogFile logFile = binder.bind(ConfigKey.LogSystemConfigKey.LOG_FILE, LogFile.class).orElse(new LogFile(this.toString(), this.path));

        if (logFile.getPath() == null) {
            logFile.setPath(this.path);
        }
        if (logFile.getName() == null) {
            logFile.setName(this.name);
        }
        // fkh.logging.file.path
        this.setSystemProperty(FileUtils.toPath(logFile.getPath()), LoggingSystemProperties.LOG_PATH,
                               ConfigKey.LogSystemConfigKey.LOG_FILE_PATH);
        // fkh.logging.file.name
        this.setSystemProperty(logFile.getName(), LoggingSystemProperties.LOG_FILE, ConfigKey.LogSystemConfigKey.LOG_FILE_NAME);
        // fkh.logging.file.clean-history-on-start
        this.setSystemProperty(String.valueOf(logFile.isCleanHistoryOnStart()), ConfigKey.LogSystemConfigKey.LOG_FILE_CLEAN_HISTORY,
                               Constants.FILE_CLEAN_HISTORY_ON_START);
        // fkh.logging.file.max-history
        this.setSystemProperty(String.valueOf(logFile.getMaxHistory()), ConfigKey.LogSystemConfigKey.LOG_FILE_MAX_HISTORY,
                               Constants.FILE_MAX_HISTORY);
        // fkh.logging.file.max-size
        this.setSystemProperty(logFile.getMaxSize(), ConfigKey.LogSystemConfigKey.LOG_FILE_MAX_SIZE, Constants.FILE_MAX_SIZE);
        // fkh.logging.file.total-size-cap
        this.setSystemProperty(String.valueOf(logFile.getTotalSizeCap()), ConfigKey.LogSystemConfigKey.LOG_FILE_TOTAL_SIZE_CAP,
                               Constants.FILE_TOTAL_SIZE_CAP);

    }

    /**
     * 根据 path 和 name 生成完整的日志文件路径
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        if (StringUtils.hasLength(this.name)) {
            return this.name;
        }
        return new File(this.path, Constants.DEFAULT_FILE_NAME).getPath();
    }

}
