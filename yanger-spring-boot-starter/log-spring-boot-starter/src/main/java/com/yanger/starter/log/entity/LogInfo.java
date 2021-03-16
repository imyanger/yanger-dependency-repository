package com.yanger.starter.log.entity;

import org.springframework.web.method.HandlerMethod;

import java.io.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 拦截日志对象
 * @Author yanger
 * @Date 2021/3/16 17:05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 描述 */
    private  String description;

    /** 简单类名 */
    private  String simpleClassName;

    /** 命名 */
    private  String className;

    /** 方法名 */
    private  String methodName;

    /** 请求URI */
    private  String requestURI;

    /** 执行时间 */
    private  Long exeTimes;

    /** handlerMethod */
    private HandlerMethod handlerMethod;

}
