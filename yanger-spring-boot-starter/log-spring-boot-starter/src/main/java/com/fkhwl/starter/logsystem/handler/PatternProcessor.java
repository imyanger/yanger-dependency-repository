package com.fkhwl.starter.logsystem.handler;

import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.logsystem.AbstractPropertiesProcessor;
import com.fkhwl.starter.logsystem.Constants;
import com.fkhwl.starter.logsystem.entity.Pattern;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  fkh.logging.pattern 配置处理</p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.05.20 10:47
 * @since 1.4.0
 */
public class PatternProcessor extends AbstractPropertiesProcessor {
    /**
     * Pattern processor
     *
     * @param environment environment
     * @since 1.4.0
     */
    public PatternProcessor(ConfigurableEnvironment environment) {
        super(environment);
    }

    /**
     * Apply
     *
     * @since 1.4.0
     */
    @Override
    public void apply() {
        // 提前将配置绑定到配置类
        Binder binder = Binder.get(this.environment);
        Pattern pattern = binder.bind(ConfigKey.LogSystemConfigKey.LOG_PATTERN, Pattern.class).orElse(new Pattern());

        // fkh.logging.pattern.console
        this.setSystemProperty(pattern.getConsole(), Constants.CONSOLE_LOG_PATTERN,
                               ConfigKey.LogSystemConfigKey.LOG_PATTERN_CONSOLE);
        // fkh.logging.pattern.file
        this.setSystemProperty(pattern.getFile(), Constants.FILE_LOG_PATTERN, ConfigKey.LogSystemConfigKey.LOG_PATTERN_FILE);
        // fkh.logging.pattern.level
        this.setSystemProperty(pattern.getLevel(), Constants.LOG_LEVEL_PATTERN, ConfigKey.LogSystemConfigKey.LOG_PATTERN_LEVEL);
        // fkh.logging.pattern.dateformat
        this.setSystemProperty(pattern.getDateformat(), Constants.LOG_DATEFORMAT_PATTERN,
                               ConfigKey.LogSystemConfigKey.LOG_PATTERN_DATEFORMAT);
        // fkh.logging.pattern.rolling-file-name
        this.setSystemProperty(pattern.getRollingFileName(), Constants.ROLLING_FILE_NAME_PATTERN,
                               ConfigKey.LogSystemConfigKey.ROLLING_FILE_NAME);
        //  fkh.logging.pattern.marker
        this.setSystemProperty(pattern.getMarker(), Constants.MARKER_PATTERN, ConfigKey.LogSystemConfigKey.MARKER_PATTERN);

    }
}
