package com.yanger.starter.log.annotation;

import java.lang.annotation.*;

/**
 * 业务日志注解
 * @Author yanger
 * @Date 2021/3/10 14:00
 */
@Target( {ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface BusinessLog {

    String description()  default "";

}
