package com.yanger.starter.log.interceptor;

import com.yanger.starter.log.entity.LogInfo;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志处理器
 * @Author yanger
 * @Date 2021/3/16 17:11
 */
public interface ILogHandler {

    /**
     * 处理接口拦截信息（交由子类实现）
     * @Author yanger
     * @Date 2021/3/16 17:34
     * @param: logInfo
     * @throws
     */
    void handler(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView, LogInfo logInfo);

}
