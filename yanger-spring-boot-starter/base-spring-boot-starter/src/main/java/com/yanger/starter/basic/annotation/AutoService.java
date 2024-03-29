package com.yanger.starter.basic.annotation;

import java.lang.annotation.*;

/**
 * 自动装配标志
 * @Author yanger
 * @Date 2021/1/29 10:40
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target( {ElementType.TYPE})
public @interface AutoService {

    Class<?>[] value();

}
