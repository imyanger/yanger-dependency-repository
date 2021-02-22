package com.fkhwl.starter.cache.aop;

import com.fkhwl.starter.cache.annotation.CacheLock;
import com.fkhwl.starter.cache.el.AspectSupportUtils;
import com.fkhwl.starter.cache.exception.CacheLockException;
import com.fkhwl.starter.cache.util.CacheLockUtils;
import com.fkhwl.starter.core.util.StringUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: spring-integration 对 redis 分布锁的支持,底层应该也是 lua 脚本的实现,可完美解决线程挂掉造成的死锁,以及执行时间过长锁释放掉,误删别人的锁 </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.15 12:26
 * @since 1.0.0
 */
@Slf4j
@Aspect
public class CacheLockAspect {

    /**
     * Cache lock point cut *
     *
     * @param cacheLock cache lock
     * @since 1.0.0
     */
    @Pointcut("@annotation(cacheLock)")
    public void cacheLockPointCut(CacheLock cacheLock) {
    }

    /**
     * Around object
     *
     * @param joinPoint join point
     * @param cacheLock cache lock
     * @return the object
     * @throws Throwable throwable
     * @since 1.0.0
     */
    @Around(value = "cacheLockPointCut(cacheLock)", argNames = "joinPoint,cacheLock")
    public Object around(@NotNull ProceedingJoinPoint joinPoint, @NotNull CacheLock cacheLock)
        throws Throwable {

        if (StringUtils.isEmpty(cacheLock.prefix())) {
            throw new CacheLockException("prefix 不能为空, 请在 @CacheLock 中定义 prefix");
        }

        String key = this.buildKey(joinPoint, cacheLock);
        int retries = cacheLock.retries() <= 0 ? 1 : cacheLock.retries();
        Lock lock = CacheLockUtils.lock(key, retries, cacheLock.expire(), cacheLock.timeUnit());
        try {
            log.info("[{}] 成功获取到分布式锁 key = {}, 开始执行...", Thread.currentThread().getName(), key);
            return joinPoint.proceed();
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                // 如果 redis <4.0, 将抛出 The UNLINK command has failed (not supported on the Redis server?)
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Gets cache keys *
     *
     * @param joinPoint join point
     * @param cacheLock cache lock
     * @return the cache keys
     * @since 1.0.0
     */
    @NotNull
    private String buildKey(@NotNull ProceedingJoinPoint joinPoint,
                            @NotNull CacheLock cacheLock) {
        return cacheLock.prefix() + AspectSupportUtils.getKeyValue(joinPoint, cacheLock.key());
    }

}
