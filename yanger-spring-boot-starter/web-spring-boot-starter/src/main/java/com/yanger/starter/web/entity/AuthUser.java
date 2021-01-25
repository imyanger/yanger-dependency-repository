package com.yanger.starter.web.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 用户信息
 * @Author yanger
 * @Date 2020/12/10 18:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser {

    /** 用户id */
    private Long userId;

    /** 用户姓名 */
    private String userName;

    /** 用户账号 */
    private String userCode;

    /** 手机号码 */
    private String mobile;

}
