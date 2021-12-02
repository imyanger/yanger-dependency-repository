package com.yanger.tools.general.constant;

/**
 * 时间常用常量
 * @Author yanger
 * @Date 2021/12/2/002 20:47
 */
public class DateConstant {

    /** 标准日期时间格式 */
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /** 标准日期格式  */
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /** 标准时间格式 */
    public static final String PATTERN_TIME = "HH:mm:ss";

    /** 没有分割符的日期格式 */
    public static final String PATTERN_DATE_NO_SEPARATOR = "yyyyMMdd";

    /** 没有分割符的时间格式 */
    public static final String PATTERN_TIME_NO_SEPARATOR = "HHmmss";

    /** 没有分割符的日期日期格式 */
    public static final String PATTERN_DATETIME_NO_SEPARATOR = PATTERN_DATE_NO_SEPARATOR + PATTERN_TIME_NO_SEPARATOR;

}
