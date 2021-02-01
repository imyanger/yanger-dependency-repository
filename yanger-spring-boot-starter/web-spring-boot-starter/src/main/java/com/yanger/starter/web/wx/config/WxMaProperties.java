package com.yanger.starter.web.wx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import lombok.Data;

/**
 * @Description 微信配置类
 * @Author yanger
 * @Date 2021/1/11 17:58
 */
@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

    /**
     * 微信授权信息配置，集合形式
     * <p>
     * appSign：应用标识（当前服务唯一）
     * appid：设置微信小程序的appid
     * secret：设置微信小程序的Secret
     * token：设置微信小程序消息服务器配置的token
     * aesKey：设置微信小程序消息服务器配置的EncodingAESKey
     * msgDataFormat：消息格式，XML或者JSON
     */
    private List<Config> configs;

    @Data
    public static class Config {

        /** 应用标识（当前服务唯一） */
        private String appSign;

        /** 设置微信小程序的appid */
        private String appid;

        /** 设置微信小程序的Secret */
        private String secret;

        /** 设置微信小程序消息服务器配置的token */
        private String token;

        /** 设置微信小程序消息服务器配置的EncodingAESKey */
        private String aesKey;

        /** 消息格式，XML或者JSON */
        private String msgDataFormat;
    }

}
