package com.yanger.starter.web.api;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础的 api 基类
 * @Author yanger
 * @Date 2021/1/27 15:24
 */
public class BaseApi {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

}
