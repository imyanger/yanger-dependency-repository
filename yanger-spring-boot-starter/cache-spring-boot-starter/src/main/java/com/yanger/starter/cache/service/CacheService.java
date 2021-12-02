package com.yanger.starter.cache.service;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**

 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@SuppressWarnings("checkstyle:MethodLimit")
public interface CacheService {

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return boolean boolean
     */
    Boolean expire(String key, Long time);

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为 null
     * @return 时间(秒) 返回0代表为永久有效;如果该key已经过期,将返回"-2";
     */
    Long getExpire(String key);

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    Boolean exists(String key);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    Boolean set(String key, Object value, Long time);

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
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
     */
    Boolean set(String key, Object value, Long time, Consumer<String> consumer);

    /**
     * 普通缓存放入
     *
     * @param key      键
     * @param value    值
     * @param consumer consumer
     * @return true成功 false失败
     */
    Boolean set(String key, Object value, Consumer<String> consumer);

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long long
     */
    Long incr(String key, Long delta);

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long long
     */
    Long decr(String key, Long delta);

    /**
     * 删除缓存
     *
     * @param keys keys
     */
    void del(@NotNull String... keys);

    /**
     * 删除缓存
     *
     * @param key key
     * @return the boolean
     */
    Boolean del(String key);

    /**
     * 获取缓存
     *
     * @param <T>   the type parameter
     * @param key   redis的key
     * @param clazz value的class类型
     * @return value的实际对象 t
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取泛型
     *
     * @param key 键
     * @return 值 object
     */
    Object get(String key);

    /**
     * Sets if absent *
     *
     * @param key   key
     * @param value value
     * @return the if absent
     */
    Boolean setIfAbsent(String key, String value);

    /**
     * Sets if absent *
     *
     * @param key        key
     * @param value      value
     * @param expireTime expire time
     * @return the if absent
     */
    Boolean setIfAbsent(String key, String value, Long expireTime);

    /**
     * Hset
     *
     * @param key   key
     * @param field field
     * @param value value
     * @return the boolean
     */
    Boolean hset(String key, String field, Object value);

    /**
     * Hget
     *
     * @param key   key
     * @param field field
     * @return the object
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
     */
    <T> T hget(String key, String field, Class<T> clazz);

    /**
     * Hlen
     *
     * @param key key
     * @return the long
     */
    Long hlen(String key);

    /**
     * Hkeys
     *
     * @param key key
     * @return the set
     */
    Set<Object> hkeys(String key);

    /**
     * Hvals
     *
     * @param key key
     * @return the set
     */
    List<Object> hvals(String key);

    /**
     * Hvals
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the list
     */
    <T> List<T> hvals(String key, Class<T> clazz);

    /**
     * Hexists
     *
     * @param key   key
     * @param field field
     * @return the boolean
     */
    Boolean hexists(String key, String field);

    /**
     * Hdel
     *
     * @param key   key
     * @param filed filed
     * @return the long
     */
    Long hdel(String key, String... filed);

    /**
     * Lpop
     *
     * @param key key
     * @return the object
     */
    Object lpop(String key);

    /**
     * Lpop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    <T> T lpop(String key, Class<T> clazz);

    /**
     * Llen
     *
     * @param key key
     * @return the long
     */
    Long llen(String key);

    /**
     * Lpush
     *
     * @param key   key
     * @param value value
     * @return the boolean
     */
    Boolean lpush(String key, Object value);

    /**
     * Rpush
     *
     * @param key   key
     * @param value value
     * @return the boolean
     */
    Boolean rpush(@NotNull String key, @NotNull Object value);

    /**
     * Rpop
     *
     * @param key key
     * @return the object
     */
    Object rpop(String key);

    /**
     * Rpop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    <T> T rpop(String key, Class<T> clazz);

    /**
     * Sadd
     *
     * @param key   key
     * @param value value
     * @return the boolean
     */
    Boolean sadd(String key, Object... value);

    /**
     * Scard
     *
     * @param key key
     * @return the long
     */
    Long scard(String key);

    /**
     * Srem
     *
     * @param key   key
     * @param value value
     * @return the long
     */
    Long srem(String key, Object... value);

    /**
     * Spop
     *
     * @param key key
     * @return the object
     */
    Object spop(String key);

    /**
     * Spop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    <T> T spop(String key, Class<T> clazz);

    /**
     * Smembers
     *
     * @param key key
     * @return the set
     */
    Set<Object> smembers(String key);

    /**
     * Smembers
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the set
     */
    <T> Set<T> smembers(String key, Class<T> clazz);

    /**
     * zset  @param key key
     *
     * @param key   key
     * @param score score
     * @param value value
     * @return the boolean  更新分数时返回false 插入成功返回true
     */
    Boolean zadd(String key, Double score, Object value);

    /**
     * Zcard
     *
     * @param key key
     * @return the long
     */
    Long zcard(String key);

    /**
     * Zscore
     *
     * @param key   key
     * @param value value
     * @return the double
     */
    Double zscore(String key, Object value);

    /**
     * Zrem
     *
     * @param key   key
     * @param value value
     * @return the long
     */
    Long zrem(String key, Object... value);

}
