package com.yanger.tools.general.tools;

import cn.hutool.core.lang.Assert;
import com.yanger.tools.general.constant.DateConstant;
import com.yanger.tools.general.format.DateFormat;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 对日期时间操作的公共方法类
 * @Author yanger
 * @Date 2018/12/29 20:05
 */
public class DateUtils {

    /**
     * LocalDateTime 转 Date
     * @param dateTime LocalDateTime对象
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    @NotNull
    public static Date fromLocalDateTime(LocalDateTime dateTime) {
        return Date.from(convertLocalDateTime2Instant(dateTime));
    }

    /**
     * LocalDate 转 Date
     * @param localDate LocalDate对象
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    @NotNull
    public static Date fromLocalDate(@NotNull LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date 转 LocalDateTime
     * @param date Date对象
     * @return {@link LocalDateTime}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    @NotNull
    public static LocalDateTime toLocalDateTime(@NotNull Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDate 转 Long
     * @param localDate LocalDate对象
     * @return {@link long}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    public static long convertLocalDate2Long(@NotNull LocalDate localDate) {
        return convertLocalDateTime2Long(localDate.atStartOfDay());
    }

    /**
     * LocalDateTime 转 Long
     * @param localDateTime LocalDateTime对象
     * @return {@link long}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    public static long convertLocalDateTime2Long(@NotNull LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Long 转 LocalDateTime
     * @param milliseconds long 型时间毫秒数
     * @return {@link LocalDateTime}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    @NotNull
    public static LocalDateTime convertLong2LocalDateTime(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    /**
     * Long 转 Instant
     * @param dateTime LocalDateTime 对象
     * @return {@link Instant}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    public static Instant convertLocalDateTime2Instant(@NotNull LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * Instant 转 LocalDateTime
     * @param instant Instant 对象
     * @return {@link LocalDateTime}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    @NotNull
    public static LocalDateTime convertInstant2LocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转 Calendar
     * @param localDateTime LocalDateTime 对象
     * @return {@link Calendar}
     * @Author yanger
     * @Date 2021/12/04 23:32
     */
    @NotNull
    public static Calendar convertLocalDateTime2Calendar(LocalDateTime localDateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }

    /**
     * LocalDateTime 转 Calendar
     * @param calendar Calendar 对象
     * @return {@link LocalDateTime}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static LocalDateTime convertCalendar2LocalDateTime(@NotNull Calendar calendar) {
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    /**
     * 获取当前时间
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    @Contract(" -> new")
    public static Date now() {
        return new Date();
    }

    /**
     * 获取当前时间字符串
     * @return {@link String}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    public static String nowOfString() {
        return DateFormat.format(new Date(), DateConstant.PATTERN_DATE);
    }

    /**
     * 增加年
     * @param date 当前时间
     * @param years 年数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date plusYears(Date date, int years) {
        return reset(date, Calendar.YEAR, years);
    }

    /**
     * 增加月
     * @param date 当前时间
     * @param months 月数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date plusMonths(Date date, int months) {
        return reset(date, Calendar.MONTH, months);
    }

    /**
     * 增加周
     * @param date  当前时间
     * @param weeks 周数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date plusWeeks(Date date, int weeks) {
        return plus(date, Period.ofWeeks(weeks));
    }

    /**
     * 增加天
     * @param date 当前时间
     * @param days 天数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date plusDays(Date date, long days) {
        return plus(date, Duration.ofDays(days));
    }

    /**
     * 增加小时
     * @param date 当前时间
     * @param hours 小时数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date plusHours(Date date, long hours) {
        return plus(date, Duration.ofHours(hours));
    }

    /**
     *
     * @param date 当前时间
     * @param minutes 分钟数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date plusMinutes(Date date, long minutes) {
        return plus(date, Duration.ofMinutes(minutes));
    }

    /**
     * 增加秒
     * @param date 当前时间
     * @param seconds 秒数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date plusSeconds(Date date, long seconds) {
        return plus(date, Duration.ofSeconds(seconds));
    }

    /**
     * 增加毫秒
     * @param date 当前时间
     * @param millis 毫秒数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date plusMillis(Date date, long millis) {
        return plus(date, Duration.ofMillis(millis));
    }

    /**
     * 减少年
     * @param date 当前时间
     * @param years 年数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date minusYears(Date date, int years) {
        return reset(date, Calendar.YEAR, -years);
    }

    /**
     * 减少月
     * @param date 当前时间
     * @param months 月数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:33
     */
    @NotNull
    public static Date minusMonths(Date date, int months) {
        return reset(date, Calendar.MONTH, -months);
    }

