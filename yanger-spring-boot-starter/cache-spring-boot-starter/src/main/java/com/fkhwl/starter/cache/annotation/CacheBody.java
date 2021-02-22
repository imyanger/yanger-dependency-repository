package com.fkhwl.starter.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 用于注解自建对象 </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.15 12:23
 * @since 1.0.0
 * @deprecated 请使用 EL 表达式代替
 */
@Target(value = {ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Deprecated
public @interface CacheBody {

}
