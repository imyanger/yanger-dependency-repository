package com.yanger.tools.general.tools;

import cn.hutool.core.text.StrSpliter;
import com.yanger.tools.general.constant.CharPool;
import com.yanger.tools.general.constant.StringPool;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * string tool
 * @Author yanger
 * @Date 2020/12/21 10:27
 */
public class StringTools extends StringUtils {

    /** 特殊字符正则,sql特殊字符和空白符 */
    public static final Pattern SPECIAL_CHARS_REGEX = Pattern.compile("[`'\"|/,;()-+*%#·•�　\\s]");

    /** BLANK_SYMBOL */
    public static final String BLANK_SYMBOL = "\\s*|\t|\r|\n";

    /**
     * 首字母变大写
     * @param str
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    public static String firstCharToUpper(String str) {
        if (isBlank(str)) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (firstChar >= CharPool.LOWER_A && firstChar <= CharPool.LOWER_Z) {
            char[] arr = str.toCharArray();
            arr[0] -= (CharPool.LOWER_A - CharPool.UPPER_A);
            return new String(arr);
        }
        return str;
    }

    /**
     * 首字母变小写
     * @param str
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    public static String firstCharToLower(String str) {
        if (isBlank(str)) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (firstChar >= CharPool.UPPER_A && firstChar <= CharPool.UPPER_Z) {
            char[] arr = str.toCharArray();
            arr[0] += (CharPool.LOWER_A - CharPool.UPPER_A);
            return new String(arr);
        }
        return str;
    }

    /**
     * 下划线转驼峰
     * @param str
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    public static String underlineToCamelCase(String str) {
        return toHump(str, "_");
    }

    /**
     * 驼峰转下划线
     * @param str
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    public static String camelCaseToUnderline(String str) {
        return fromHump(str, "_");
    }

    /**
     * 横线转驼峰
     * @param str
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @org.jetbrains.annotations.NotNull
    public static String lineToHump(@org.jetbrains.annotations.NotNull String str) {
        return toHump(str, "-");
    }

    /**
     * 驼峰转横线
     * @param str
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @org.jetbrains.annotations.NotNull
    public static String humpToLine(String str) {
        return fromHump(str, "-");
    }

    /**
     * 替换 sign 为驼峰
     * @param str
     * @param sign
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @org.jetbrains.annotations.NotNull
    private static String toHump(@org.jetbrains.annotations.NotNull String str, String sign) {
        StringBuilder result = new StringBuilder();
        String[] a = str.split(sign);
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 替换驼峰为 sign
     * @param str
     * @param sign
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @org.jetbrains.annotations.NotNull
    private static String fromHump(String str, String sign) {
        str = firstCharToLower(str);
        StringBuilder sb = new StringBuilder(str);
        int temp = 0;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                sb.insert(i + temp, sign);
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 生成uuid
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @NotNull
    public static String randomUid() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replace(StringPool.DASH, StringPool.EMPTY);
    }

    /**
     * 清理字符串,清理出某些不可见字符
     * @param txt
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @NotNull
    @Contract(pure = true)
    public static String cleanChars(@NotNull String txt) {
        return txt.replaceAll("[ 　`·•�\\f\\t\\v\\s]", "");
    }

    /**
     * 清理字符串,清理出某些不可见字符和一些sql特殊字符
     * @param txt 文本
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    @Contract("null -> null")
    @Nullable
    public static String cleanText(@Nullable String txt) {
        if (txt == null) {
            return null;
        }
        return SPECIAL_CHARS_REGEX.matcher(txt).replaceAll(StringPool.EMPTY);
    }

    /**
     * 切分字符串,不去除切分后每个元素两边的空白符,不去除空白项
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @Contract("null, _ -> new")
    public static List<String> split(CharSequence str, char separator) {
        return split(str, separator, -1);
    }

    /**
     * 切分字符串,不去除切分后每个元素两边的空白符,不去除空白项
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @Contract("null, _ -> new")
    public static List<String> split(CharSequence str, CharSequence separator) {
        return split(str, separator, -1);
    }

    /**
     * 切分字符串,不去除切分后每个元素两边的空白符,不去除空白项
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param limit 限制分片数, -1不限制
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @Contract("null, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, false, false);
    }

    /**
     * 切分字符串,不去除切分后每个元素两边的空白符,不去除空白项
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param limit 限制分片数, -1不限制
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @Contract("null, _, _ -> new")
    private static List<String> split(CharSequence str, CharSequence separator, int limit) {
        return split(str, separator, limit, false, false);
    }

    /**
     * 切分字符串, 去除切分后每个元素两边的空白符, 去除空白项
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @Contract("null, _ -> new")
    public static List<String> splitTrim(CharSequence str, char separator) {
        return splitTrim(str, separator, -1);
    }

    /**
     * 切分字符串, 去除切分后每个元素两边的空白符, 去除空白项
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @Contract("null, _ -> new")
    public static List<String> splitTrim(CharSequence str, CharSequence separator) {
        return splitTrim(str, separator, -1);
    }

    /**
     * 切分字符串, 去除切分后每个元素两边的空白符, 去除空白项
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param limit 限制分片数, -1不限制
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @Contract("null, _, _ -> new")
    private static List<String> splitTrim(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    /**
     * 切分字符串, 去除切分后每个元素两边的空白符, 去除空白项
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param limit 限制分片数, -1不限制
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @Contract("null, _, _ -> new")
    private static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    /**
     * 切分字符串
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param limit 限制分片数, -1不限制
     * @param isTrim 是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:55
     */
    @Contract("null, _, _, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList<>(0);
        }
        return StrSpliter.split(str.toString(), separator, limit, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param limit 限制分片数, -1不限制
     * @param isTrim 是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @Contract("null, _, _, _, _ -> new")
    public static List<String> split(CharSequence str, CharSequence separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList<>(0);
        }
        String separatorStr = (null == separator) ? null : separator.toString();
        return StrSpliter.split(str.toString(), separatorStr, limit, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串, 不限制分片数量
     * @param str 被切分的字符串
     * @param separator 分隔符字符
     * @param isTrim 是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return {@link List< String>} 切分后的集合 list
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @Contract("null, _, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, 0, isTrim, ignoreEmpty);
    }

    /**
     * 根据给定长度, 将给定字符串截取为多个部分
     * @param str 字符串
     * @param len 每一个小节的长度
     * @return {@link String[]} 截取后的字符串数组
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @Contract("null, _ -> new")
    public static String[] split(CharSequence str, int len) {
        if (null == str) {
            return new String[0];
        }
        return StrSpliter.splitByLength(str.toString(), len);
    }

    /**
     * 去掉指定前缀
     * @param str 字符串
     * @param prefix 前缀
     * @return {@link String} 切掉后的字符串 ,若前缀不是 preffix, 返回原字符串
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(prefix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.startsWith(prefix.toString())) {
            return str2.substring(prefix.length());
        }
        return str2;
    }

    /**
     * 忽略大小写去掉指定前缀
     * @param str 字符串
     * @param prefix 前缀
     * @return {@link String} 切掉后的字符串 ,若前缀不是 prefix, 返回原字符串
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(prefix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            return str2.substring(prefix.length());
        }
        return str2;
    }

    /**
     * 去掉指定后缀
     * @param str 字符串
     * @param suffix 后缀
     * @return {@link String} 切掉后的字符串 ,若后缀不是 suffix, 返回原字符串
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(suffix)) {
            return StringPool.EMPTY;
        }
        String str2 = str.toString();
        if (str2.endsWith(suffix.toString())) {
            return str2.substring(0, str2.length() - suffix.length());
        }
        return str2;
    }

    /**
     * 忽略大小写去掉指定后缀
     * @param str 字符串
     * @param suffix 后缀
     * @return {@link String} 切掉后的字符串 ,若后缀不是 suffix, 返回原字符串
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(suffix)) {
            return StringPool.EMPTY;
        }
        String str2 = str.toString();
        if (str2.toLowerCase().endsWith(suffix.toString().toLowerCase())) {
            return str2.substring(0, str2.length() - suffix.length());
        }
        return str2;
    }

    /**
     * 创建StringBuilder对象
     * @param
     * @return {@link StringBuilder}
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @org.jetbrains.annotations.NotNull
    @Contract(value = " -> new", pure = true)
    public static StringBuilder builder() {
        return new StringBuilder();
    }

    /**
     * 创建StringBuilder对象
     * @param capacity 初始大小
     * @return {@link StringBuilder}
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @org.jetbrains.annotations.NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StringBuilder builder(int capacity) {
        return new StringBuilder(capacity);
    }

    /**
     * 创建StringBuilder对象
     * @param strs 初始字符串列表
     * @return {@link StringBuilder}
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    public static StringBuilder builder(@org.jetbrains.annotations.NotNull CharSequence... strs) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 创建StringBuilder对象
     * @param sb 初始StringBuilder
     * @param strs 初始字符串列表
     * @return {@link StringBuilder}
     * @Author yanger
     * @Date 2022/01/21 21:56
     */
    @Contract("_, _ -> param1")
    public static StringBuilder appendBuilder(StringBuilder sb, @org.jetbrains.annotations.NotNull CharSequence... strs) {
        for (CharSequence str : strs) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 统计指定内容中包含指定字符的数量
     * @param content 内容
     * @param charForSearch 被统计的字符
     * @return {@link int}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    public static int count(CharSequence content, char charForSearch) {
        int count = 0;
        if (StringUtils.isEmpty(content)) {
            return 0;
        }
        int contentLength = content.length();
        for (int i = 0; i < contentLength; i++) {
            if (charForSearch == content.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 重复某个字符串到指定长度
     * @param str 被重复的字符
     * @param padLen 指定长度
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    @Contract("null, _ -> null")
    private static String repeatByLength(CharSequence str, int padLen) {
        if (null == str) {
            return null;
        }
        if (padLen <= 0) {
            return StringPool.EMPTY;
        }
        int strLen = str.length();
        if (strLen == padLen) {
            return str.toString();
        } else if (strLen > padLen) {
            return str.toString().substring(0, padLen);
        }
        // 重复,直到达到指定长度
        char[] padding = new char[padLen];
        for (int i = 0; i < padLen; i++) {
            padding[i] = str.charAt(i % strLen);
        }
        return new String(padding);
    }

    /**
     * 删除空白符
     * @param str
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile(BLANK_SYMBOL);
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 截取分隔字符串之前的字符串,不包括分隔字符串<br>
     * 如果给定的字符串为空串 (null或"") 或者分隔字符串为null,返回原字符串<br>
     * 如果分隔字符串为空串"",则返回空串,如果分隔字符串未找到,返回原字符串
     * <p>
     * 栗子:
     * <pre>
     * StringUtil.subBefore(null, *)      = null
     * StringUtil.subBefore("", *)        = ""
     * StringUtil.subBefore("abc", "a")   = ""
     * StringUtil.subBefore("abcba", "b") = "a"
     * StringUtil.subBefore("abc", "c")   = "ab"
     * StringUtil.subBefore("abc", "d")   = "abc"
     * StringUtil.subBefore("abc", "")    = ""
     * StringUtil.subBefore("abc", null)  = "abc"
     * </pre>
     * @param string 被查找的字符串
     * @param separator 分隔字符串 (不包括)
     * @param isLastSeparator 是否查找最后一个分隔字符串 (多次出现分隔字符串时选取最后一个) ,true为选取最后一个
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    @Nullable
    public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (StringUtils.isEmpty(string) || separator == null) {
            return null == string ? null : string.toString();
        }

        String str = string.toString();
        String sep = separator.toString();
        if (sep.isEmpty()) {
            return StringPool.EMPTY;
        }
        int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 截取分隔字符串之前的字符串,不包括分隔字符串<br>
     *  默认查找最后一个分隔符
     * @param string
     * @param separator
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    public static String subBefore(CharSequence string, CharSequence separator) {
        return subBefore(string, separator, true);
    }

    /**
     * 截取分隔字符串之后的字符串,不包括分隔字符串<br>
     * 如果给定的字符串为空串 (null或"") ,返回原字符串<br>
     * 如果分隔字符串为空串 (null或"") ,则返回空串,如果分隔字符串未找到,返回空串
     * <p>
     * 栗子:
     * <pre>
     * StringUtil.subAfter(null, *)      = null
     * StringUtil.subAfter("", *)        = ""
     * StringUtil.subAfter(*, null)      = ""
     * StringUtil.subAfter("abc", "a")   = "bc"
     * StringUtil.subAfter("abcba", "b") = "cba"
     * StringUtil.subAfter("abc", "c")   = ""
     * StringUtil.subAfter("abc", "d")   = ""
     * StringUtil.subAfter("abc", "")    = "abc"
     * </pre>
     * @param string 被查找的字符串
     * @param separator 分隔字符串 (不包括)
     * @param isLastSeparator 是否查找最后一个分隔字符串 (多次出现分隔字符串时选取最后一个) ,true为选取最后一个
     * @return {@link String} 切割后的字符串
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    @Nullable
    public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (StringUtils.isEmpty(string)) {
            return null == string ? null : string.toString();
        }
        if (separator == null) {
            return StringPool.EMPTY;
        }
        String str = string.toString();
        String sep = separator.toString();
        int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (pos == INDEX_NOT_FOUND) {
            return StringPool.EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 截取分隔字符串之后的字符串,不包括分隔字符串<br>
     * 默认查找第一个分隔符
     * @param string
     * @param separator
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    public static String subAfter(CharSequence string, CharSequence separator) {
        return subAfter(string, separator, false);
    }

    /**
     * 对象组中是否存在 Empty Object
     * @param os
     * @return {@link boolean}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    public static boolean hasEmpty(@NotNull Object... os) {
        for (Object o : os) {
            if (ObjectUtils.isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换为 String 数组
     * @param str
     * @param split
     * @return {@link String[]}
     * @Author yanger
     * @Date 2022/01/21 21:57
     */
    public static String[] toStrArray(String str, String split) {
        if (StringUtils.isBlank(str)) {
            return new String[0];
        }
        return str.split(split);
    }

}
