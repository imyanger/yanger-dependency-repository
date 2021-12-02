package com.yanger.tools.general.constant;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 字符集工具类
 * @Author yanger
 * @Date 2020/12/11 18:19
 */
public class Charsets {

    /** 字符集 ISO-8859-1 */
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

    /** 字符集 ISO-8859-1 name */
    public static final String ISO_8859_1_NAME = ISO_8859_1.name();

    /** 字符集 GBK */
    public static final Charset GBK = of("GBK");

    /** 字符集 GBK name */
    public static final String GBK_NAME = GBK.name();

    /** 字符集 utf-8 */
    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    /** 字符集 utf-8 name */
    public static final String UTF_8_NAME = UTF_8.name();

    /**
     * 转换为Charset对象
     * @param charsetName 字符集,为空则返回默认字符集
     * @return {@link Charset}
     * @throws UnsupportedCharsetException 编码不支持
     */
    public static Charset of(String charsetName) throws UnsupportedCharsetException {
        return StringUtils.isNotBlank(charsetName) ? Charset.forName(charsetName) : Charset.defaultCharset();
    }

}