    /**
     * 减少周
     * @param date 当前时间
     * @param weeks 周数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    public static Date minusWeeks(Date date, int weeks) {
        return DateUtils.minus(date, Period.ofWeeks(weeks));
    }

    /**
     * 减少天
     * @param date 当前时间
     * @param days 天数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    public static Date minusDays(Date date, long days) {
        return DateUtils.minus(date, Duration.ofDays(days));
    }

    /**
     * 减少小时
     * @param date 当前时间
     * @param hours 小时数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    public static Date minusHours(Date date, long hours) {
        return DateUtils.minus(date, Duration.ofHours(hours));
    }

    /**
     * 减少分钟
     * @param date 当前时间
     * @param minutes 分钟数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    public static Date minusMinutes(Date date, long minutes) {
        return DateUtils.minus(date, Duration.ofMinutes(minutes));
    }

    /**
     * 减少秒
     * @param date 当前时间
     * @param seconds 秒数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    public static Date minusSeconds(Date date, long seconds) {
        return DateUtils.minus(date, Duration.ofSeconds(seconds));
    }

    /**
     * 减少毫秒
     * @param date 当前时间
     * @param millis 毫秒数
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    public static Date minusMillis(Date date, long millis) {
        return DateUtils.minus(date, Duration.ofMillis(millis));
    }

    /**
     * 设置日期属性
     * @param date 当前时间
     * @param calendarField calendar 属性
     * @param amount 变化量
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    private static Date reset(Date date, int calendarField, int amount) {
        Assert.notNull(date, "The date must not be null");
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 日期增加时间量
     * @param date 当前时间
     * @param amount 增加量
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    public static Date plus(@NotNull Date date, TemporalAmount amount) {
        Instant instant = date.toInstant();
        return Date.from(instant.plus(amount));
    }

    /**
     * 日期减少时间量
     * @param date 当前时间
     * @param amount 减少量
     * @return {@link Date}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    @NotNull
    public static Date minus(@NotNull Date date, TemporalAmount amount) {
        Instant instant = date.toInstant();
        return Date.from(instant.minus(amount));
    }

    /**
     * 比较两个日期的大小
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return {@link int} -1：开始日期小于结束日期  0：开始日期等于结束日期  1 ：开始日期大于结束日期
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    public static int compareDate(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        if (startTime < endTime) {
            return -1;
        }
        if (startTime == endTime) {
            return 0;
        }
        return 1;
    }

    /**
     * 根据起始日期、终止日期计算天数
     * @param startDate 起始日期
     * @param endDate 终止日期
     * @return {@link int} 相隔天数
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    public static int getDaysCount(Date startDate, Date endDate) {
        return getDaysCount(startDate, 0, endDate, 0);
    }

    /**
     * 根据起始日期、起始时间、终止日期、终止时间计算天数
     * @param startDate 起始日期
     * @param startHour 起始小时
     * @param endDate 终止日期
     * @param endHour 终止小时
     * @return {@link int} 相隔天数
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    public static int getDaysCount(Date startDate, int startHour, Date endDate, int endHour) {
        // 根据起始日期计算起始的毫秒
        long startTime = startDate.getTime();
        // 根据终止日期计算终止的毫秒
        long endTime = endDate.getTime();
        // 通过起始毫秒和终止毫秒的差值，计算天数
        int dayCount = (int) ((endTime - startTime) / (24 * 60 * 60 * 1000) + 1);
        if (endHour <= startHour) {
            if (startHour == 24 && endHour == 0) {
                dayCount = dayCount - 2;
            } else {
                dayCount = dayCount - 1;
            }
        }
        return dayCount;

    }

    /**
     * 比较2个时间差，跨度比较小
     * @param startInclusive 开始时间
     * @param endExclusive 结束时间
     * @return {@link Duration}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    /**
     * 比较2个时间差，跨度比较大，年月日为单位
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return {@link Period}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    public static Period between(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate);
    }

    /**
     * 比较2个时间差
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return {@link Duration}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    public static Duration between(@NotNull Date startDate, @NotNull Date endDate) {
        return Duration.between(startDate.toInstant(), endDate.toInstant());
    }

    /**
     * 是否是闰年
     * @param year 年份
     * @return {@link boolean} true：闰年   false：平年
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    public static boolean isLeapYear(int year){
        return (year % 400 == 0) || (year % 100 != 0) && (year % 4 == 0);
    }

    /**
     * 根据年数返回所包含的天数
     * @param year 年份
     * @return {@link int}
     * @Author yanger
     * @Date 2021/12/04 23:34
     */
    public static int getDaysFromYear(int year) {
        return isLeapYear(year) ? 366 : 365;
    }

}
