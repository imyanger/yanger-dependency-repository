package com.fkhwl.starter.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.15 12:21
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * key 前缀
     *
     * @return the string
     * @since 1.0.0
     */
    String prefix() default "";

    /**
     * Key
     *
     * @return the string
     * @since 1.6.0
     */
    String key() default "";

    /**
     * 自动释放锁的超时时间
     *
     * @return the int
     * @since 1.0.0
     */
    int expire() default 5;

    /**
     * 超时释放锁的时间单位
     *
     * @return the time unit
     * @since 1.0.0
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 重试次数
     *
     * @return the int
     * @since 1.0.0
     */
    int retries() default 3;

}
