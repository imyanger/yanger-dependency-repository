package com.fkhwl.starter.logsystem.plugin;

import com.fkhwl.starter.basic.context.Trace;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.message.Message;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: com.fkhwl.starter.log4j2 自定义插件 应用类型输出, TMS/驾驶员APP/千迅智运APP/xxxx </p>
 * {@link LogEventPatternConverter} 接口用于扩展 pattern, 类似的有 %d (时间), %C (classname) 等
 * 在 com.fkhwl.starter.log4j2 配置 '%appType', 将会输出 AT:xxxx
 * com.fkhwl.starter.log4j2 插件相关 {@link Plugin}
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.26 21:41
 * @since 1.0.0
 */
@Plugin(name = "ApplicationTypeConverter", category = PatternConverter.CATEGORY)
@ConverterKeys(value = {"appType"})
public class ApplicationTypeConverter extends LogEventPatternConverter {

    /**
     * Instantiates a new Application type converter.
     *
     * @param name  the name
     * @param style the style
     * @since 1.0.0
     */
    protected ApplicationTypeConverter(String name, String style) {
        super(name, style);
    }

    /**
     * 必须实现 new Instance 方法, 被 com.fkhwl.starter.log4j2 调用
     *
     * @param options the options
     * @return the application type converter
     * @since 1.0.0
     */
    @NotNull
    @Contract("_ -> new")
    public static ApplicationTypeConverter newInstance(String[] options) {
        return new ApplicationTypeConverter("appType", "appType");
    }

    /**
     * Formats.
     *
     * @param event      the event          系统已经存在的一些可选数据
     * @param toAppendTo the to append to   最终的输出字符流
     * @since 1.0.0
     */
    @Override
    public void format(@NotNull LogEvent event, StringBuilder toAppendTo) {
        Message msg = event.getMessage();
        if (msg != null) {
            toAppendTo.append(generaterLogId());
        }
    }

    /**
     * 业务日志全局UUID
     *
     * @return string string
     * @since 1.0.0
     */
    @NotNull
    private static String generaterLogId() {
        String traceId = Trace.context().get();
        return traceId == null ? "" : traceId;
    }
}
