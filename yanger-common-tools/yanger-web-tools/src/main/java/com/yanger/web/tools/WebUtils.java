package com.yanger.web.tools;

import com.google.common.collect.Maps;

import com.google.common.collect.Maps;

import com.fkhwl.starter.basic.util.CharPool;
import com.fkhwl.starter.basic.util.Charsets;
import com.fkhwl.starter.basic.util.IoUtils;
import com.fkhwl.starter.basic.util.JsonUtils;
import com.fkhwl.starter.basic.util.StringPool;
import com.sun.istack.internal.NotNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 用于处理HTTP请求的工具类
 * @Author yanger
 * @Date 2020/11/25 18:35
 */
@Slf4j
@UtilityClass
@SuppressWarnings("checkstyle:MethodLimit")
public class WebUtils extends org.springframework.web.util.WebUtils {

    /**
     * The constant USER_AGENT_HEADER.
     */
    public static final String USER_AGENT_HEADER = "user-agent";

    /**
     * The constant UN_KNOWN.
     */
    public static final String UN_KNOWN = "unknown";

    /**
     * 判断是否ajax请求
     * spring ajax 返回含有 ResponseBody 或者 RestController注解
     *
     * @param handlerMethod HandlerMethod
     * @return 是否ajax请求 boolean
     * @since 1.0.0
     */
    public static boolean isBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = ClassUtils.getAnnotation(handlerMethod, ResponseBody.class);
        return responseBody != null;
    }

    /**
     * 读取cookie
     *
     * @param name cookie name
     * @return cookie value
     * @since 1.0.0
     */
    public static String getCookieVal(String name) {
        HttpServletRequest request = WebUtils.getRequest();
        return getCookieVal(request, name);
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     * @since 1.0.0
     */
    @NotNull
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.notNull(requestAttributes, "当前线程中不存在 RequestAttributes");
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 读取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie name
     * @return cookie value
     * @since 1.0.0
     */
    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 获取COOKIE
     *
     * @param request the request
     * @param name    the name
     * @return the cookie
     * @since 1.0.0
     */
    public static @Nullable Cookie getCookie(@NotNull HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name, ck.getName())) {
                return ck;
            }
        }
        return null;
    }

    /**
     * 清除 某个指定的cookie
     *
     * @param response HttpServletResponse
     * @param key      cookie key
     * @since 1.0.0
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    /**
     * 设置cookie
     *
     * @param response        HttpServletResponse
     * @param name            cookie name
     * @param value           cookie value
     * @param maxAgeInSeconds maxage
     * @since 1.0.0
     */
    public static void setCookie(@NotNull HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(StringPool.SLASH);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 返回json
     *
     * @param response HttpServletResponse
     * @param result   结果对象
     * @since 1.0.0
     */
    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 返回json
     *
     * @param response    HttpServletResponse
     * @param result      结果对象
     * @param contentType contentType
     * @since 1.0.0
     */
    public static void renderJson(@NotNull HttpServletResponse response, Object result, String contentType) {
        response.setCharacterEncoding(StringPool.UTF_8);
        response.setContentType(contentType);
        try (PrintWriter out = response.getWriter()) {
            out.append(JsonUtils.toJson(result));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取ip
     *
     * @return {String}
     * @since 1.0.0
     */
    public static String getIp() {
        return getIp(WebUtils.getRequest());
    }

    /**
     * 获取ip
     *
     * @param request HttpServletRequest
     * @return {String}
     * @since 1.0.0
     */
    @Nullable
    public static String getIp(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest is null");
        String ip = request.getHeader("X-Requested-For");
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.isBlank(ip) ? null : ip.split(",")[0];
    }

    /**
     * 获取请求的 url: http[s]://www.xxx.xx[:port]
     *
     * @return the string
     * @since 1.5.0
     */
    public static @NotNull String getUrl() {
        return getUrl(WebUtils.getRequest());
    }

    /**
     * Gets url *
     *
     * @param request request
     * @return the url
     * @since 1.5.0
     */
    public static @NotNull String getUrl(@NotNull HttpServletRequest request) {
        String schemeAndHost = request.getScheme() + "://" + request.getServerName();
        // 默认端口输出
        int defaultPort = 80;
        if (request.getServerPort() != defaultPort) {
            schemeAndHost += ":" + request.getServerPort();
        }
        return schemeAndHost;
    }

    /***
     * 获取 request 中 json 字符串的内容
     *
     * @param request request
     * @return 字符串内容 request param string
     * @since 1.0.0
     */
    public static String getRequestParamString(HttpServletRequest request) {
        try {
            return getRequestStr(request);
        } catch (Exception ex) {
            return StringPool.EMPTY;
        }
    }

    /**
     * 获取 request 请求内容
     *
     * @param request request
     * @return String request str
     * @throws IOException IOException
     * @since 1.0.0
     */
    public static String getRequestStr(@NotNull HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            return new String(queryString.getBytes(Charsets.ISO_8859_1), Charsets.UTF_8)
                .replaceAll("&amp;", StringPool.AMPERSAND)
                .replaceAll("%22", "\"");
        }
        return getRequestStr(request, getRequestBytes(request));
    }

    /**
     * 获取 request 请求内容
     *
     * @param request request
     * @param buffer  buffer
     * @return String request str
     * @throws IOException IOException
     * @since 1.0.0
     */
    @SneakyThrows
    @NotNull
    public static String getRequestStr(@NotNull HttpServletRequest request, byte[] buffer) {
        String charEncoding = request.getCharacterEncoding();
        String str = new String(buffer, charEncoding).trim();
        if (StringUtils.isBlank(str)) {
            StringBuilder sb = new StringBuilder();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String key = parameterNames.nextElement();
                String value = request.getParameter(key);
                StringUtils.appendBuilder(sb, key, StringPool.EQUALS, value, StringPool.AMPERSAND);
            }
            str = StringUtils.removeSuffix(sb.toString(), StringPool.AMPERSAND);
        }
        return str.replaceAll("&amp;", StringPool.AMPERSAND);
    }

    /**
     * 获取 request 请求的 byte[] 数组
     *
     * @param request request
     * @return byte[] byte [ ]
     * @throws IOException IOException
     * @since 1.0.0
     */
    @Nullable
    @SneakyThrows
    public static byte[] getRequestBytes(@NotNull HttpServletRequest request) {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 获取客户端IP地址,此方法用在proxy环境中
     *
     * @param req the req
     * @return remote addr
     * @since 1.0.0
     */
    public static String getRemoteAddr(@NotNull HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip)) {
            String[] ips = StringUtils.split(ip, StringPool.COMMA);
            for (String tmpip : Objects.requireNonNull(ips)) {
                if (StringUtils.isBlank(tmpip)) {
                    continue;
                }
                tmpip = tmpip.trim();
                if (isIpAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.")
                    && !INetUtils.LOCAL_HOST.equals(tmpip)) {
                    return tmpip.trim();
                }
            }
        }
        ip = req.getHeader("x-real-ip");
        if (isIpAddr(ip)) {
            return ip;
        }
        ip = req.getRemoteAddr();
        if (ip.indexOf(CharPool.DOT) == -1) {
            ip = INetUtils.LOCAL_HOST;
        }
        return ip;
    }

    /**
     * 判断字符串是否是一个IP地址
     *
     * @param addr the addr
     * @return boolean boolean
     * @since 1.0.0
     */
    @SuppressWarnings(value = {"PMD.UndefineMagicConstantRule", "checkstyle:ReturnCount"})
    public static boolean isIpAddr(String addr) {
        if (StringUtils.isEmpty(addr)) {
            return false;
        }
        String[] ips = StringUtils.split(addr, StringPool.DOT);
        if (Objects.requireNonNull(ips).length != 4) {
            return false;
        }
        try {
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                   && ipc <= 255 && ipd >= 0 && ipd <= 255;
        } catch (Exception e) {
            log.error("transformation error", e);
        }
        return false;
    }

    /**
     * 判断是否为搜索引擎
     *
     * @param req the req
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean isRobot(@NotNull HttpServletRequest req) {
        String ua = req.getHeader("user-agent");
        if (StringUtils.isBlank(ua)) {
            return false;
        }
        return ua.contains("Baiduspider")
               || ua.contains("Googlebot")
               || ua.contains("sogou")
               || ua.contains("sina")
               || ua.contains("iaskspider")
               || ua.contains("ia_archiver")
               || ua.contains("Sosospider")
               || ua.contains("YoudaoBot")
               || ua.contains("yahoo")
               || ua.contains("yodao")
               || ua.contains("MSNBot")
               || ua.contains("spider")
               || ua.contains("Twiceler")
               || ua.contains("Sosoimagespider")
               || ua.contains("naver.com/robots")
               || ua.contains("Nutch")
               || ua.contains("spider");
    }

    /**
     * 获取COOKIE
     *
     * @param request the request
     * @param name    the name
     * @return the cookie value
     * @since 1.0.0
     */
    @Nullable
    public static String getCookieValue(@NotNull HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name, ck.getName())) {
                return ck.getValue();
            }
        }
        return null;
    }

    /**
     * 设置COOKIE
     *
     * @param request  the request
     * @param response the response
     * @param name     the name
     * @param value    the value
     * @param maxAge   the max age
     * @since 1.0.0
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name,
                                 String value, int maxAge) {
        setCookie(request, response, name, value, maxAge, true);
    }

    /**
     * 设置COOKIE
     *
     * @param request      the request
     * @param response     the response
     * @param name         the name
     * @param value        the value
     * @param maxAge       the max age
     * @param allSubDomain the all sub domain
     * @since 1.0.0
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String name,
                                 String value,
                                 int maxAge,
                                 boolean allSubDomain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        if (allSubDomain) {
            String serverName = request.getServerName();
            String domain = getDomainOfServerName(serverName);
            if (domain != null && domain.indexOf(CharPool.DOT) != -1) {
                cookie.setDomain(CharPool.DOT + domain);
            }
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获取用户访问URL中的根域名
     * 例如: www.dlog.cn -> dlog.cn
     *
     * @param host the host
     * @return domain of server name
     * @since 1.0.0
     */
    @Nullable
    @SuppressWarnings(value = {"PMD.UndefineMagicConstantRule", "checkstyle:ReturnCount"})
    public static String getDomainOfServerName(String host) {
        if (isIpAddr(host)) {
            return null;
        }
        String[] names = StringUtils.split(host, StringPool.DOT);
        int len = Objects.requireNonNull(names).length;
        if (len == 1) {
            return null;
        }
        int www = 3;
        if (len == www) {
            return makeup(names[len - 2], names[len - 1]);
        }
        if (len > www) {
            String dp = names[len - 2];
            if ("com".equalsIgnoreCase(dp)
                || "gov".equalsIgnoreCase(dp)
                || "net".equalsIgnoreCase(dp)
                || "edu".equalsIgnoreCase(dp)
                || "org".equalsIgnoreCase(dp)) {
                return makeup(names[len - 3], names[len - 2], names[len - 1]);
            } else {
                return makeup(names[len - 2], names[len - 1]);
            }
        }
        return host;
    }

    /**
     * Makeup string
     *
     * @param ps ps
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    private static String makeup(@NotNull String... ps) {
        StringBuilder s = new StringBuilder();
        for (int idx = 0; idx < ps.length; idx++) {
            if (idx > 0) {
                s.append('.');
            }
            s.append(ps[idx]);
        }
        return s.toString();
    }

    /**
     * Delete cookie.
     *
     * @param request      the request
     * @param response     the response
     * @param name         the name
     * @param allSubDomain the all sub domain
     * @since 1.0.0
     */
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, String name, boolean allSubDomain) {
        setCookie(request, response, name, "", 0, allSubDomain);
    }

    /**
     * 获取HTTP端口
     *
     * @param req the req
     * @return http port
     * @since 1.0.0
     */
    public static int getHttpPort(@NotNull HttpServletRequest req) {
        try {
            return new URL(req.getRequestURL().toString()).getPort();
        } catch (MalformedURLException excp) {
            return 80;
        }
    }

    /**
     * 获取浏览器提交的整形参数
     *
     * @param req          the req
     * @param param        the param
     * @param defaultValue the default value
     * @return param param
     * @since 1.0.0
     */
    public static int getParam(@NotNull HttpServletRequest req, String param, int defaultValue) {
        return NumberUtils.toInt(req.getParameter(param), defaultValue);
    }

    /**
     * 获取浏览器提交的整形参数
     *
     * @param req          the req
     * @param param        the param
     * @param defaultValue the default value
     * @return param param
     * @since 1.0.0
     */
    public static long getParam(@NotNull HttpServletRequest req, String param, long defaultValue) {
        return NumberUtils.toLong(req.getParameter(param), defaultValue);
    }

    /**
     * 获取浏览器提交的字符串参数?
     *
     * @param req          the req
     * @param param        the param
     * @param defaultValue the default value
     * @return param param
     * @since 1.0.0
     */
    public static String getParam(@NotNull HttpServletRequest req, String param, String defaultValue) {
        String value = req.getParameter(param);
        return (StringUtils.isEmpty(value)) ? defaultValue : value;
    }

    /**
     *  获取请求参数
     *
     * @param request the request
     * @return parameters map
     * @since 1.0.0
     */
    public static @NotNull
    Map<String, String> getParameterMap(@NotNull HttpServletRequest request) {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);

        Map<String, String> returnMap = Maps.newHashMapWithExpectedSize(16);
        //request.getParameterMap() 返回的是一个Map类型的值,该返回值记录着前端 (如jsp页面) 所提交请求中的请求参数和请求参数值的映射关系. 这个返回值有个特别之处——只能读.
        Map<String, String[]> parameterMap = cachingRequestWrapper.getParameterMap();
        //  Map.Entry是Map声明的一个内部接口,此接口为泛型,定义为Entry. 它表示Map中的一个实体 (一个key-value对) . 接口中有getKey(),getValue方法.
        for (Map.Entry<String, String[]> en : parameterMap.entrySet()) {
            String value = cachingRequestWrapper.getParameter(en.getKey());
            try {
                returnMap.put(en.getKey(), URLDecoder.decode(value, Charsets.UTF_8_NAME));
            } catch (Exception e) {
                log.error("url decode error", e);
            }
        }
        return returnMap;
    }

    /**
     * Get header map.
     *
     * @param request the request
     * @return the map
     * @since 1.0.0
     */
    public static @NotNull Map<String, String> getHeader(@NotNull HttpServletRequest request) {
        // 获取所有的消息头名称
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headers = Maps.newHashMapWithExpectedSize(16);
        // 获取获取的消息头名称,获取对应的值,并输出
        while (headerNames.hasMoreElements()) {
            String nextElement = headerNames.nextElement();
            headers.put(nextElement, request.getHeader(nextElement));
        }
        return headers;
    }

    /**
     * Get header string
     *
     * @param headerName header name
     * @return the string
     * @since 1.0.0
     */
    public static String getHeader(String headerName) {
        Map<String, String> headers = getHeader(getRequest());
        return headers.get(headerName);
    }

    /**
     * 是否multipart/form-data or application/octet-stream表单提交方式
     *
     * @param request the request
     * @return boolean boolean
     * @since 1.0.0
     */
    public static boolean isMultipartContent(@NotNull HttpServletRequest request) {
        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        contentType = contentType.toLowerCase(Locale.ENGLISH);
        return contentType.startsWith("multipart/") || MediaType.APPLICATION_OCTET_STREAM_VALUE.equals(contentType);
    }

    /**
     * Build query params map map.
     *
     * @param request the request
     * @return the map
     * @since 1.0.0
     */
    public static @NotNull Map<String, Object> buildQueryParamsMap(@NotNull HttpServletRequest request) {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        Map<String, Object> params = Maps.newHashMap();
        Enumeration<String> e = cachingRequestWrapper.getParameterNames();

        StringBuilder tmpbuff = new StringBuilder();
        if (e.hasMoreElements()) {
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = cachingRequestWrapper.getParameterValues(name);
                if (values.length == 1) {
                    if (StringUtils.isNotBlank(values[0])) {
                        params.put(name, values[0]);
                    }
                } else {
                    tmpbuff.setLength(0);
                    for (String value : values) {
                        if (StringUtils.isNotBlank(value)) {
                            tmpbuff.append(value.trim()).append(",");
                        }
                    }
                    if (tmpbuff.length() > 0) {
                        tmpbuff.deleteCharAt(tmpbuff.length() - 1);
                        params.put(name, tmpbuff.toString());
                    }
                }
            }
        }
        return params;
    }

    /**
     * Gets typesafe request map *
     *
     * @param request request
     * @return the typesafe request map
     * @since 1.0.0
     */
    private @NotNull Map<String, String> getTypesafeRequestMap(@NotNull HttpServletRequest request) {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        Enumeration<?> requestParamNames = cachingRequestWrapper.getParameterNames();
        Map<String, String> typesafeRequestMap = Maps.newHashMap();

        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue = cachingRequestWrapper.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }

        return typesafeRequestMap;
    }

    /**
     * 获取请求 Body, 注意: 调用此方法后还需要二次读取 request时, HttpServletRequest 必须为 CacheRequestWrapper
     *
     * @param request the request
     * @return the string
     * @since 1.0.0
     */
    public static String getBody(@NotNull HttpServletRequest request) {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        String bodyInfo = "";
        try (InputStream is = cachingRequestWrapper.getInputStream()) {
            bodyInfo = IoUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
        return StringUtils.replaceBlank(bodyInfo);
    }

    /**
     * 将 url 参数转换成 map
     *
     * @param param aa=11&bb=22&cc=33
     * @return url params
     * @since 1.0.0
     */
    public static @NotNull Map<String, String> getUrlParams(String param) {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isBlank(param)) {
            return map;
        }
        if (param.startsWith(StringPool.QUESTION_MARK)) {
            param = StringUtils.subAfter(param, StringPool.QUESTION_MARK);
        }
        String[] params = param.split(StringPool.AMPERSAND);
        for (String s : params) {
            String[] p = s.split(StringPool.EQUALS);
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将 map 转换成 url
     *
     * @param map map
     * @return url params by map
     * @since 1.0.0
     */
    @Contract("null -> !null")
    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(StringPool.EQUALS).append(entry.getValue()).append(StringPool.AMPERSAND);
        }
        String s = sb.toString();
        if (s.endsWith(StringPool.AMPERSAND)) {
            s = StringUtils.subBefore(s, StringPool.AMPERSAND);
        }
        return s;
    }

    /**
     * Gets cache input stream *
     *
     * @param body body
     * @return the cache input stream
     * @since 1.0.0
     */
    @NotNull
    public static ServletInputStream getCacheInputStream(byte[] body) {
        ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    /**
     * 将 url 参数解析为 map
     *
     * @param url the url
     * @return the query params
     * @since 1.5.0
     */
    public static @NotNull
    Map<String, Object> converterToMap(@NotNull String url) {
        try {
            Map<String, Object> params = new HashMap<>(16);
            for (String param : url.split(StringPool.AMPERSAND)) {
                String[] pair = param.split(StringPool.EQUALS);
                String key = URLDecoder.decode(pair[0], Charsets.UTF_8_NAME);
                String value = StringPool.EMPTY;
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], Charsets.UTF_8_NAME);
                }
                params.put(key, value);
            }
            return params;
        } catch (UnsupportedEncodingException ex) {
            log.error("{}", ex.getMessage());
        }
        return Collections.emptyMap();
    }
}
