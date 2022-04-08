package com.yanger.starter.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录数据
 * @Author yanger
 * @Date 2020/12/10 18:17
 */
@Data
@ApiModel(value = "登录数据")
public class LoginData {

    /** 登录账号 */
    @ApiModelProperty("登录账号")
    private String username;

    /** 登录密码 */
    @ApiModelProperty("登录密码")
    private String password;

    /** 验证码 */
    @ApiModelProperty("验证码")
    private String randomCode;

    /** 手机验证码 */
    @ApiModelProperty("手机验证码")
    private String mobileCode;

}
