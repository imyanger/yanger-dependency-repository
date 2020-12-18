package com.yanger.general.format;

import com.yanger.general.constant.CharPool;
import com.yanger.general.constant.StringPool;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

import lombok.experimental.UtilityClass;

/**
 * @Description 字符串格式化
 * @Author yanger
 * @Date 2020/12/18 11:31
 */
@UtilityClass
public class StringFormatter {

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可,如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例: <br>
     * 通常使用:        format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}:        format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\:         format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray   参数列表
     * @return 结果 string
     * @Author yanger
     */
    @SuppressWarnings("checkstyle:ReturnCount")
    public static String format(String strPattern, Object... argArray) {
        boolean isEmpty = argArray == null || argArray.length == 0;
        if (StringUtils.isEmpty(strPattern) || isEmpty) {
            return strPattern;
        }
        int strPatternLength = strPattern.length();
        // 初始化定义好的长度以获得更好的性能
        StringBuilder sbuf = new StringBuilder(strPatternLength + 50);
        // 记录已经处理到的位置
        int handledPosition = 0;
        // 占位符所在位置
        int delimIndex;
        for (int argIndex = 0, length = argArray.length; argIndex < length; argIndex++) {
            delimIndex = strPattern.indexOf(StringPool.EMPTY_JSON, handledPosition);
            // 剩余部分无占位符
            if (delimIndex == -1) {
                // 不带占位符的模板直接返回
                if (handledPosition == 0) {
                    return strPattern;
                } else {
                    sbuf.append(strPattern, handledPosition, strPatternLength);
                    return sbuf.toString();
                }
            } else {
                // 转义符
                if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == CharPool.BACK_SLASH) {
                    // 双转义符
                    if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == CharPool.BACK_SLASH) {
                        // 转义符之前还有一个转义符,占位符依旧有效
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(toStr(argArray[argIndex]));
                        handledPosition = delimIndex + 2;
                    } else {
                        //占位符被转义
                        argIndex--;
                        sbuf.append(strPattern, handledPosition, delimIndex - 1);
                        sbuf.append(StringPool.LEFT_BRACE);
                        handledPosition = delimIndex + 1;
                    }
                } else {
                    // 正常占位符
                    sbuf.append(strPattern, handledPosition, delimIndex);
                    sbuf.append(toStr(argArray[argIndex]));
                    handledPosition = delimIndex + 2;
                }
            }
        }
        // 加入最后一个占位符后所有的字符
        sbuf.append(strPattern, handledPosition, strPattern.length());
        return sbuf.toString();
    }

    /**
     * To str string
     *
     * @param str str
     * @return the string
     * @Author yanger
     */
    @Contract(pure = true)
    public static String toStr(Object str) {
        return toStr(str, "");
    }

    /**
     * 强转string,并去掉多余空格
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return String string
     * @Author yanger
     */
    @Contract(value = "null, _ -> param2", pure = true)
    public static String toStr(Object str, String defaultValue) {
        if (null == str) {
            return defaultValue;
        }
        return String.valueOf(str);
    }

    /**
     * 同时兼容 {} 和 {0} 2 种格式
     *
     * @param value  value
     * @param params params
     * @return the string
     * @Author yanger
     */
    @NotNull
    public static String mergeFormat(@NotNull String value, @NotNull Object... params) {
        return params.length > 0 && value.indexOf('{') >= 0
               ? value.contains("{0")
                 ? MessageFormat.format(value, params)
                 : format(value, params)
               : value;
    }

}
