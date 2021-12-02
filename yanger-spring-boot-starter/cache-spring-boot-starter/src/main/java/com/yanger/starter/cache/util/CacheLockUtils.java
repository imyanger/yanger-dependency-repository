package com.yanger.starter.cache.util;

import com.yanger.starter.basic.context.EarlySpringContext;
import com.yanger.starter.cache.exception.CacheLockException;
import com.yanger.tools.general.format.StringFormat;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * redis lock 工具
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class CacheLockUtils {

    /** Redis lock registry */
    private static RedisLockRegistry redisLockRegistry;

    /** Redis template */
    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * Lock
     *
     * @param key     key 资源标识
     * @param retries retries 重试次数，最少一次
     * @param expire  expire 重试时间
     * @param unit    unit 重试时间单位
     * @return the lock   释放锁：lock.unlock();
     * @throws InterruptedException interrupted exception
     */
    public static @NotNull Lock lock(String key, int retries, int expire, TimeUnit unit) throws InterruptedException {
        if (CacheLockUtils.redisLockRegistry == null) {
            CacheLockUtils.redisLockRegistry = EarlySpringContext.getInstance(RedisLockRegistry.class);
        }
        Lock lock = CacheLockUtils.redisLockRegistry.obtain(key);
        boolean lockFlag;
        retries = retries <= 0 ? 1 : retries;
        // 循环 retries 次去尝试获取锁
        for (int i = 0; i < retries; i++) {
            lockFlag = lock.tryLock(expire, unit);
            if (lockFlag) {
                return lock;
            }
        }
        throw new CacheLockException(StringFormat.format("尝试 {} 次获取分布式锁失败, 耗时: [{} {}]  ",
                                                            retries, retries * expire, unit.name()));
    }

    /**
     * Lock by expire
     *
     * @param key     key
     * @param retries retries 重试次数，最少一次
     * @param seconds seconds 重试时间，单位秒
     * @return the boolean
     * @throws InterruptedException interrupted exception
     */
    public static @NotNull Boolean lockByExpire(String key, int retries, int seconds) throws InterruptedException {
        return lockByExpire(key, retries, seconds * 1000, 30, TimeUnit.SECONDS);
    }

    /**
     * Lock by expire
     *
     * @param key       key
     * @param retries   retries 重试次数，最少一次
     * @param seconds   seconds 重试时间，单位秒
     * @param exSeconds exSeconds 过期时间，打委秒
     * @return the boolean
     * @throws InterruptedException interrupted exception
     */
    public static @NotNull Boolean lockByExpire(String key, int retries, int seconds, int exSeconds) throws InterruptedException {
        return lockByExpire(key, retries, seconds * 1000, exSeconds, TimeUnit.SECONDS);
    }

    /**
     * Lock by expire
     *
     * @param key     key
     * @param seconds seconds 重试时间，单位秒
     * @return the boolean
     * @throws InterruptedException interrupted exception
     */
    public static @NotNull Boolean lockByExpire(String key, int seconds) throws InterruptedException {
        return lockByExpire(key, 3, seconds * 1000, 30, TimeUnit.SECONDS);
    }

    /**
     * Lock by expire
     *
     * @param key          key
     * @param retries      retries 次数，最少一次
     * @param milliseconds milliseconds 重试时间 毫秒
     * @param ex           ex 过期时间
     * @param unit         unit 过期时间单位
     * @return the boolean
     * @throws InterruptedException interrupted exception
     */
    @SuppressWarnings("unchecked")
    public static @NotNull Boolean lockByExpire(String key, int retries, int milliseconds, int ex,
                                                TimeUnit unit) throws InterruptedException {
        if (redisTemplate == null) {
            redisTemplate = EarlySpringContext.getInstance(RedisTemplate.class);
        }
        Boolean rs = false;
        retries = retries <= 0 ? 1 : retries;
        // 循环 retries 次去尝试获取锁
        for (int i = 0; i < retries; i++) {
            long expire = System.currentTimeMillis() + milliseconds;
            while (System.currentTimeMillis() < expire) {
                rs = redisTemplate.opsForValue().setIfAbsent(key, 1, ex, unit);
                if (rs != null && rs) {
                    break;
                }
                //noinspection BusyWait
                Thread.sleep(100L);
            }
            if (rs != null && rs) {
                return true;
            }
        }
        throw new CacheLockException(StringFormat.format("尝试 {} 次获取分布式锁失败, 耗时: [{} 秒]  ",
                                                        retries, retries * milliseconds / 1000));
    }

    /**
     * Unlock by expire
     *
     * @param key key
     */
    public static void unlockByExpire(String key) {
        redisTemplate.delete(key);
    }

}
