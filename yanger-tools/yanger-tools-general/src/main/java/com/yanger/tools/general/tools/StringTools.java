package com.yanger.tools.general.tools;

import com.yanger.tools.general.constant.CharPool;
import com.yanger.tools.general.constant.StringPool;

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

import cn.hutool.core.text.StrSpliter;

/**
 * @Description string tool
 * @Author yanger
 * @Date 2020/12/21 10:27
 */
public class StringTools extends StringUtils {

    /** 特殊字符正则,sql特殊字符和空白符 */
    public static final Pattern SPECIAL_CHARS_REGEX = Pattern.compile("[`'\"|/,;()-+*%#·•�　\\s]");

    /** BLANK_SYMBOL */
    public static final String BLANK_SYMBOL = "\\s*|\t|\r|\n";

    /**
     * 首字母大写
     *
     * @param str
     * @return java.lang.String
     * @date 2020/7/17
     */
    public static String upperCaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return java.lang.String
     * @date 2020/7/17
     */
    public static String lowerCaseFirst(String str) {
        return (str != null && str.length() > 1) ? str.substring(0, 1).toLowerCase() + str.substring(1) : "";
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
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
     *
     * @param str 字符串
     * @return {String}
     */
    @org.jetbrains.annotations.NotNull
    public static String firstCharToLower(@org.jetbrains.annotations.NotNull String str) {
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
     *
     * @param underscoreName
     * @return java.lang.String
     * @date 2020/7/17
     */
    public static String underlineToCamelCase(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.trim().length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param para
     * @return java.lang.String
     * @date 2020/7/17
     */
    public static String camelCaseToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 1; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 横线转驼峰
     *
     * @param para 字符串
     * @return String string
     */
    @org.jetbrains.annotations.NotNull
    public static String lineToHump(@org.jetbrains.annotations.NotNull String para) {
        return toHump(para, "-");
    }

    /**
     * 驼峰转横线
     *
     * @param para 字符串
     * @return String string
     */
    @org.jetbrains.annotations.NotNull
    public static String humpToLine(String para) {
        return fromHump(para, "-");
    }

    /**
     * To hump string
     *
     * @param para para
     * @param s2   s 2
     * @return the string
     */
    @org.jetbrains.annotations.NotNull
    private static String toHump(@org.jetbrains.annotations.NotNull String para, String s2) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split(s2);
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
     * From hump string
     *
     * @param para para
     * @param s    s
     * @return the string
     */
    @org.jetbrains.annotations.NotNull
    private static String fromHump(String para, String s) {
        para = firstCharToLower(para);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, s);
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 去掉指定后缀,并小写首字母
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串 ,若后缀不是 suffix, 返回原字符串
     */
    @org.jetbrains.annotations.NotNull
    public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
        return firstCharToLower(removeSuffix(str, suffix));
    }

    /**
     * 生成uuid
     *
     * @return UUID string
     */
    @NotNull
    public static String randomUid() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replace(StringPool.DASH, StringPool.EMPTY);
    }

    /**
     * 清理字符串,清理出某些不可见字符
     *
     * @param txt 字符串
     * @return {String}
     */
    @NotNull
    @Contract(pure = true)
    public static String cleanChars(@NotNull String txt) {
        return txt.replaceAll("[ 　`·•�\\f\\t\\v\\s]", "");
    }

    /**
     * 切分字符串,不去除切分后每个元素两边的空白符,不去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数,-1不限制
     * @return 切分后的集合 list
     */
    @Contract("null, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, false, false);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数,-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     */
    @Contract("null, _, _, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList<>(0);
        }
        return StrSpliter.split(str.toString(), separator, limit, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串,去除切分后每个元素两边的空白符,去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合 list
     */
    @Contract("null, _ -> new")
    public static List<String> splitTrim(CharSequence str, char separator) {
        return splitTrim(str, separator, -1);
    }

    /**
     * 切分字符串,去除切分后每个元素两边的空白符,去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数,-1不限制
     * @return 切分后的集合 list
     */
    @Contract("null, _, _ -> new")
    private static List<String> splitTrim(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    /**
     * 切分字符串,去除切分后每个元素两边的空白符,去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合 list
     */
    @Contract("null, _ -> new")
    public static List<String> splitTrim(CharSequence str, CharSequence separator) {
        return splitTrim(str, separator, -1);
    }

    /**
     * 切分字符串,去除切分后每个元素两边的空白符,去除空白项
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数,-1不限制
     * @return 切分后的集合 list
     */
    @Contract("null, _, _ -> new")
    private static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    /**
     * 切分字符串
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param limit       限制分片数,-1不限制
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
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
     * 切分字符串,不限制分片数量
     *
     * @param str         被切分的字符串
     * @param separator   分隔符字符
     * @param isTrim      是否去除切分字符串后每个元素两边的空格
     * @param ignoreEmpty 是否忽略空串
     * @return 切分后的集合 list
     */
    @Contract("null, _, _, _ -> new")
    public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, 0, isTrim, ignoreEmpty);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @return 字符串 string [ ]
     */
    @org.jetbrains.annotations.NotNull
    @Contract("null, _ -> new")
    public static String[] split(CharSequence str, CharSequence separator) {
        if (str == null) {
            return new String[0];
        }

        String separatorStr = (null == separator) ? null : separator.toString();
        return StrSpliter.splitToArray(str.toString(), separatorStr, 0, false, false);
    }

    /**
     * 根据给定长度,将给定字符串截取为多个部分
     *
     * @param str 字符串
     * @param len 每一个小节的长度
     * @return 截取后的字符串数组 string [ ]
     * @see StrSpliter#splitByLength(String, int) StrSpliter#splitByLength(String, int)StrSpliter#splitByLength(String, int)
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
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串 ,若前缀不是 preffix, 返回原字符串
     */
    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(prefix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.startsWith(prefix.toString())) {
            return subSuf(str2, prefix.length());
        }
        return str2;
    }

    /**
     * 切割指定位置之后部分的字符串
     *
     * @param string    字符串
     * @param fromIndex 切割开始的位置 (包括)
     * @return 切割后后剩余的后半部分字符串 string
     */
    @Nullable
    public static String subSuf(CharSequence string, int fromIndex) {
        if (StringUtils.isEmpty(string)) {
            return StringPool.EMPTY;
        }
        return sub(string, fromIndex, string.length());
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算,最后一个字符为-1<br>
     * 如果from和to位置一样,返回 "" <br>
     * 如果from或to为负数,则按照length从后向前数位置,如果绝对值大于字符串长度,则from归到0,to归到length<br>
     * 如果经过修正的index中from大于to,则互换from和to example: <br>
     * abcdefgh 2 3 =》 c <br>
     * abcdefgh 2 -3 =》 cde <br>
     *
     * @param str       String
     * @param fromIndex 开始的index (包括)
     * @param toIndex   结束的index (不包括)
     * @return 字串 string
     */
    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (StringUtils.isEmpty(str)) {
            return StringPool.EMPTY;
        }
        int len = str.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return StringPool.EMPTY;
        }

        return str.toString().substring(fromIndex, toIndex);
    }

    /**
     * 忽略大小写去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串 ,若前缀不是 prefix, 返回原字符串
     */
    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(prefix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            return subSuf(str2, prefix.length());
        }
        return str2;
    }

