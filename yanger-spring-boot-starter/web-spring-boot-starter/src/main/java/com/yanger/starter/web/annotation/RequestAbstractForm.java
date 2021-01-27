package com.yanger.starter.web.annotation;

import com.yanger.starter.web.support.SubClassType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 解析 json 格式的参数, 可直接注入单个参数到 controller
 * @Author yanger
 * @Date 2021/1/27 18:48
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestAbstractForm {

    /**
     * 指定抽象类的需要被绑定的类型的枚举, 此枚举必须实现 SerializeEnum 接口
     *
     * @return the class
     */
    Class<? extends SubClassType> value();

    /**
     * Required boolean
     *
     * @return the boolean
     */
    boolean required() default true;

}
