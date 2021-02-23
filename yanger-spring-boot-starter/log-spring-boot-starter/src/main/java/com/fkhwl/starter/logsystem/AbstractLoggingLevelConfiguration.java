package com.fkhwl.starter.logsystem;

import com.fkhwl.starter.basic.constant.ConfigDefaultValue;
import com.fkhwl.starter.basic.util.JsonUtils;
import com.fkhwl.starter.logsystem.listener.FkhLoggingListener;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerGroup;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Locale;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.09.06 19:51
 * @since 1.6.0
 */
@Slf4j
public abstract class AbstractLoggingLevelConfiguration<T extends ApplicationEvent> implements ApplicationListener<T>, EnvironmentAware {
    /** Environment */
    protected Environment environment;

    /**
     * Sets environment *
     *
     * @param environment environment
     * @since 1.6.0
     */
    @Override
    public void setEnvironment(@NotNull Environment environment) {
        this.environment = environment;
    }

    /**
     * On application event
     *
     * @param event event
     * @since 1.6.0
     */
    @Override
    public void onApplicationEvent(@NotNull T event) {
        if (this.environment == null) {
            return;
        }
        LoggingSystem system = LoggingSystem.get(LoggingSystem.class.getClassLoader());
        this.configureLogLevel(system, this.changedLevels(event));
    }

    /**
     * Changed levels
     *
     * @param event event
     * @return the map
     * @since 1.6.0
     */
    protected abstract Map<String, String> changedLevels(T event);

    /**
     * Configure log level
     *
     * @param loggingSystem logging system
     * @param levels        levels
     * @since 1.6.0
     */
    public void configureLogLevel(LoggingSystem loggingSystem, @NotNull Map<String, String> levels) {
        log.info("日志等级修改事件处理: {}", JsonUtils.toJson(levels));

        levels.forEach((name, level) -> {
            level = this.environment.resolvePlaceholders(level);

            LoggerGroup group = FkhLoggingListener.getLoggerGroups().get(name);
            if (group != null && group.hasMembers()) {
                group.configureLogLevel(this.resolveLogLevel(level), loggingSystem::setLogLevel);
                return;
            }

            if (name.equalsIgnoreCase(LoggingSystem.ROOT_LOGGER_NAME)) {
                name = null;
            }
            loggingSystem.setLogLevel(name, this.resolveLogLevel(level));
        });
    }

    /**
     * Resolve log level
     *
     * @param level level
     * @return the log level
     * @since 1.6.0
     */
    private LogLevel resolveLogLevel(@NotNull String level) {
        String trimmedLevel = level.trim();
        if (ConfigDefaultValue.FALSE_STRING.equalsIgnoreCase(trimmedLevel)) {
            return LogLevel.OFF;
        }
        return LogLevel.valueOf(trimmedLevel.toUpperCase(Locale.ENGLISH));
    }
}
