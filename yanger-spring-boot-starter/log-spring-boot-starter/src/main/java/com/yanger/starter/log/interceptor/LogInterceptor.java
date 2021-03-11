package com.yanger.starter.log.interceptor;

import com.yanger.starter.log.annotation.BusinessLog;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 登录拦截器
 * @Author yanger
 * @Date 2020/9/15 15:38
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    /** 记录请求时间 */
    private static final ThreadLocal<Long> threadLocalTime = new ThreadLocal<>();

    /**
     * 打印请求数据
     *
     * @param request      request
     * @param response     response
     * @param handler      handler
     * @param modelAndView model and view
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod) handler;
            Method method = h.getMethod();
            String description = h.hasMethodAnnotation(BusinessLog.class) ? h.getMethodAnnotation(BusinessLog.class).description() : "";
            Class<?> declaringClass = method.getDeclaringClass();
            String simpleName = declaringClass.getSimpleName();
            String methodName = method.getName();
            Long startTime = threadLocalTime.get();
            threadLocalTime.remove();
            long exeTimes = System.currentTimeMillis() - startTime;
            log.info("{}[{}-{}]({}) 执行耗时{}ms", description, simpleName, methodName, request.getRequestURI(), exeTimes);
        }
    }

    /**
     * 获取请求开始时间
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return the boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        threadLocalTime.set(System.currentTimeMillis());
        return true;
    }

}
