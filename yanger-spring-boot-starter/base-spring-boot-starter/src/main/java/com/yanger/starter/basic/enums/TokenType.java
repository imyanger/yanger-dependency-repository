package com.yanger.starter.basic.enums;

/**
 * 用户信息存储方式，session 还是 header 获取
 * @Author yanger
 * @Date 2020/12/29 16:12
 */
public enum TokenType {

    /** header 获取用户信息 */
    HEADER,

    /** session 获取用户信息 */
    SESSION;

}
