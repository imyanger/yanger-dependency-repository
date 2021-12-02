package com.yanger.starter.test.service;

import com.yanger.starter.log.entity.LogInfo;
import com.yanger.starter.log.interceptor.ILogHandler;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 * @Author yanger
 * @Date 2021/3/16 17:33
 */
@Component
public class LogHandler implements ILogHandler {

    @Override
    public void handler(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView, LogInfo logInfo) {
        System.out.println(logInfo);
    }

}
