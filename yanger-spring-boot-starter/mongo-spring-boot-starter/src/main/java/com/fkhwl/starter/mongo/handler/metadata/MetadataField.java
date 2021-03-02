package com.fkhwl.starter.mongo.handler.metadata;

import com.fkhwl.starter.mongo.enums.FieldFill;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 13:10
 * @since 1.0.0
 */

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
