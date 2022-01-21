package com.yanger.tools.general.format;

import com.yanger.tools.general.constant.DateConstant;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;
import java.util.Date;

/**
 * Date 格式化工具
 * @Author yanger
 * @Date 2021/12/4/004 16:16
 */
public class DateFormat {

    /** 日期时间格式化对象：yyyy-MM-dd HH:mm:ss */
    public static final ConcurrentDateFormat DATETIME_FORMAT = ConcurrentDateFormat.of(DateConstant.PATTERN_DATETIME);

    /** 日期格式化对象：yyyy-MM-dd */
    public static final ConcurrentDateFormat DATE_FORMAT = ConcurrentDateFormat.of(DateConstant.PATTERN_DATE);

    /** 时间格式化对象：HH:mm:ss */
    public static final ConcurrentDateFormat TIME_FORMAT = ConcurrentDateFormat.of(DateConstant.PATTERN_TIME);

    /**
     * 日期时间格式化
     * @param date 时间
     * @return {@link String} 格式化后的时间 string
     * @Author yanger
     * @Date 2021/12/04 16:26
     */
    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    /**
     * 日期格式化
     * @param date 时间
     * @return {@link String} 格式化后的时间 string
     * @Author yanger
     * @Date 2021/12/04 16:26
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * 时间格式化
     * @param date 时间
     * @return {@link String} 格式化后的时间 string
     * @Author yanger
     * @Date 2021/12/04 16:26
     */
    public static String formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }

    /**
     * Date 格式化
     * @param date 时间
     * @param pattern 格式化字符串
     * @return {@link String} 格式化后的时间 string
     * @Author yanger
     * @Date 2021/12/04 16:26
     */
    public static String format(Date date, String pattern) {
        return ConcurrentDateFormat.of(pattern).format(date);
    }

    /**
     * Date 字符串解析为 Date
     * @param dateStr Date 字符串
     * @param pattern 格式化字符串
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 16:26
     */
    @Nullable
    public static Date parse(String dateStr, String pattern) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        ConcurrentDateFormat format = ConcurrentDateFormat.of(pattern);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(StringFormat.format("不能将 {} 以 {} 格式转换为 Date 类型", dateStr, pattern), e);
        }
    }

    /**
     * 时间字符串解析为 Date
     * @param dateStr Date 字符串
     * @param format ConcurrentDateFormat 格式化对象
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 16:26
     */
    public static Date parse(String dateStr, @NotNull ConcurrentDateFormat format) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(StringFormat.format("不能将 {} 以 {} 格式转换为 Date 类型", dateStr, format.getFormat()), e);
        }
    }

    /**
     * 时间字符串解析为 Date
     * @param dateStr Date 字符串
     * @param pattern 格式化字符串
     * @param query TemporalQuery 实现
     * @return {@link T}
     * @Author yanger
     * @Date 2021/12/04 16:26
     */
    public static <T> T parse(String dateStr, String pattern, TemporalQuery<T> query) {
        return DateTimeFormatter.ofPattern(pattern).parse(dateStr, query);
    }

}
