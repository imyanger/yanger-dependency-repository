package com.yanger.starter.web.xss;

import com.yanger.starter.web.support.CacheRequestEnhanceWrapper;

import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.*;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;

/**
 * @Description XSS过滤
 * @Author yanger
 * @Date 2021/1/27 16:21
 */
@AllArgsConstructor
public class XssFilter implements Filter {

    /** Xss properties */
    private final List<String> excludePatterns;

    /**
     * Init *
     *
     * @param config config
     */
    @Override
    public void init(FilterConfig config) {}

    /**
     * Do filter *
     *
     * @param request  request
     * @param response response
     * @param chain    chain
     * @throws IOException      io exception
     * @throws ServletException servlet exception
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest) request).getServletPath();
        if (this.excludePatterns.stream().anyMatch(path::contains)) {
            chain.doFilter(request, response);
        } else {
            if (request instanceof CacheRequestEnhanceWrapper) {
                chain.doFilter(new XssHttpServletRequestWrapper(((CacheRequestEnhanceWrapper) request).getCachingRequestWrapper()),
                               response);
            } else {
                chain.doFilter(new XssHttpServletRequestWrapper(new ContentCachingRequestWrapper((HttpServletRequest) request)),
                               response);
            }
        }
    }

    /**
     * Destroy
     */
    @Override
    public void destroy() {}

}
