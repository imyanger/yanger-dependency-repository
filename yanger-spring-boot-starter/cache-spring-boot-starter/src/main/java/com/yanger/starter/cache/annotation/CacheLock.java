package com.yanger.starter.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于 redis 分布式锁
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * key 前缀
     */
    String prefix() default "";

    /**
     * Key
     */
    String key() default "";

    /**
     * 自动释放锁的超时时间
     */
    int expire() default 5;

    /**
     * 超时释放锁的时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 重试次数
     */
    int retries() default 3;

}
