package com.yanger.starter.web.api;

import com.yanger.starter.web.config.TokenConfig;
import com.yanger.starter.web.entity.AuthUser;
import com.yanger.starter.web.entity.LoginData;
import com.yanger.starter.web.entity.WxLoginData;
import com.yanger.starter.web.service.LoginService;
import com.yanger.starter.web.wx.agent.WxAgent;
import com.yanger.starter.web.wx.config.WxMaProperties;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.tools.JwtUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import io.swagger.annotations.Api;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @Description Notebook的接口Controller类
 * @Author yanger
 * @Date 2020-12-04 23:07:44
 */
@Api
@RestController
public class IndexApi extends BaseApi {

    @Autowired(required = false)
    private LoginService loginService;

    @Autowired
    private WxAgent wxAgent;

    @Autowired
    private WxMaProperties wxMaProperties;

    @Resource
    private TokenConfig tokenConfig;

    @PostMapping("login")
    public void login(@RequestBody LoginData loginData) {
        AuthUser authUser = loginService.login(loginData, request);
        setToken(authUser);
    }

    /**
     * @throws
     * @Description 微信小程序授权登录
     * @Author yanger
     * @Date 2021/1/14 17:23
     * @param: wxLoginData
     * @return: com.yanger.notepad.po.NoteUser
     */
    @PostMapping("wxMiniLogin")
    public void wxLogin(@RequestBody WxLoginData wxLoginData) {
        try {
            WxMaProperties.Config config = wxMaProperties.getConfigs().stream().filter(s -> StringUtils.equals(s.getAppSign(),
                                                                                                               wxLoginData.getAppSign())).findAny().orElse(null);
            if (config == null) {
                throw new BasicException("无法获取 appSign【{}】 对应的授权配置信息", wxLoginData.getAppSign());
            }
            WxMaJscode2SessionResult sessionResult = wxAgent.login(config.getAppid(), wxLoginData.getCode());
            wxLoginData.setOpenId(sessionResult.getOpenid());
            AuthUser authUser = loginService.wxLogin(wxLoginData, request);
            setToken(authUser);
        } catch (WxErrorException e) {
            throw new BasicException("微信授权失败");
        }
    }

    /**
     * @throws
     * @Description 微信登录
     * @Author yanger
     * @Date 2021/1/14 17:22
     * @param: wxLoginData
     * @return: com.yanger.notepad.po.NoteUser
     */
    @PostMapping("wxAppLogin")
    public void wxAppLogin(@RequestBody WxLoginData wxLoginData) {
        AuthUser authUser = loginService.wxLogin(wxLoginData, request);
        setToken(authUser);
    }

    /**
     * @throws
     * @Description 设置 Token
     * @Author yanger
     * @Date 2021/1/27 15:23
     * @param: authUser
     */
    private void setToken(AuthUser authUser) {
        response.setHeader(tokenConfig.getHeaderKey(), JwtUtils.sign(authUser, tokenConfig.getAvailableTimeMinute()));
    }

}
