package com.yanger.web.tools;

import com.yanger.general.constant.Charsets;
import com.yanger.general.exception.Exceptions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

import java.io.*;

import lombok.experimental.UtilityClass;

/**
 * @Description IO工具类
 * @Author yanger
 * @Date 2020/12/21 10:17
 */
@UtilityClass
public class IoUtils extends org.springframework.util.StreamUtils {

    /**
     * byte[] 转 String, 编码默认 UTF-8
     *
     * @param input input
     * @return the string
     */
    @NotNull
    public static String toString(byte[] input) {
        return toString(input, Charsets.UTF_8_NAME);
    }

    /**
     * 使用指定的字符编码以字符串形式获取 byte[] 的内容
     *
     * @param input    the byte array to read from
     * @param encoding the encoding to use, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     */
    @NotNull
    @Contract("_, _ -> new")
    public static String toString(byte[] input, String encoding) {
        return new String(input, Charsets.of(encoding));
    }

    /**
     * InputStream to String utf-8
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested String
     */
    @NotNull
    public static String toString(InputStream input) {
        return toString(input, Charsets.UTF_8);
    }

    /**
     * InputStream to String
     *
     * @param input   the <code>InputStream</code> to read from
     * @param charset the <code>Charsets</code>
     * @return the requested String
     */
    @NotNull
    public static String toString(@Nullable InputStream input, java.nio.charset.Charset charset) {
        try {
            return IoUtils.copyToString(input, charset);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        } finally {
            IoUtils.closeQuietly(input);
        }
    }

    /**
     * closeQuietly
     *
     * @param closeable 自动关闭
     */
    public static void closeQuietly(@Nullable Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * To byte array byte [ ].
     *
     * @param input the input
     * @return the byte [ ]
     */
    @NotNull
    public static byte[] toByteArray(@Nullable InputStream input) {
        try {
            return IoUtils.copyToByteArray(input);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        } finally {
            IoUtils.closeQuietly(input);
        }
    }

    /**
     * Writes chars from a <code>String</code> to bytes on an
     * <code>OutputStream</code> using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     *
     * @param data     the <code>String</code> to write, null ignored
     * @param output   the <code>OutputStream</code> to write to
     * @param encoding the encoding to use, null means platform default
     * @throws IOException if an I/O error occurs
     */
    public static void write(@Nullable String data, OutputStream output, java.nio.charset.Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(encoding));
        }
    }

}
