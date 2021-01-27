package com.yanger.tools.general.tools;

import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.format.StringFormatter;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import cn.hutool.core.lang.Assert;

/**
 * @Description 对日期操作的公共方法类
 * @Author yanger
 * @date 2018年10月13日
 */
public class DateUtils {

    /** The constant PATTERN_DATETIME. */
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /** The constant PATTERN_DATE. */
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /** 时分秒 */
    public static final String PATTERN_TIME = "HH:mm:ss";

    /** PATTERN_DATE_NO_SEPARATOR */
    public static final String PATTERN_DATE_NO_SEPARATOR = "yyyyMMdd";

    /** PATTERN_TIME_NO_SEPARATOR */
    public static final String PATTERN_TIME_NO_SEPARATOR = "HHmmss";

    /** PATTERN_DATETIME_NO_SEPARATOR */
    public static final String PATTERN_DATETIME_NO_SEPARATOR = PATTERN_DATE_NO_SEPARATOR + PATTERN_TIME_NO_SEPARATOR;

    /** 老 date 格式化 */
    public static final ConcurrentDateFormat DATETIME_FORMAT = ConcurrentDateFormat.of(PATTERN_DATETIME);

    /** The constant DATE_FORMAT. */
    public static final ConcurrentDateFormat DATE_FORMAT = ConcurrentDateFormat.of(PATTERN_DATE);

    /** The constant TIME_FORMAT. */
    public static final ConcurrentDateFormat TIME_FORMAT = ConcurrentDateFormat.of(PATTERN_TIME);

