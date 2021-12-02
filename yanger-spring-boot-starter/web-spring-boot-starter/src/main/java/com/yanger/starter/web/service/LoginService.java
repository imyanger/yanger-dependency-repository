package com.yanger.starter.web.service;

import com.yanger.starter.web.entity.AuthUser;
import com.yanger.starter.web.entity.LoginData;
import com.yanger.starter.web.entity.WxLoginData;
import com.yanger.tools.web.tools.KaptchaGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理登录的接口
 * @Author yanger
 * @Date 2021/1/27 14:59
 */
public interface LoginService {

    /**
     * @throws
     * 处理登录逻辑
     * @Author yanger
     * @Date 2021/1/27 15:50
     * @param: loginData
     * @param: request
     * @return: com.yanger.starter.web.entity.AuthUser
     */
    AuthUser login(LoginData loginData, HttpServletRequest request);

    /**
     * @throws
     * 处理微信登录的逻辑
     * @Author yanger
     * @Date 2021/1/27 15:51
     * @param: wxLoginData
     * @param: request
     * @return: com.yanger.starter.web.entity.AuthUser
     */
    AuthUser wxLogin(WxLoginData wxLoginData, HttpServletRequest request);

    /**
     * 验证码获取的处理
     * @Author yanger
     * @Date 2021/2/2 18:16
     * @param: kaptchaData
     * @throws
     */
    void randomCodeAfter(KaptchaGenerator.KaptchaData kaptchaData);

}
