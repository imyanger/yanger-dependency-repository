package com.yanger.starter.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 密码加密处理，使用 EncryptUtils.getMD5()进行加密
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordField {
}
