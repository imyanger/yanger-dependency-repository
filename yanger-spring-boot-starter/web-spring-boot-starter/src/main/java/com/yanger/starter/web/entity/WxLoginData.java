package com.yanger.starter.web.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信登录数据
 * @Author yanger
 * @Date 2020-12-04 23:07:44
 */
@Data
@ApiModel(value = "微信登录数据")
public class WxLoginData implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 应用标识（当前服务唯一） */
    @ApiModelProperty("应用标识（当前服务唯一）")
    private String appSign;

    /** 用户名 */
    @ApiModelProperty("用户名")
    private String userName;

    /** 头像图片 */
    @ApiModelProperty("头像图片")
    private String avatarUrl;

    /** 性别 */
    @ApiModelProperty("性别")
    private Integer gender;

    /** 微信授权code */
    @ApiModelProperty("微信授权code")
    private String code;

    /** openId */
    @ApiModelProperty("openId")
    private String openId;

}