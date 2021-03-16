package com.yanger.starter.log.interceptor;

import com.yanger.starter.log.entity.LogInfo;

/**
 * @Description 日志处理器
 * @Author yanger
 * @Date 2021/3/16 17:11
 */
public interface ILogHandler {

    /**
     * @Description 处理接口拦截信息（交由子类实现）
     * @Author yanger
     * @Date 2021/3/16 17:34
     * @param: logInfo
     * @throws
     */
    void handler(LogInfo logInfo);

}
