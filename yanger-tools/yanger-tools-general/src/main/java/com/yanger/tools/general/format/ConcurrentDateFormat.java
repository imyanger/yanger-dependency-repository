package com.yanger.tools.general.format;

import com.yanger.tools.general.constant.DateConstant;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 安全的时间格式化
 * 参考 tomcat8 中的并发 DateFormat, {@link SimpleDateFormat}的线程安全包装器. 不使用ThreadLocal,创建足够的SimpleDateFormat对象来满足并发性要求.
 * @Author yanger
 * @Date 2020/12/21 9:54
 */
public class ConcurrentDateFormat {

    /** date format 格式字符串 */
    @Getter
    private final String format;

    /** Locale */
    private final Locale locale;

    /** Timezone */
    private final TimeZone timezone;

    /** Queue */
    private final Queue<SimpleDateFormat> queue = new ConcurrentLinkedQueue<>();

    /**
     * 构造方法私有化，仅内部调用
     * @param format
     * @param locale
     * @param timezone
     * @Author yanger
     * @Date 2021/11/30 22:59
     */
    private ConcurrentDateFormat(String format, Locale locale, TimeZone timezone) {
        this.format = format;
        this.locale = locale;
        this.timezone = timezone;
        SimpleDateFormat initial = this.createInstance();
        this.queue.add(initial);
    }

    /**
     * Create instance of SimpleDateFormat
     * @param
     * @return {@link java.text.SimpleDateFormat}
     * @Author yanger
     * @Date 2021/11/30 23:01
     */
    @NotNull
    private SimpleDateFormat createInstance() {
        SimpleDateFormat sdf = new SimpleDateFormat(this.format, this.locale);
        sdf.setTimeZone(this.timezone);
        return sdf;
    }

    /**
     * 默认格式：yyyy-MM-dd HH:mm:ss
     * @return {@link com.yanger.tools.general.format.ConcurrentDateFormat}
     * @Author yanger
     * @Date 2021/11/30 23:03
     */
    @NotNull
    @Contract(" -> new")
    public static ConcurrentDateFormat of() {
        return new ConcurrentDateFormat(DateConstant.PATTERN_DATETIME, Locale.getDefault(), TimeZone.getDefault());
    }

    /**
     * Create new instance
     * @param format 时间格式， eg: yyyy-MM-dd HH:mm:ss
     * @return {@link com.yanger.tools.general.format.ConcurrentDateFormat}
     * @Author yanger
     * @Date 2021/11/30 23:08
     */
    @NotNull
    @Contract("_ -> new")
    public static ConcurrentDateFormat of(String format) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), TimeZone.getDefault());
    }

    /**
     * Create new instance
     * @param format 时间格式， eg: yyyy-MM-dd HH:mm:ss
     * @param timezone 时区
     * @return {@link ConcurrentDateFormat}
     * @Author yanger
     * @Date 2021/11/30 23:08
     */
    @NotNull
    @Contract("_, _ -> new")
    public static ConcurrentDateFormat of(String format, TimeZone timezone) {
        return new ConcurrentDateFormat(format, Locale.getDefault(), timezone);
    }

    /**
     * Create new instance
     * @param format 时间格式， eg: yyyy-MM-dd HH:mm:ss
     * @param locale country(region)
     * @param timezone 时区
     * @return {@link ConcurrentDateFormat}
     * @Author yanger
     * @Date 2021/11/30 23:08
     */
    @NotNull
    @Contract("_, _, _ -> new")
    public static ConcurrentDateFormat of(String format, Locale locale, TimeZone timezone) {
        return new ConcurrentDateFormat(format, locale, timezone);
    }

    /**
     * 格式化时间
     * @param date 待格式的 Date
     * @return {@link String}
     * @Author yanger
     * @Date 2021/11/30 23:10
     */
    public @NotNull String format(Date date) {
        SimpleDateFormat sdf = this.queue.poll();
        if (sdf == null) {
            sdf = this.createInstance();
        }
        String result = sdf.format(date);
        this.queue.add(sdf);
        return result;
    }

    /**
     * 解析字符为 Date
     * @param source 时间字符串
     * @return {@link Date}
     * @throws ParseException 时间格式错误
     * @Author yanger
     * @Date 2021/11/30 23:11
     */
    public Date parse(String source) throws ParseException {
        SimpleDateFormat sdf = this.queue.poll();
        if (sdf == null) {
            sdf = this.createInstance();
        }
        Date result = sdf.parse(source);
        this.queue.add(sdf);
        return result;
    }

}