    /** java 8 时间格式化 */
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATETIME);

    /** The constant DATE_FORMATTER. */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_DATE);

    /** The constant TIME_FORMATTER. */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(DateUtils.PATTERN_TIME);

    /**
     * 添加年
     *
     * @param date       时间
     * @param yearsToAdd 添加的年数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusYears(Date date, int yearsToAdd) {
        return DateUtils.set(date, Calendar.YEAR, yearsToAdd);
    }

    /**
     * 设置日期属性
     *
     * @param date          时间
     * @param calendarField 更改的属性
     * @param amount        更改数,-1表示减少
     * @return 设置后的时间 date
     */
    @NotNull
    private static Date set(Date date, int calendarField, int amount) {
        Assert.notNull(date, "The date must not be null");
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 添加月
     *
     * @param date        时间
     * @param monthsToAdd 添加的月数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusMonths(Date date, int monthsToAdd) {
        return DateUtils.set(date, Calendar.MONTH, monthsToAdd);
    }

    /**
     * 添加周
     *
     * @param date       时间
     * @param weeksToAdd 添加的周数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusWeeks(Date date, int weeksToAdd) {
        return DateUtils.plus(date, Period.ofWeeks(weeksToAdd));
    }

    /**
     * 日期添加时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plus(@NotNull Date date, TemporalAmount amount) {
        Instant instant = date.toInstant();
        return Date.from(instant.plus(amount));
    }

    /**
     * 添加天
     *
     * @param date      时间
     * @param daysToAdd 添加的天数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusDays(Date date, long daysToAdd) {
        return DateUtils.plus(date, Duration.ofDays(daysToAdd));
    }

    /**
     * 添加小时
     *
     * @param date       时间
     * @param hoursToAdd 添加的小时数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusHours(Date date, long hoursToAdd) {
        return DateUtils.plus(date, Duration.ofHours(hoursToAdd));
    }

    /**
     * 添加分钟
     *
     * @param date         时间
     * @param minutesToAdd 添加的分钟数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusMinutes(Date date, long minutesToAdd) {
        return DateUtils.plus(date, Duration.ofMinutes(minutesToAdd));
    }

    /**
     * 添加秒
     *
     * @param date         时间
     * @param secondsToAdd 添加的秒数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusSeconds(Date date, long secondsToAdd) {
        return DateUtils.plus(date, Duration.ofSeconds(secondsToAdd));
    }

    /**
     * 添加毫秒
     *
     * @param date        时间
     * @param millisToAdd 添加的毫秒数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusMillis(Date date, long millisToAdd) {
        return DateUtils.plus(date, Duration.ofMillis(millisToAdd));
    }

    /**
     * 添加纳秒
     *
     * @param date       时间
     * @param nanosToAdd 添加的纳秒数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date plusNanos(Date date, long nanosToAdd) {
        return DateUtils.plus(date, Duration.ofNanos(nanosToAdd));
    }

    /**
     * 减少年
     *
     * @param date  时间
     * @param years 减少的年数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusYears(Date date, int years) {
        return DateUtils.set(date, Calendar.YEAR, -years);
    }

    /**
     * 减少月
     *
     * @param date   时间
     * @param months 减少的月数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusMonths(Date date, int months) {
        return DateUtils.set(date, Calendar.MONTH, -months);
    }

    /**
     * 减少周
     *
     * @param date  时间
     * @param weeks 减少的周数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusWeeks(Date date, int weeks) {
        return DateUtils.minus(date, Period.ofWeeks(weeks));
    }

    /**
     * 日期减少时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minus(@NotNull Date date, TemporalAmount amount) {
        Instant instant = date.toInstant();
        return Date.from(instant.minus(amount));
    }

    /**
     * 减少天
     *
     * @param date 时间
     * @param days 减少的天数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusDays(Date date, long days) {
        return DateUtils.minus(date, Duration.ofDays(days));
    }

    /**
     * 减少小时
     *
     * @param date  时间
     * @param hours 减少的小时数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusHours(Date date, long hours) {
        return DateUtils.minus(date, Duration.ofHours(hours));
    }

    /**
     * 减少分钟
     *
     * @param date    时间
     * @param minutes 减少的分钟数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusMinutes(Date date, long minutes) {
        return DateUtils.minus(date, Duration.ofMinutes(minutes));
    }

    /**
     * 减少秒
     *
     * @param date    时间
     * @param seconds 减少的秒数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusSeconds(Date date, long seconds) {
        return DateUtils.minus(date, Duration.ofSeconds(seconds));
    }

    /**
     * 减少毫秒
     *
     * @param date   时间
     * @param millis 减少的毫秒数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusMillis(Date date, long millis) {
        return DateUtils.minus(date, Duration.ofMillis(millis));
    }

    /**
     * 减少纳秒
     *
     * @param date  时间
     * @param nanos 减少的纳秒数
     * @return 设置后的时间 date
     */
    @NotNull
    public static Date minusNanos(Date date, long nanos) {
        return DateUtils.minus(date, Duration.ofNanos(nanos));
    }

    /**
     * 日期时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     */
    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    /**
     * 日期格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * 时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间 string
     */
    public static String formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }

    /**
     * java8 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     */
    @NotNull
    public static String formatDateTime(TemporalAccessor temporal) {
        return DATETIME_FORMATTER.format(temporal);
    }

    /**
     * java8 日期时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     */
    @NotNull
    public static String formatDate(TemporalAccessor temporal) {
        return DATE_FORMATTER.format(temporal);
    }

    /**
     * java8 时间格式化
     *
     * @param temporal 时间
     * @return 格式化后的时间 string
     */
    @NotNull
    public static String formatTime(TemporalAccessor temporal) {
        return TIME_FORMATTER.format(temporal);
    }

    /**
     * java8 日期格式化
     *
     * @param temporal 时间
     * @param pattern  表达式
     * @return 格式化后的时间 string
     */
    @NotNull
    public static String format(TemporalAccessor temporal, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(temporal);
    }

    /**
     * Parse date time
     *
     * @param dateStr date str
     * @return the date
     */
    public static Date parseDateTime(String dateStr) {
        return parse(dateStr, PATTERN_DATETIME);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间 date
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
            throw new RuntimeException(StringFormatter.format("不能将 {} 以 {} 格式转换为 Date 类型", dateStr, pattern), e);
        }
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param format  ConcurrentDateFormat
     * @return 时间 date
     */
    public static Date parse(String dateStr, @NotNull ConcurrentDateFormat format) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(StringFormatter.format("不能将 {} 以 {} 格式转换为 Date 类型", dateStr, format.getFormat()), e);
        }
    }

    /**
     * 将字符串转换为时间
     *
     * @param <T>     the type parameter
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @param query   the query
     * @return 时间 t
     */
    public static <T> T parse(String dateStr, String pattern, TemporalQuery<T> query) {
        return DateTimeFormatter.ofPattern(pattern).parse(dateStr, query);
    }

    /**
     * Instant 转 时间
     *
     * @param instant Instant
     * @return Instant local date time
     */
    @NotNull
    public static LocalDateTime toDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 转换成 date
     *
     * @param dateTime LocalDateTime
     * @return Date date
     */
    @NotNull
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(DateUtils.toInstant(dateTime));
    }

    /**
     * 时间转 Instant
     *
     * @param dateTime 时间
     * @return Instant instant
     */
    public static Instant toInstant(@NotNull LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * 转换成 date
     *
     * @param localDate LocalDate
     * @return Date date
     */
    @NotNull
    public static Date toDate(@NotNull LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Converts local date time to Calendar.
     *
     * @param localDateTime the local date time
     * @return the calendar
     */
    @NotNull
    public static Calendar toCalendar(LocalDateTime localDateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()));
    }

    /**
     * localDate 转换成毫秒数
     *
     * @param localDate LocalDate
     * @return long long
     */
    public static long toMilliseconds(@NotNull LocalDate localDate) {
        return toMilliseconds(localDate.atStartOfDay());
    }

    /**
     * localDateTime 转换成毫秒数
     *
     * @param localDateTime LocalDateTime
     * @return long long
     */
    public static long toMilliseconds(@NotNull LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 转换成java8 时间
     *
     * @param calendar 日历
     * @return LocalDateTime local date time
     */
    @NotNull
    public static LocalDateTime fromCalendar(@NotNull Calendar calendar) {
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    /**
     * 转换成java8 时间
     *
     * @param instant Instant
     * @return LocalDateTime local date time
     */
    @NotNull
    public static LocalDateTime fromInstant(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 转换成java8 时间
     *
     * @param date Date
     * @return LocalDateTime local date time
     */
    @NotNull
    public static LocalDateTime fromDate(@NotNull Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 转换成java8 时间
     *
     * @param milliseconds 毫秒数
     * @return LocalDateTime local date time
     */
    @NotNull
    public static LocalDateTime fromMilliseconds(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    /**
     * 比较2个时间差,跨度比较小
     *
     * @param startInclusive 开始时间
     * @param endExclusive   结束时间
     * @return 时间间隔 duration
     */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return Duration.between(startInclusive, endExclusive);
    }

    /**
     * 比较2个时间差,跨度比较大,年月日为单位
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间间隔 period
     */
    public static Period between(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate);
    }

    /**
     * 比较2个 时间差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间间隔 duration
     */
    public static Duration between(@NotNull Date startDate, @NotNull Date endDate) {
        return Duration.between(startDate.toInstant(), endDate.toInstant());
    }

    /**
     * Between
     *
     * @param startTime  start time
     * @param targetDate target date
     * @param endTime    end time
     * @return the boolean
     */
    public static boolean between(Date startTime, Date targetDate, Date endTime) {
        return startTime.getTime() <= targetDate.getTime()
               && endTime.getTime() >= targetDate.getTime();
    }

    /**
     * 将秒数转换为日时分秒
     *
     * @param second 秒数
     * @return 时间 string
     */
    public static String secondToTime(Long second) {
        // 判断是否为空
        if (second == null || second == 0L) {
            return StringPool.EMPTY;
        }
        //转换天数
        long days = second / 86400;
        //剩余秒数
        second = second % 86400;
        //转换小时
        long hours = second / 3600;
        //剩余秒数
        second = second % 3600;
        //转换分钟
        long minutes = second / 60;
        //剩余秒数
        second = second % 60;
        if (days > 0) {
            return StringFormatter.format("{}天{}小时{}分{}秒", days, hours, minutes, second);
        } else {
            return StringFormatter.format("{}小时{}分{}秒", hours, minutes, second);
        }
    }

    /**
     * 将字符串格式的时间转换为 long
     *
     * @param dateString the date string
     * @return the long
     */
    @NotNull
    @Contract(pure = true)
    public static Long timeToSecond(String dateString) {

        return 0L;
    }

    /**
     * 获取今天的日期
     *
     * @return 时间 string
     */
    public static String today() {
        return format(new Date(), "yyyyMMdd");
    }

    /**
     * Now date
     *
     * @return the date
     */
    @NotNull
    @Contract(" -> new")
    public static Date now() {
        return new Date();
    }

    /**
     * 日期格式化
     *
     * @param date    时间
     * @param pattern 表达式
     * @return 格式化后的时间 string
     */
    public static String format(Date date, String pattern) {
        return ConcurrentDateFormat.of(pattern).format(date);
    }

    /**
     * 获取当前时间到当日最后时间相差的分钟数
     *
     * @param nowTime the now time
     * @return int int
     */
    public static int diffMinutesTime(Date nowTime) {
        nowTime = null == nowTime ? now() : nowTime;

        Calendar todayLastTime = DateUtils.getCurrentCalendar();
        todayLastTime.set(Calendar.HOUR_OF_DAY, 23);
        todayLastTime.set(Calendar.MINUTE, 59);
        todayLastTime.set(Calendar.SECOND, 59);

        return (int) ((todayLastTime.getTimeInMillis() - nowTime.getTime()) / 1000 / 60);
    }

    /**
     * Diff minutes time int
     *
     * @return the int
     */
    public static int diffMinutesTime() {
        return diffMinutesTime(now());
    }

    /**
     * Gets current calendar *
     *
     * @return the current calendar
     */
    private static Calendar getCurrentCalendar() {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        calendar.setTimeZone(timeZone);
        return calendar;
    }

    /**
     * 格式化日期的方法
     *
     * @param date：带格式化的日期
     * @param pattern：格式化的表达式，比如 yyyy-MM-dd
     * @return 格式化后的字符串
     * @date 2018年10月13日-下午4:27:03
     */
    public static String formatDate(Date date, String pattern) {
        // 建立日期FORMAT的实例
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 获取N天以后的日期对象
     *
     * @param date：传入的日期对象
     * @param n：往后顺延的天数
     * @return 顺延后的日期对象
     * @date 2018年10月13日-下午4:42:58
     */
    public static Date getPreviousNDate(Date date, long n) {
        long time = date.getTime();
        // 用毫秒数来计算新的日期
        time = time - n * 24 * 60 * 60 * 1000;
        return new Date(time);
    }

    /**
     * 获取N天以前的日期对象
     *
     * @param date：传入的日期对象
     * @param n：往后顺延的天数
     * @return 往前推算后的日期对象
     * @date 2018年10月13日-下午5:03:07
     */
    public static Date getNextNDate(Date date, long n) {
        long time = date.getTime();
        // 用毫秒数来计算新的日期
        time = time + n * 24 * 60 * 60 * 1000;
        return new Date(time);
    }

    /**
     * 计算传入日期的前一天
     *
     * @param date：传入日期
     * @return 传入日期的前一天的日期对象
     * @date 2018年10月13日-下午5:04:05
     */
    public static Date getPreviousDate(Date date) {
        return getPreviousNDate(date, 1);
    }

    /**
     * 计算传入日期的后一天
     *
     * @param date：传入日期
     * @return 传入日期的后一天的日期对象
     * @date 2018年10月13日-下午5:04:33
     */
    public static Date getNextDate(Date date) {
        return getNextNDate(date, 1);
    }

    /**
     * 根据起始日期、终止日期计算天数
     *
     * @param startDate：起始日期
     * @param endDate：终止日期
     * @return 天数区间
     * @date 2018年10月13日-下午5:04:57
     */
    public static int getDaysCount(Date startDate, Date endDate) {
        return getDaysCount(startDate, 0, endDate, 0);
    }

    /**
     * 根据年数返回所包含的天数
     *
     * @param year：年数
     * @return 该年所包含的天数
     * @date 2018年10月13日-下午5:05:24
     */
    public static int getDaysFromYear(int year) {
        // 判断是平年还是闰年
        if ((year % 400 == 0) || (year % 100 != 0) && (year % 4 == 0)) {
            return 366;
        } else {
            return 365;
        }
    }

    /**
     * 根据起始日期、起始时间、终止日期、终止时间计算天数
     *
     * @param startDate：起始日期
     * @param startHour：起始小时
     * @param endDate：终止日期
     * @param endHour：终止小时
     * @return 根据起始日期、起始时间、终止日期、终止时间计算天数
     * @date 2018年10月13日-下午5:08:22
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
     * 比较两个日期的大小
     *
     * @param startDate：开始日期
     * @param endDate：结束日期
     * @return 开始日期小于结束日期 0 ： 开始日期等于结束日期 1 ： 开始日期大于结束日期
     * @date 2018年10月13日-下午5:08:52
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

}
