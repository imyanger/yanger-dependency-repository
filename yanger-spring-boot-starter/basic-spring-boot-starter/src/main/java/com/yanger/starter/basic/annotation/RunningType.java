package com.yanger.starter.basic.annotation;

import com.yanger.starter.basic.enums.ApplicationType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 应用运行类型
 * @Author yanger
 * @Date 2020/12/29 16:12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RunningType {

    /**
     * Value application type
     *
     * @return the application type
     * @since 1.0.0
     */
    ApplicationType value();
}
