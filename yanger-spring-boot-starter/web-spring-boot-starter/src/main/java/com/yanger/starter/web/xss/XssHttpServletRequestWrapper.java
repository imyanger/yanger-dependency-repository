package com.yanger.starter.web.xss;

import com.google.common.collect.Sets;

import com.yanger.starter.web.support.CacheRequestEnhanceWrapper;
import com.yanger.starter.web.util.WebUtils;
import com.yanger.tools.general.constant.Charsets;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletInputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description xss 过滤处理, 在 cache request 的基础上对 xss 进行处理
 * @Author yanger
 * @Date 2021/1/27 16:21
 */
@Slf4j
public class XssHttpServletRequestWrapper extends CacheRequestEnhanceWrapper {

    /** html过滤 */
    private static final HtmlFilter HTML_FILTER = new HtmlFilter();

    /** SQL_KEY */
    private static final String SQL_KEY = "and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+";

    /** NOT_ALLOWED_KEY_WORDS */
    private static final Set<String> NOT_ALLOWED_KEY_WORDS = Sets.newHashSet();

    /** REPLACED_STRING */
    private static final String REPLACED_STRING = "INVALID";

    static {
        String[] keyStr = SQL_KEY.split("\\|");
        NOT_ALLOWED_KEY_WORDS.addAll(Arrays.asList(keyStr));
    }

    /**
     * Instantiates a new Xss http servlet request wrapper.
     *
     * @param request the request
     */
    XssHttpServletRequestWrapper(ContentCachingRequestWrapper request) {
        super(request);
    }

    /**
     * Gets input stream *
     *
     * @return the input stream
     */
    @Override
    @SuppressWarnings("checkstyle:ReturnCount")
    public ServletInputStream getInputStream() {

        // 为空,直接返回
        if (null == super.getHeader(HttpHeaders.CONTENT_TYPE)) {
            return super.getInputStream();
        }

        // 非 json 类型,直接返回
        if (!"json".equals(MediaType.valueOf(super.getHeader(HttpHeaders.CONTENT_TYPE)).getSubtype())) {
            return super.getInputStream();
        }

        // 为空,直接返回
        String requestStr = WebUtils.getRequestStr(this.cachingRequestWrapper, this.body);
        if (StringUtils.isBlank(requestStr)) {
            return super.getInputStream();
        }

        requestStr = xssEncode(requestStr);

        return WebUtils.getCacheInputStream(requestStr.getBytes(Charsets.UTF_8));
    }

    /**
     * Gets header *
     *
     * @param name name
     * @return the header
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * Gets parameter *
     *
     * @param name name
     * @return the parameter
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * Get parameter values string [ ]
     *
     * @param name name
     * @return the string [ ]
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    /**
     * Gets parameter map *
     *
     * @return the parameter map
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    /**
     * Xss encode string
     *
     * @param input input
     * @return the string
     */
    private static String xssEncode(String input) {
        return HTML_FILTER.filter(cleanSqlKeyWords(input));
    }

    /**
     * sql 注入过滤
     *
     * @param value value
     * @return the string
     */
    private static String cleanSqlKeyWords(String value) {
        String paramValue = value;

        for (String keyword : NOT_ALLOWED_KEY_WORDS) {
            boolean lengthMatched = paramValue.length() > keyword.length() + 4;
            boolean containsMatched = paramValue.contains(" " + keyword)
                                      || paramValue.contains(keyword + " ")
                                      || paramValue.contains(" " + keyword + " ");

            if (lengthMatched && containsMatched) {
                paramValue = StringUtils.replace(paramValue, keyword, REPLACED_STRING);
            }
        }
        return paramValue;
    }

}
