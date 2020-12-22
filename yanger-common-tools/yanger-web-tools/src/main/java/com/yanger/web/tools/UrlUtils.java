package com.yanger.web.tools;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import com.yanger.general.constant.Charsets;
import com.yanger.web.exception.BasicException;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Map;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description url处理工具类
 * @Author yanger
 * @Date 2020/12/22 14:06
 */
@Slf4j
@UtilityClass
public class UrlUtils extends org.springframework.web.util.UriUtils {

    /**
     * url 编码,同js decodeURIComponent
     *
     * @param source  url
     * @param charset 字符集
     * @return 编码后的url string
     */
    @NotNull
    public static String encodeUrl(String source, @NotNull Charset charset) {
        return UrlUtils.encode(source, charset.name());
    }

    /**
     * url 解码
     *
     * @param source  url
     * @param charset 字符集
     * @return 解码url string
     */
    @NotNull
    public static String decodeUrl(String source, @NotNull Charset charset) {
        return UrlUtils.decode(source, charset.name());
    }

    /**
     * 获取 url 路径
     *
     * @param uriStr 路径
     * @return url路径 path
     */
    public static String getPath(String uriStr) {
        URI uri;

        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException var3) {
            throw new BasicException(var3);
        }

        return uri.getPath();
    }

    /**
     * map 转为 url 参数, 默认需要参数编码
     *
     * @param source source
     * @return the string
     */
    @NotNull
    public static String asUrlParams(@NotNull Map<String, String> source) {
        return asUrlParams(source, true);
    }

    /**
     * map 转为 url 参数
     *
     * @param source     source
     * @param urlEncoder url encoder
     * @return the string
     */
    @NotNull
    public static String asUrlParams(@NotNull Map<String, String> source, boolean urlEncoder) {
        Map<String, String> tmp = Maps.newHashMap();
        source.forEach((k, v) -> {
            if (k != null) {
                try {
                    if (urlEncoder) {
                        tmp.put(k, URLEncoder.encode(v, Charsets.UTF_8_NAME));
                    } else {
                        tmp.put(k, v);
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error("url encode error", e);
                }
            }
        });
        return Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(tmp);
    }

}
