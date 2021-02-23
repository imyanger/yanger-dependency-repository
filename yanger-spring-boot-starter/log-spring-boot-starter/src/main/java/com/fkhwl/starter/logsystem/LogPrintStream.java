package com.fkhwl.starter.logsystem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 替换 系统 System.err 和 System.out 为log </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 12:48
 * @since 1.0.0
 */
@Slf4j
public final class LogPrintStream extends PrintStream {
    /** Error */
    private final boolean error;

    /**
     * Log print stream
     *
     * @param error error
     * @since 1.0.0
     */
    private LogPrintStream(boolean error) {
        super(error ? System.err : System.out);
        this.error = error;
    }

    /**
     * Out log print stream.
     *
     * @return the log print stream
     * @since 1.0.0
     */
    @NotNull
    @Contract(" -> new")
    public static LogPrintStream out() {
        return new LogPrintStream(false);
    }

    /**
     * Err log print stream.
     *
     * @return the log print stream
     * @since 1.0.0
     */
    @NotNull
    @Contract(" -> new")
    public static LogPrintStream err() {
        return new LogPrintStream(true);
    }

    /**
     * Printf.
     *
     * @param s the s
     * @since 1.0.0
     */
    @Override
    public void print(String s) {
        if (error) {
            log.error(s);
        } else {
            log.info(s);
        }
    }

    /**
     * 重写掉它,因为它会打印很多无用的新行
     *
     * @since 1.0.0
     */
    @Override
    public void println() {

    }

    /**
     * Printl.
     *
     * @param x the x
     * @since 1.0.0
     */
    @Override
    public void println(String x) {
        if (error) {
            log.error(x);
        } else {
            log.info(x);
        }
    }

    /**
     * Print print stream.
     *
     * @param format the format
     * @param args   the args
     * @return the print stream
     * @since 1.0.0
     */
    @Override
    public PrintStream printf(String format, Object... args) {
        if (error) {
            log.error(String.format(format, args));
        } else {
            log.info(String.format(format, args));
        }
        return this;
    }

    /**
     * Print print stream.
     *
     * @param l      the l
     * @param format the format
     * @param args   the args
     * @return the print stream
     * @since 1.0.0
     */
    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        if (error) {
            log.error(String.format(l, format, args));
        } else {
            log.info(String.format(l, format, args));
        }
        return this;
    }
}
