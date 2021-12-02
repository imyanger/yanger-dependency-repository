package com.yanger.starter.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 强制要求登录验证（与 @IgnoreLoginAuth 同时作用时，@LoginAuth 生效，依然需要验证）
 * @Author yanger
 * @Date 2020/9/15 15:38
 */
@Target( {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAuth {

}