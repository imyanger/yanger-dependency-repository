package com.yanger.starter.web.api;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.yanger.starter.web.entity.AuthUser;
import com.yanger.starter.web.entity.LoginData;
import com.yanger.starter.web.entity.WxLoginData;
import com.yanger.starter.web.property.TokenProperties;
import com.yanger.starter.web.service.ILoginService;
import com.yanger.starter.web.wx.agent.WxAgent;
import com.yanger.starter.web.wx.property.WxMaProperties;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.tools.JwtUtils;
import com.yanger.tools.web.tools.KaptchaGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 登录相关接口
 * @Author yanger
 * @Date 2020-12-04 23:07:44
 */
@Api(tags={"IndexApi：登录相关接口"})
@RestController
public class IndexApi extends BaseApi {

    @Autowired(required = false)
    private ILoginService loginService;

    @Autowired
    private WxAgent wxAgent;

    @Autowired
    private WxMaProperties wxMaProperties;

    @Resource
    private TokenProperties tokenProperties;

    @PostMapping("login")
    @ApiOperation(value="用户登录", tags={"IndexApi：登录相关接口"}, notes="用户登录")
    public void login(@RequestBody LoginData loginData) {
        AuthUser authUser = loginService.login(loginData, request);
        setToken(authUser);
    }

    /**
     * 微信小程序授权登录
     * @Author yanger
     * @Date 2021/1/14 17:23
     * @param wxLoginData
     * @return com.yanger.notepad.po.NoteUser
     */
    @PostMapping("wxMiniLogin")
    @ApiOperation(value="微信小程序授权登录", tags={"IndexApi：登录相关接口"}, notes="微信小程序授权登录")
    public void wxLogin(@RequestBody WxLoginData wxLoginData) {
        try {
            WxMaProperties.Config config = wxMaProperties.getConfigs().stream()
                    .filter(s -> StringUtils.equals(s.getAppSign(), wxLoginData.getAppSign())).findAny().orElse(null);
            if (config == null) {
                throw BasicException.of("无法获取 appSign【{}】 对应的授权配置信息", wxLoginData.getAppSign());
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
     * 手机微信登录
     * @Author yanger
     * @Date 2021/1/14 17:22
     * @param wxLoginData
     * @return com.yanger.notepad.po.NoteUser
     */
    @PostMapping("wxAppLogin")
    @ApiOperation(value="手机微信登录", tags={"IndexApi：登录相关接口"}, notes="手机微信登录")
    public void wxAppLogin(@RequestBody WxLoginData wxLoginData) {
        AuthUser authUser = loginService.wxLogin(wxLoginData, request);
        setToken(authUser);
    }

    /**
     * 获取验证码 base64 字符串
     * @Author yanger
     * @Date 2021/2/2 18:12
     * @return: com.yanger.tools.web.entity.Result<java.lang.String>
     * @throws
     */
    @GetMapping("randomCode")
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    public String randomCode() {
        Optional<KaptchaGenerator.KaptchaData> optional = new KaptchaGenerator().create();
        if (!optional.isPresent()) {
            throw new BasicException("生成验证码异常");
        }
        KaptchaGenerator.KaptchaData kaptchaData = optional.get();
        request.getSession().setAttribute(tokenProperties.getTokenRandomCode(), kaptchaData.getCapVal());
        loginService.randomCodeAfter(kaptchaData);
        return kaptchaData.getBase64Img();
    }

    /**
     * 获取计算型验证码 base64 字符串
     * @Author yanger
     * @Date 2021/2/2 18:12
     * @return com.yanger.tools.web.entity.Result<java.lang.String>
     */
    @GetMapping("randomCode/cal")
    @ApiOperation(value = "获取计算型验证码", notes = "获取计算型验证码")
    public String randomCodeCal() {
        Optional<KaptchaGenerator.KaptchaData> optional = new KaptchaGenerator().createCal();
        if (!optional.isPresent()) {
            throw new BasicException("生成验证码异常");
        }
        KaptchaGenerator.KaptchaData kaptchaData = optional.get();
        request.getSession().setAttribute(tokenProperties.getTokenRandomCode(), kaptchaData.getCapVal());
        loginService.randomCodeAfter(kaptchaData);
        return kaptchaData.getBase64Img();
    }

    /**
     * 设置 Token
     * @Author yanger
     * @Date 2021/1/27 15:23
     * @param authUser
     */
    private void setToken(AuthUser authUser) {
        response.setHeader(tokenProperties.getHeaderKey(), JwtUtils.sign(authUser, tokenProperties.getAvailableTimeMinute()));
    }

}
