package com.yanger.starter.web.service;

import com.yanger.starter.web.entity.AuthUser;
import com.yanger.starter.web.entity.LoginData;
import com.yanger.starter.web.entity.WxLoginData;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 处理登录的接口
 * @Author yanger
 * @Date 2021/1/27 14:59
 */
@Service
public interface LoginService {

    /**
     * @Description 处理登录逻辑
     * @Author yanger
     * @Date 2021/1/27 15:50
     * @param: loginData
     * @param: request
     * @return: com.yanger.starter.web.entity.AuthUser
     * @throws
     */
    AuthUser login(LoginData loginData, HttpServletRequest request);

    /**
     * @Description 处理微信登录的逻辑
     * @Author yanger
     * @Date 2021/1/27 15:51
     * @param: wxLoginData
     * @param: request
     * @return: com.yanger.starter.web.entity.AuthUser
     * @throws
     */
    AuthUser wxLogin(WxLoginData wxLoginData, HttpServletRequest request);

}
