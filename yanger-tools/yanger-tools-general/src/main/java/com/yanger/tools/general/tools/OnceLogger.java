package com.yanger.tools.general.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 只打印一次的日志
 * @Author yanger
 * @Date 2020/12/29 17:59
 */
@Slf4j
@UtilityClass
public class OnceLogger {

    /** print 记录 */
    private static final Map<String, Set<String>> KNOWN_LOGS = new ConcurrentHashMap<>();

    /**
     * 输出 info 日志
     * @param loggerName logger 对象名称
     * @param message 打印的日志信息
     * @Author yanger
     * @Date 2022/01/06 0:44
     */
    public static void info(String loggerName, String message) {
        if (!log.isInfoEnabled()) {
            return;
        }
        if (check(loggerName, message)) {
            return;
        }
        log.info(message);
    }

    /**
     * 输出 warn 日志
     * @param loggerName logger 对象名称
     * @param message 打印的日志信息
     * @Author yanger
     * @Date 2022/01/06 0:44
     */
    public static void warn(String loggerName, String message) {
        if (!log.isWarnEnabled()) {
            return;
        }
        if (check(loggerName, message)) {
            return;
        }
        log.warn(message);
    }

    /**
     * 输出 error 日志
     * @param loggerName logger 对象名称
     * @param message 打印的日志信息
     * @Author yanger
     * @Date 2022/01/06 0:44
     */
    public static void error(String loggerName, String message) {
        if (!log.isErrorEnabled()) {
            return;
        }
        if (check(loggerName, message)) {
            return;
        }
        log.error(message);
    }

    /**
     * 输出日志到控制台（error 级别）
     * @param loggerName logger 对象名称
     * @param message 打印的日志信息
     * @Author yanger
     * @Date 2022/01/06 0:44
     */
    public static void print(String loggerName, String message) {
        if (check(loggerName, message)) {
            return;
        }
        System.err.println(message);
    }

    /**
     * 判断是否已经打印
     * @param loggerName logger 对象名称
     * @param message 打印的日志信息
     * @return {@link boolean}
     * @Author yanger
     * @Date 2022/01/06 0:48
     */
    private static boolean check(String loggerName, String message) {
        if (!KNOWN_LOGS.containsKey(loggerName)) {
            KNOWN_LOGS.put(loggerName, new ConcurrentSkipListSet<>(Collections.singleton(message)));
        } else {
            Set<String> messages = KNOWN_LOGS.get(loggerName);
            if (messages.contains(message)) {
                return true;
            }
            messages.add(message);
        }
        return false;
    }

}
