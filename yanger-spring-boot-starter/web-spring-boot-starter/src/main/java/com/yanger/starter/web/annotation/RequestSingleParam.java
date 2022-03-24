package com.yanger.starter.web.annotation;

import java.lang.annotation.*;

/**
 * 解析 json 格式的参数, 可直接注入单个参数到 controller
 * @Author yanger
 * @Date 2021/1/27 18:17
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestSingleParam {

    /**
     * Value string
     * @return the string
     */
    String value();

    /**
     * Required boolean
     * @return the boolean
     */
    boolean required() default true;

    /**
     * Default value string
     * @return the string
     */
    String defaultValue() default "";

}
