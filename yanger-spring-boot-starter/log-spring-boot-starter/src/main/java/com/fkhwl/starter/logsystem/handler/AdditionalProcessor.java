package com.fkhwl.starter.logsystem.handler;

import com.fkhwl.starter.basic.constant.ConfigDefaultValue;
import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.common.util.ConfigKit;
import com.fkhwl.starter.common.util.JustOnceLogger;
import com.fkhwl.starter.core.util.StringUtils;
import com.fkhwl.starter.logsystem.AbstractPropertiesProcessor;
import com.fkhwl.starter.logsystem.Constants;
import com.fkhwl.starter.logsystem.enums.LogAppenderType;
import com.fkhwl.starter.logsystem.listener.FkhLoggingListener;

import org.slf4j.MDC;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;


/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 此类的常量与日志配置文件中的常量对应 </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.25 21:36
 * @since 1.0.0
 */
public class AdditionalProcessor extends AbstractPropertiesProcessor {
    /**
     * Create a new {@link AdditionalProcessor} instance.
     *
     * @param environment the source environment
     * @since 1.0.0
     */
    public AdditionalProcessor(ConfigurableEnvironment environment) {
        super(environment);
    }

    /**
     * 将自定义配置注入到日志系统, 这里首先读取出应用环境的日志配置, 然后注入到系统环境中, 日志系统在读取日志配置文件时, 会从系统环境中替换日志配置中的变量
     *
     * @since 1.0.0
     */
    @Override
    public void apply() {
        boolean isLocalLaunch = ConfigKit.isLocalLaunch();

        // 1. 设置使用到日志配置
        this.setLogConfigFile(isLocalLaunch);
        this.setShowLogLocation(isLocalLaunch);
        this.setLogAppName();
    }

    /**
     * 设置日志配置文件, 交由原逻辑处理.
     * 优先使用业务端设置的日志配置文件名, 如果未配置则会根据当前环境使用 log4j2-console.xml 或者 log4j2-file.xml 配置文件.
     *
     * @param isLocalLaunch is local launch
     * @see FkhLoggingListener#initializeSystem FkhLoggingListener#initializeSystem
     * @see LoggingApplicationListener#initializeSystem LoggingApplicationListener#initializeSystem
     * @since 1.0.0
     */
    private void setLogConfigFile(boolean isLocalLaunch) {
        // 设置当前环境, 通过 ${ctx:env} 读取
        MDC.put("env", "本地开发");
        LogAppenderType appenderType = LogAppenderType.CONSOLE;

        // 本地开发环境, 特指使用 IDE 启动的应用环境
        if (!isLocalLaunch) {
            MDC.put("env", "服务器部署");
            appenderType = LogAppenderType.FILE;
        }

        String logConfig = this.environment.getProperty(ConfigKey.LogSystemConfigKey.LOG_CONFIG, appenderType.getConfig());

        // 通过配置文件或使用 JVM 参数修改日志配置
        System.setProperty(LoggingApplicationListener.CONFIG_PROPERTY, StringUtils.format("classpath:{}", logConfig));
        JustOnceLogger.printOnce(AdditionalProcessor.class.getName(), "fkh.logging.config=" + logConfig);
    }

    /**
     * 设置是否显示跳转信息 (fkh.logging.enable-show-location), 用于本地开发快速跳转到日志输出位置
     *
     * @param isLocalLaunch is local launch
     * @since 1.0.0
     */
    private void setShowLogLocation(boolean isLocalLaunch) {

        String showLogLocation = this.resolver.getProperty(ConfigKey.LogSystemConfigKey.SHOW_LOG_LOCATION);

        // 本地开发且没有设置, 则默认开启
        if (isLocalLaunch && StringUtils.isBlank(showLogLocation)) {
            showLogLocation = ConfigDefaultValue.TRUE_STRING;
        }

        // 其他情况根据业务端配置为准
        boolean enableShowLocation = Boolean.parseBoolean(showLogLocation);
        if (!enableShowLocation) {
            // 如果不显示 location, 则需要替换 layout
            System.setProperty(Constants.SHOW_LOG_LOCATION_LAYOUT, "%clr{%c{1.}}{cyan}");
        }

        System.setProperty(Constants.SHOW_LOG_LOCATION, String.valueOf(enableShowLocation));
    }

    /**
     * 设置日志配置文件中的变量, 从配置文件中读取, 有则设置, 没有则不设置, 将使用日志配置文件中的默认配置
     *
     * @since 1.0.0
     */
    private void setLogAppName() {
        String applicationName = ConfigKit.getProperty(this.environment, ConfigKey.SpringConfigKey.APPLICATION_NAME);
        if (StringUtils.isBlank(applicationName)) {
            applicationName = this.environment.getProperty(ConfigKey.LogSystemConfigKey.LOG_APP_NAME);
        }
        // fkh.logging.app-name
        this.setSystemProperty(StringUtils.isBlank(applicationName) ? "Please-Inherit-FkhStarter" : applicationName, Constants.APP_NAME);

    }

}
