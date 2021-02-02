package com.yanger.starter.test.service;

import com.yanger.starter.web.entity.AuthUser;
import com.yanger.starter.web.entity.LoginData;
import com.yanger.starter.web.entity.WxLoginData;
import com.yanger.starter.web.service.LoginService;
import com.yanger.tools.web.tools.KaptchaGenerator;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 默认的登录后处理逻辑
 * @Author yanger
 * @Date 2021/2/2 18:18
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    /**
     * @Description 处理登录逻辑
     * @Author yanger
     * @Date 2021/1/27 15:50
     * @param: loginData
     * @param: request
     * @return: com.yanger.starter.web.entity.AuthUser
     */
    @Override
    public AuthUser login(LoginData loginData, HttpServletRequest request) {
        return null;
    }

    /**
     * @Description 处理微信登录的逻辑
     * @Author yanger
     * @Date 2021/1/27 15:51
     * @param: wxLoginData
     * @param: request
     * @return: com.yanger.starter.web.entity.AuthUser
     */
    @Override
    public AuthUser wxLogin(WxLoginData wxLoginData, HttpServletRequest request) {
        return null;
    }

    /**
     * @Description 验证码获取的处理
     * @Author yanger
     * @Date 2021/2/2 18:16
     * @param: kaptchaData
     */
    @Override
    public void randomCodeAfter(KaptchaGenerator.KaptchaData kaptchaData) {
        log.info(kaptchaData.toString());
    }

}
