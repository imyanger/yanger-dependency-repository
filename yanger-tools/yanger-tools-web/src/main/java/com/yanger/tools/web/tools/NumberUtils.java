package com.yanger.tools.web.tools;

import cn.hutool.core.util.NumberUtil;
import org.jetbrains.annotations.Contract;
import org.springframework.lang.Nullable;

/**
 * NumberUtils
 * @Author yanger
 * @Date 2020/12/21 10:10
 */
public class NumberUtils extends org.springframework.util.NumberUtils {

    /**
     * All possible chars for representing a number as a String
     */
    static final char[] DIGITS = {
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F',
        'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z'
    };

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     * <pre>
     *   NumberUtil.toInt(null) = 0
     *   NumberUtil.toInt("")   = 0
     *   NumberUtil.toInt("1")  = 1
     * </pre>
     * @param str the string to convert, may be null
     * @return the int represented by the string, or <code>zero</code> if     conversion fails
     */
    public static int toInt(String str) {
        return toInt(str, -1);
    }

    /**
     * Is number
     * @param str str
     * @return the boolean
     */
    @Contract("null -> false")
    public static boolean isNumber(String str) {
        return NumberUtil.isNumber(str);
    }

    /**
     * <p>Convert a <code>String</code> to an <code>int</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   NumberUtil.toInt(null, 1) = 1
     *   NumberUtil.toInt("", 1)   = 1
     *   NumberUtil.toInt("1", 0)  = 1
     * </pre>
     * @param str          the string to convert, may be null
     * @param defaultValue the default value
     * @return the int represented by the string, or the default if conversion fails
     */
    @Contract("null, _ -> param2")
    public static int toInt(@Nullable String str, int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning
     * <code>zero</code> if the conversion fails.</p>
     * <p>If the string is <code>null</code>, <code>zero</code> is returned.</p>
     * <pre>
     *   NumberUtil.toLong(null) = 0L
     *   NumberUtil.toLong("")   = 0L
     *   NumberUtil.toLong("1")  = 1L
     * </pre>
     * @param str the string to convert, may be null
     * @return the long represented by the string, or <code>0</code> if     conversion fails
     */
    public static long toLong(String str) {
        return toLong(str, 0L);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>long</code>, returning a
     * default value if the conversion fails.</p>
     * <p>If the string is <code>null</code>, the default value is returned.</p>
     * <pre>
     *   NumberUtil.toLong(null, 1L) = 1L
     *   NumberUtil.toLong("", 1L)   = 1L
     *   NumberUtil.toLong("1", 0L)  = 1L
     * </pre>
     * @param str          the string to convert, may be null
     * @param defaultValue the default value
     * @return the long represented by the string, or the default if conversion fails
     */
    @Contract("null, _ -> param2")
    public static long toLong(@Nullable String str, long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     * @param value value
     * @return double value
     */
    @Contract("!null -> !null")
    public static Double toDouble(String value) {
        return toDouble(value, null);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     * @param value        value
     * @param defaultValue 默认值
     * @return double value
     */
    @Contract("!null, _ -> !null; null, _ -> param2")
    public static Double toDouble(@Nullable String value, Double defaultValue) {
        if (value != null) {
            return Double.valueOf(value.trim());
        }
        return defaultValue;
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     * @param value value
     * @return double value
     */
    @Contract("!null -> !null")
    public static Float toFloat(String value) {
        return toFloat(value, null);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>
     * @param value        value
     * @param defaultValue 默认值
     * @return double value
     */
    @Contract("!null, _ -> !null; null, _ -> param2")
    public static Float toFloat(@Nullable String value, Float defaultValue) {
        if (value != null) {
            return Float.valueOf(value.trim());
        }
        return defaultValue;
    }

}