    /**
     * 去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串 ,若后缀不是 suffix, 返回原字符串
     */
    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(suffix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.endsWith(suffix.toString())) {
            return subPre(str2, str2.length() - suffix.length());
        }
        return str2;
    }

    /**
     * 切割指定位置之前部分的字符串
     *
     * @param string  字符串
     * @param toIndex 切割到的位置 (不包括)
     * @return 切割后的剩余的前半部分字符串 string
     */
    private static String subPre(CharSequence string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    /**
     * 忽略大小写去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串 ,若后缀不是 suffix, 返回原字符串
     */
    public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(suffix)) {
            return StringPool.EMPTY;
        }

        String str2 = str.toString();
        if (str2.toLowerCase().endsWith(suffix.toString().toLowerCase())) {
            return subPre(str2, str2.length() - suffix.length());
        }
        return str2;
    }

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象 string builder
     */
    @org.jetbrains.annotations.NotNull
    @Contract(value = " -> new", pure = true)
    public static StringBuilder builder() {
        return new StringBuilder();
    }

    /**
     * 创建StringBuilder对象
     *
     * @param capacity 初始大小
     * @return StringBuilder对象 string builder
     */
    @org.jetbrains.annotations.NotNull
    @Contract(value = "_ -> new", pure = true)
    public static StringBuilder builder(int capacity) {
        return new StringBuilder(capacity);
    }

    /**
     * 创建StringBuilder对象
     *
     * @param strs 初始字符串列表
     * @return StringBuilder对象 string builder
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
     *
     * @param sb   初始StringBuilder
     * @param strs 初始字符串列表
     * @return StringBuilder对象 string builder
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
     *
     * @param content       内容
     * @param charForSearch 被统计的字符
     * @return 包含数量 int
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
     *
     * @param str    被重复的字符
     * @param padLen 指定长度
     * @return 重复字符字符串 string
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
            return subPre(str, padLen);
        }

        // 重复,直到达到指定长度
        char[] padding = new char[padLen];
        for (int i = 0; i < padLen; i++) {
            padding[i] = str.charAt(i % strLen);
        }
        return new String(padding);
    }


    /**
     * 清理字符串,清理出某些不可见字符和一些sql特殊字符
     *
     * @param txt 文本
     * @return {String}
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
     * 获取标识符,用于参数清理
     *
     * @param param 参数
     * @return 清理后的标识符 string
     */
    @Contract("null -> null")
    @Nullable
    public static String cleanIdentifier(@Nullable String param) {
        if (param == null) {
            return null;
        }
        StringBuilder paramBuilder = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                paramBuilder.append(c);
            }
        }
        return paramBuilder.toString();
    }

    /**
     * 删除空白符
     *
     * @param str the str
     * @return the string
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
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串 (不包括)
     * @param isLastSeparator 是否查找最后一个分隔字符串 (多次出现分隔字符串时选取最后一个) ,true为选取最后一个
     * @return 切割后的字符串 string
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
     * 默认查找最后一个分隔符
     *
     * @param string    string
     * @param separator separator
     * @return the string
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
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串 (不包括)
     * @param isLastSeparator 是否查找最后一个分隔字符串 (多次出现分隔字符串时选取最后一个) ,true为选取最后一个
     * @return 切割后的字符串 string
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
     * 默认查找第一个分隔符
     *
     * @param string    string
     * @param separator separator
     * @return the string
     */
    public static String subAfter(CharSequence string, CharSequence separator) {
        return subAfter(string, separator, false);
    }

}
