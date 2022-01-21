package com.yanger.tools.general.format;

import com.yanger.tools.general.constant.DateConstant;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * DateTime 工具类
 * @Author yanger
 * @Date 2021/1/27 17:46
 */
@UtilityClass
public class DateTimeFormat {

    /** 日期时间格式化对象：yyyy-MM-dd HH:mm:ss */
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern(DateConstant.PATTERN_DATETIME);

    /** 日期格式化对象：yyyy-MM-dd */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DateConstant.PATTERN_DATE);

    /** 时间格式化对象：HH:mm:ss */
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(DateConstant.PATTERN_TIME);

    /**
     * 日期时间格式化
     * @param temporal TemporalAccessor 接口实现
     * @return {@link String} 格式化后的日期时间字符串
     * @Author yanger
     * @Date 2021/12/02 21:09
     */
    @NotNull
    public static String formatDateTime(TemporalAccessor temporal) {
        return DATETIME_FORMAT.format(temporal);
    }

    /**
     * 日期格式化
     * @param temporal TemporalAccessor 接口实现
     * @return {@link String} 格式化后的日期字符串
     * @Author yanger
     * @Date 2021/12/02 21:09
     */
    @NotNull
    public static String formatDate(TemporalAccessor temporal) {
        return DATE_FORMAT.format(temporal);
    }

    /**
     * 时间格式化
     * @param temporal TemporalAccessor 接口实现
     * @return {@link String} 格式化后的时间字符串
     * @Author yanger
     * @Date 2021/12/02 21:09
     */
    @NotNull
    public static String formatTime(TemporalAccessor temporal) {
        return TIME_FORMAT.format(temporal);
    }

    /**
     * 日期时间格式化
     * @param temporal TemporalAccessor 接口实现
     * @param pattern 日期时间格式
     * @return {@link String} 格式化后的日期时间字符串
     * @Author yanger
     * @Date 2021/12/02 21:10
     */
    @NotNull
    public static String format(TemporalAccessor temporal, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(temporal);
    }

    /**
     * 将字符串转换为时间
     * @param dateStr 日期时间字符串
     * @param pattern 日期时间格式
     * @return {@link TemporalAccessor}
     * @Author yanger
     * @Date 2021/12/02 21:10
     */
    @NotNull
    public static TemporalAccessor parse(String dateStr, String pattern) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        return format.parse(dateStr);
    }

    /**
     * 将字符串转换为时间
     * @param dateStr 日期时间字符串
     * @param formatter 日期时间格式化 DateTimeFormatter 对象
     * @return {@link TemporalAccessor}
     * @Author yanger
     * @Date 2021/12/02 21:10
     */
    @NotNull
    public static TemporalAccessor parse(String dateStr, @NotNull DateTimeFormatter formatter) {
        return formatter.parse(dateStr);
    }

}
