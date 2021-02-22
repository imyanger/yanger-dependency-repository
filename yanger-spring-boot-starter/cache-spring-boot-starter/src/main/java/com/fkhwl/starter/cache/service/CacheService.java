package com.fkhwl.starter.cache.service;

import com.fkhwl.starter.core.function.CheckedConsumer;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.11 16:31
 * @since 1.0.0
 */
@SuppressWarnings("checkstyle:MethodLimit")
public interface CacheService {

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return boolean boolean
     * @since 1.0.0
     */
    Boolean expire(String key, Long time);

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为 null
     * @return 时间(秒) 返回0代表为永久有效;如果该key已经过期,将返回"-2";
     * @since 1.0.0
     */
    Long getExpire(String key);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     * @since 1.0.0
     */
    Boolean exists(String key);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     * @since 1.0.0
     */
    Boolean set(String key, Object value, Long time);

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     * @since 1.0.0
     */
    Boolean set(String key, Object value);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @param consumer consumer
     * @return true成功 false 失败
     * @since 1.0.0
     */
    Boolean set(String key, Object value, Long time, CheckedConsumer<String> consumer);

    /**
     * 普通缓存放入
     *
     * @param key      键
     * @param value    值
     * @param consumer consumer
     * @return true成功 false失败
     * @since 1.0.0
     */
    Boolean set(String key, Object value, CheckedConsumer<String> consumer);

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long long
     * @since 1.0.0
     */
    Long incr(String key, Long delta);

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long long
     * @since 1.0.0
     */
    Long decr(String key, Long delta);

    /**
     * 删除缓存
     *
     * @param keys keys
     * @since 1.0.0
     */
    void del(@NotNull String... keys);

    /**
     * 删除缓存
     *
     * @param key key
     * @return the boolean
     * @since 1.0.0
     */
    Boolean del(String key);

    /**
     * 获取缓存
     *
     * @param <T>   the type parameter
     * @param key   redis的key
     * @param clazz value的class类型
     * @return value的实际对象 t
     * @since 1.0.0
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取泛型
     *
     * @param key 键
     * @return 值 object
     * @since 1.0.0
     */
    Object get(String key);

    /**
     * Sets if absent *
     *
     * @param key   key
     * @param value value
     * @return the if absent
     * @since 1.0.0
     */
    Boolean setIfAbsent(String key, String value);

    /**
     * Sets if absent *
     *
     * @param key        key
     * @param value      value
     * @param expireTime expire time
     * @return the if absent
     * @since 1.6.0
     */
    Boolean setIfAbsent(String key, String value, Long expireTime);

    /**
     * Hset
     *
     * @param key   key
     * @param field field
     * @param value value
     * @return the boolean
     * @since 1.6.0
     */
    Boolean hset(String key, String field, Object value);

    /**
     * Hget
     *
     * @param key   key
     * @param field field
     * @return the object
     * @since 1.6.0
     */
    Object hget(String key, String field);

    /**
     * Hget
     *
     * @param <T>   parameter
     * @param key   key
     * @param field field
     * @param clazz clazz
     * @return the t
     * @since 1.6.0
     */
    <T> T hget(String key, String field, Class<T> clazz);

    /**
     * Hlen
     *
     * @param key key
     * @return the long
     * @since 1.6.0
     */
    Long hlen(String key);

    /**
     * Hkeys
     *
     * @param key key
     * @return the set
     * @since 1.6.0
     */
    Set<Object> hkeys(String key);

    /**
     * Hvals
     *
     * @param key key
     * @return the set
     * @since 1.6.0
     */
    List<Object> hvals(String key);

    /**
     * Hvals
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the list
     * @since 1.6.0
     */
    <T> List<T> hvals(String key, Class<T> clazz);

    /**
     * Hexists
     *
     * @param key   key
     * @param field field
     * @return the boolean
     * @since 1.6.0
     */
    Boolean hexists(String key, String field);

    /**
     * Hdel
     *
     * @param key   key
     * @param filed filed
     * @return the long
     * @since 1.6.0
     */
    Long hdel(String key, String... filed);

    /**
     * Lpop
     *
     * @param key key
     * @return the object
     * @since 1.6.0
     */
    Object lpop(String key);

    /**
     * Lpop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     * @since 1.6.0
     */
    <T> T lpop(String key, Class<T> clazz);

    /**
     * Llen
     *
     * @param key key
     * @return the long
     * @since 1.6.0
     */
    Long llen(String key);

    /**
     * Lpush
     *
     * @param key   key
     * @param value value
     * @return the boolean
     * @since 1.6.0
     */
    Boolean lpush(String key, Object value);

    /**
     * Rpush
     *
     * @param key   key
     * @param value value
     * @return the boolean
     * @since 1.6.0
     */
    Boolean rpush(@NotNull String key, @NotNull Object value);

    /**
     * Rpop
     *
     * @param key key
     * @return the object
     * @since 1.6.0
     */
    Object rpop(String key);

    /**
     * Rpop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     * @since 1.6.0
     */
    <T> T rpop(String key, Class<T> clazz);

    /**
     * Sadd
     *
     * @param key   key
     * @param value value
     * @return the boolean
     * @since 1.6.0
     */
    Boolean sadd(String key, Object... value);

    /**
     * Scard
     *
     * @param key key
     * @return the long
     * @since 1.6.0
     */
    Long scard(String key);

    /**
     * Srem
     *
     * @param key   key
     * @param value value
     * @return the long
     * @since 1.6.0
     */
    Long srem(String key, Object... value);

    /**
     * Spop
     *
     * @param key key
     * @return the object
     * @since 1.6.0
     */
    Object spop(String key);

    /**
     * Spop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     * @since 1.6.0
     */
    <T> T spop(String key, Class<T> clazz);

    /**
     * Smembers
     *
     * @param key key
     * @return the set
     * @since 1.6.0
     */
    Set<Object> smembers(String key);

    /**
     * Smembers
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the set
     * @since 1.6.0
     */
    <T> Set<T> smembers(String key, Class<T> clazz);

    /**
     * zset  @param key key
     *
     * @param key   key
     * @param score score
     * @param value value
     * @return the boolean  更新分数时返回false 插入成功返回true
     * @since 1.6.0
     */
    Boolean zadd(String key, Double score, Object value);

    /**
     * Zcard
     *
     * @param key key
     * @return the long
     * @since 1.6.0
     */
    Long zcard(String key);

    /**
     * Zscore
     *
     * @param key   key
     * @param value value
     * @return the double
     * @since 1.6.0
     */
    Double zscore(String key, Object value);

    /**
     * Zrem
     *
     * @param key   key
     * @param value value
     * @return the long
     * @since 1.6.0
     */
    Long zrem(String key, Object... value);

}
