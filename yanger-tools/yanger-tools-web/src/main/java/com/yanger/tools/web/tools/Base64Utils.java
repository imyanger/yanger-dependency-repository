package com.yanger.tools.web.tools;

import com.yanger.tools.general.constant.Charsets;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Base64工具
 * @Author yanger
 * @Date 2020/12/18 18:37
 */
public class Base64Utils extends org.springframework.util.Base64Utils {

    /**
     * 编码
     * @param value 字符串
     * @return {String}
     */
    @NotNull
    public static String encode(String value) {
        return Base64Utils.encode(value, Charsets.UTF_8);
    }

    /**
     * 编码
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    @NotNull
    @Contract("_, _ -> new")
    public static String encode(@NotNull String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(Base64Utils.encode(val), charset);
    }

    /**
     * 编码URL安全
     * @param value 字符串
     * @return {String}
     */
    @NotNull
    public static String encodeUrlSafe(String value) {
        return Base64Utils.encodeUrlSafe(value, Charsets.UTF_8);
    }

    /**
     * 编码URL安全
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    @NotNull
    @Contract("_, _ -> new")
    public static String encodeUrlSafe(@NotNull String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        return new String(Base64Utils.encodeUrlSafe(val), charset);
    }

    /**
     * 解码
     * @param value 字符串
     * @return {String}
     */
    @NotNull
    public static String decode(String value) {
        return Base64Utils.decode(value, Charsets.UTF_8);
    }

    /**
     * 解码
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    @NotNull
    public static String decode(@NotNull String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        byte[] decodedValue = Base64Utils.decode(val);
        return new String(decodedValue, charset);
    }

    /**
     * 解码URL安全
     * @param value 字符串
     * @return {String}
     */
    @NotNull
    public static String decodeUrlSafe(String value) {
        return Base64Utils.decodeUrlSafe(value, Charsets.UTF_8);
    }

    /**
     * 解码URL安全
     * @param value   字符串
     * @param charset 字符集
     * @return {String}
     */
    @NotNull
    public static String decodeUrlSafe(@NotNull String value, java.nio.charset.Charset charset) {
        byte[] val = value.getBytes(charset);
        byte[] decodedValue = Base64Utils.decodeUrlSafe(val);
        return new String(decodedValue, charset);
    }

}
