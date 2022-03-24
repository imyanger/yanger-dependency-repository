package com.yanger.starter.basic.annotation;

import com.yanger.starter.basic.enums.ApplicationType;

import java.lang.annotation.*;

/**
 * 应用运行类型
 * @Author yanger
 * @Date 2020/12/29 16:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RunningType {

    ApplicationType value();

}
