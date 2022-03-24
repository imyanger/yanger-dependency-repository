package com.yanger.starter.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注枚举的名称， 如果使用 @EnumName， 则 SerializeEnumContainer 将使用其 value 作为容器 key
 * @Author yanger
 * @Date 2020/9/15 15:38
 */
@Target( {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumName {

    String value() default "";

}