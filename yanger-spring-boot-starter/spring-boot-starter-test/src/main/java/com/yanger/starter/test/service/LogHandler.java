package com.yanger.starter.test.service;

import com.yanger.starter.log.entity.LogInfo;
import com.yanger.starter.log.interceptor.ILogHandler;

import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/3/16 17:33
 */
@Component
public class LogHandler implements ILogHandler {

    @Override
    public void handler(LogInfo logInfo) {
        System.out.println(logInfo);
    }

}
