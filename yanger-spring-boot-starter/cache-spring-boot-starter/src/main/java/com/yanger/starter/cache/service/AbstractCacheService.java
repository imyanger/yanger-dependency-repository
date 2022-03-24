package com.yanger.starter.cache.service;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheResult;
import com.alicp.jetcache.CacheResultCode;
import com.yanger.starter.cache.exception.CacheNotSupportException;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**

 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@SuppressWarnings("checkstyle:MethodLimit")
public abstract class AbstractCacheService implements CacheService {

    /** Cache */
    @Getter
    protected final Cache<String, Object> cache;

    /** NOT_SUPPORT_MESSAGE */
    private static final String NOT_SUPPORT_MESSAGE = "不支持的操作, 请使用 RedisCacheService";

    /**
     * Abstract cache service
     * @param cache cache
     */
    @Contract(pure = true)
    public AbstractCacheService(Cache<String, Object> cache) {
        this.cache = cache;
    }

    /**
     * 指定缓存失效时间
     * @param key  键
     * @param time 时间(秒)
     * @return boolean boolean
     */
    @Override
    public Boolean expire(String key, Long time) {
        Object value = this.cache.get(key);
        if (value == null) {
            return false;
        }

        this.cache.put(key, value, time, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 根据key 获取过期时间 (本地缓存不能获取过期时间)
     * @param key 键 不能为 null
     * @return 时间(秒) 返回0代表为永久有效;如果该key已经过期,将返回"-2";
     */
    @Override
    public Long getExpire(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    @Override
    public Boolean exists(String key) {
        return this.cache.get(key) != null;
    }

    /**
     * 普通缓存放入并设置时间
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public Boolean set(String key, Object value, Long time) {
        this.cache.put(key, value, time, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 普通缓存放入
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public Boolean set(String key, Object value) {
        this.cache.put(key, value);
        return true;
    }

    /**
     * 普通缓存放入并设置时间
     * @param key      键
     * @param value    值
     * @param time     时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @param consumer consumer
     * @return true成功 false 失败
     */
    @Override
    public Boolean set(String key, Object value, Long time, Consumer<String> consumer) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * 普通缓存放入
     * @param key      键
     * @param value    值
     * @param consumer consumer
     * @return true成功 false失败
     */
    @Override
    public Boolean set(String key, Object value, Consumer<String> consumer) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * 递增
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long long
     */
    @Override
    public Long incr(String key, Long delta) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * 递减
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long long
     */
    @Override
    public Long decr(String key, Long delta) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * 删除缓存
     * @param keys keys
     */
    @Override
    public void del(@NotNull String... keys) {
        Set<String> keySet = new HashSet<>();
        Collections.addAll(keySet, keys);
        this.cache.removeAll(keySet);
    }

    /**
     * 删除缓存
     * @param key key
     * @return the boolean
     */
    @Override
    public Boolean del(String key) {
        return this.cache.remove(key);
    }

    /**
     * 获取缓存
     * @param <T>   parameter
     * @param key   redis的key
     * @param clazz value的class类型
     * @return value的实际对象 t
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        return (T) this.cache.get(key);
    }

    /**
     * 获取泛型
     * @param key 键
     * @return 值 object
     */
    @Override
    public Object get(String key) {
        return this.cache.get(key);
    }

    /**
     * Sets if absent
     * @param key   key
     * @param value value
     * @return the if absent
     */
    @Override
    public Boolean setIfAbsent(String key, String value) {
        return this.cache.putIfAbsent(key, value);
    }

    /**
     * Sets if absent
     * @param key        key
     * @param value      value
     * @param expireTime expire time
     * @return the if absent
     */
    @Override
    public Boolean setIfAbsent(String key, String value, Long expireTime) {
        CacheResult result = this.cache.PUT_IF_ABSENT(key, value, expireTime, TimeUnit.SECONDS);
        return result.getResultCode() == CacheResultCode.SUCCESS;
    }

    /**
     * Hset
     * @param key   key
     * @param field field
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean hset(String key, String field, Object value) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Hget
     * @param key   key
     * @param field field
     * @return the object
     */
    @Override
    public Object hget(String key, String field) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Hget
     * @param <T>   parameter
     * @param key   key
     * @param field field
     * @param clazz clazz
     * @return the t
     */
    @Override
    public <T> T hget(String key, String field, Class<T> clazz) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Hlen
     * @param key key
     * @return the long
     */
    @Override
    public Long hlen(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Hkeys
     * @param key key
     * @return the set
     */
    @Override
    public Set<Object> hkeys(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Hvals
     * @param key key
     * @return the list
     */
    @Override
    public List<Object> hvals(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Hvals
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the list
     */
    @Override
    public <T> List<T> hvals(String key, Class<T> clazz) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Hexists
     * @param key   key
     * @param field field
     * @return the boolean
     */
    @Override
    public Boolean hexists(String key, String field) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Hdel
     * @param key   key
     * @param filed filed
     * @return the long
     */
    @Override
    public Long hdel(String key, String... filed) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Lpop
     * @param key key
     * @return the object
     */
    @Override
    public Object lpop(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Llen
     * @param key key
     * @return the long
     */
    @Override
    public Long llen(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Lpush
     * @param key   key
     * @param value value
     * @return the long
     */
    @Override
    public Boolean lpush(String key, Object value) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Rpush
     * @param key   key
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean rpush(@NotNull String key, @NotNull Object value) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Lpop
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    @Override
    public <T> T lpop(String key, Class<T> clazz) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Rpop
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    @Override
    public <T> T rpop(String key, Class<T> clazz) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Rpop
     * @param key key
     * @return the object
     */
    @Override
    public Object rpop(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Sadd
     * @param key   key
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean sadd(String key, Object... value) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Scard
     * @param key key
     * @return the long
     */
    @Override
    public Long scard(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Srem
     * @param key   key
     * @param value value
     * @return the long
     */
    @Override
    public Long srem(String key, Object... value) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Spop
     * @param key key
     * @return the object
     */
    @Override
    public Object spop(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Spop
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    @Override
    public <T> T spop(String key, Class<T> clazz) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Smembers
     * @param key key
     * @return the set
     */
    @Override
    public Set<Object> smembers(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Smembers
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the set
     */
    @Override
    public <T> Set<T> smembers(String key, Class<T> clazz) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Zadd
     * @param key   key
     * @param score score
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean zadd(String key, Double score, Object value) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Zcard
     * @param key key
     * @return the long
     */
    @Override
    public Long zcard(String key) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Zscore
     * @param key   key
     * @param value value
     * @return the double
     */
    @Override
    public Double zscore(String key, Object value) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

    /**
     * Zrem
     * @param key   key
     * @param value value
     * @return the long
     */
    @Override
    public Long zrem(String key, Object... value) {
        throw new CacheNotSupportException(NOT_SUPPORT_MESSAGE);
    }

}
