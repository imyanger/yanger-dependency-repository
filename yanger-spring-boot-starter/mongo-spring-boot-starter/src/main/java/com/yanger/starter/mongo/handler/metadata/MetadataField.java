package com.yanger.starter.mongo.handler.metadata;

import com.yanger.starter.mongo.enums.FieldFill;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MetadataField {

    /**
     * Value string
     *
     * @return the string
     */
    String value() default "";

    /**
     * 字段自动填充策略
     *
     * @return the field fill
     */
    FieldFill fill() default FieldFill.DEFAULT;

}
