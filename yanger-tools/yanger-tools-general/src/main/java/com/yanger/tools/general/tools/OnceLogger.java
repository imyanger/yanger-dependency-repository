package com.yanger.tools.general.tools;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 只打印一次的日志
 * @Author yanger
 * @Date 2020/12/29 17:59
 */
@Slf4j
@UtilityClass
public class OnceLogger {

    /** KNOWN_LOGS */
    private static final Map<String, Set<String>> KNOWN_LOGS = new ConcurrentHashMap<>();

    /**
     * Warn
     *
     * @param loggerName logger name
     * @param message    message
     */
    public static void warnOnce(String loggerName, String message) {
        if (!log.isWarnEnabled()) {
            return;
        }
        if (check(loggerName, message)) {
            return;
        }
        log.warn(message);
    }

    /**
     * Print
     *
     * @param loggerName logger name
     * @param message    message
     */
    public static void printOnce(String loggerName, String message) {
        if (check(loggerName, message)) {
            return;
        }
        System.err.println(message);
    }

    /**
     * Check
     *
     * @param loggerName logger name
     * @param message    message
     * @return the boolean
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
