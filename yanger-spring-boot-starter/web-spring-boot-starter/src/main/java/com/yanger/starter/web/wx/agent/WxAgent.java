package com.yanger.starter.web.wx.agent;

import com.yanger.starter.web.wx.config.WxMaConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @Description 微信请求代理类
 * @Author yanger
 * @Date 2021/1/11 17:58
 */
@Slf4j
@Component
public class WxAgent {

    /**
     * 登陆接口
     */
    public WxMaJscode2SessionResult login(String appid, String code) throws WxErrorException {
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("code不能为空");
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
        return wxService.getUserService().getSessionInfo(code);
    }

    /**
     * 获取用户信息接口
     */
    public WxMaUserInfo info(String appid, String sessionKey, String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            throw new RuntimeException("user check failed");
        }
        // 解密用户信息
        return wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
    }

    /**
     * 获取用户绑定手机号信息
     */
    public WxMaPhoneNumberInfo phone(String appid, String sessionKey, String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);
        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            throw new RuntimeException("user check failed");
        }
        // 解密
        return wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
    }

}
