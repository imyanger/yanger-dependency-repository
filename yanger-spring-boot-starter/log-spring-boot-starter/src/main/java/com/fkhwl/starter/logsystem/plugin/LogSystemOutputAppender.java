package com.fkhwl.starter.logsystem.plugin;

import com.fkhwl.starter.basic.util.StringPool;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 默认输出 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 18:19
 * @since 1.0.0
 */
public class LogSystemOutputAppender {

    /**
     * Append.
     *
     * @param toAppendTo the to append to
     * @since 1.0.0
     */
    public static void append(@NotNull StringBuilder toAppendTo) {
        toAppendTo.append("AT: " + StringPool.NULL_STRING);
    }
}
