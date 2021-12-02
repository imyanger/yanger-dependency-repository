package com.yanger.starter.cache.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheValueHolder;
import com.yanger.starter.cache.handler.KeyExpirationHander;
import com.yanger.starter.cache.handler.KeyExpirationListenerAdapter;
import com.yanger.starter.cache.service.AbstractCacheService;
import com.yanger.tools.web.exception.AssertUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * redis 缓存实现
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Slf4j
@SuppressWarnings(value = {"checkstyle:MethodLimit", "PMD:MethodLimit"})
public class RedisCacheServiceImpl extends AbstractCacheService {

    /** Redis template */
    @Getter
    private final RedisTemplate<String, Object> redisTemplate;

    /** Key expiration listener adapter */
    private final KeyExpirationListenerAdapter keyExpirationListenerAdapter;

    /**
     * Instantiates a new Redis service.
     *
     * @param cache                        cache
     * @param redisTemplate                the redis template
     * @param keyExpirationListenerAdapter key expiration listener adapter
     */
    @Contract(pure = true)
    public RedisCacheServiceImpl(Cache<String, Object> cache,
                                 RedisTemplate<String, Object> redisTemplate,
                                 KeyExpirationListenerAdapter keyExpirationListenerAdapter) {
        super(cache);
        this.redisTemplate = redisTemplate;
        this.keyExpirationListenerAdapter = keyExpirationListenerAdapter;
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为 null
     * @return 时间(秒) 返回0代表为永久有效;如果该key已经过期,将返回"-2";
     */
    @Override
    public Long getExpire(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public Boolean set(String key, Object value, Long time) {
        try {
            if (time > 0) {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                this.set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("【redis: 普通缓存放入并设置时间-异常】", e);
            return false;
        }

    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    @Override
    public Boolean set(String key, Object value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("【redis: 普通缓存放入-异常】", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @param consumer consumer
     * @return true成功 false 失败
     */
    @Override
    public Boolean set(String key, Object value, Long time, Consumer<String> consumer) {
        this.addHander(key, consumer);
        return this.set(key, value, time);
    }

    /**
     * 普通缓存放入
     *
     * @param key      键
     * @param value    值
     * @param consumer consumer
     * @return true成功 false失败
     */
    @Override
    public Boolean set(String key, Object value, Consumer<String> consumer) {
        this.addHander(key, consumer);
        return this.set(key, value);
    }

    /**
     * Add hander
     *
     * @param key      key
     * @param consumer consumer
     */
    private void addHander(String key, Consumer<String> consumer) {
        this.keyExpirationListenerAdapter.addHander(key, new KeyExpirationHander() {

            /**
             * Hander
             *
             */
            @Override
            public void hander() {
                try {
                    consumer.accept(key);
                } catch (Throwable throwable) {
                    log.error(key + " 过期逻辑执行失败.", throwable);
                }
            }
        });
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return long long
     */
    @Override
    public Long incr(String key, Long delta) {
        AssertUtils.isTrue(delta > 0, "递增因子必须大于 0");
        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long long
     */
    @Override
    public Long decr(String key, Long delta) {
        AssertUtils.isTrue(delta > 0, "递减因子必须大于 0");
        return this.redisTemplate.opsForValue().increment(key, -delta);
    }

    /**
     * 获取缓存
     *
     * @param <T>   the type parameter
     * @param key   redis的key
     * @param clazz value的class类型
     * @return value的实际对象 t
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object obj = key == null ? null : this.redisTemplate.opsForValue().get(key);

        if (obj == null) {
            return null;
        }

        if (obj instanceof CacheValueHolder) {
            return ((CacheValueHolder<T>) obj).getValue();
        }

        return (T) obj;
    }

    /**
     * 获取泛型
     *
     * @param key 键
     * @return 值 object
     */
    @Override
    public Object get(String key) {
        return this.get(key, Object.class);
    }

    /**
     * Hset
     *
     * @param key   key
     * @param field field
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean hset(@NotNull String key, @NotNull String field, @NotNull Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            log.error("写入缓存异常，key：{}，field:{}", key, field);
            return false;
        }

    }

    /**
     * Hget
     *
     * @param key   key
     * @param field field
     * @return the object
     */
    @Override
    public Object hget(@NotNull String key, @NotNull String field) {
        return this.hget(key, field, Object.class);
    }

    /**
     * Hget
     *
     * @param <T>   parameter
     * @param key   key
     * @param field field
     * @param clazz clazz
     * @return the t
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T hget(@NotNull String key, @NotNull String field, @NotNull Class<T> clazz) {
        Object obj = this.redisTemplate.opsForHash().get(key, field);
        if (obj == null) {
            return null;
        }
        if (obj instanceof CacheValueHolder) {
            return (T) ((CacheValueHolder<?>) obj).getValue();
        }

        return (T) obj;
    }

    /**
     * Hlen
     *
     * @param key key
     * @return the long
     */
    @Override
    public Long hlen(@NotNull String key) {
        return this.redisTemplate.opsForHash().size(key);
    }

    /**
     * Hkeys
     *
     * @param key key
     * @return the set
     */
    @Override
    public Set<Object> hkeys(@NotNull String key) {
        return this.redisTemplate.opsForHash().keys(key);
    }

    /**
     * Hvals
     *
     * @param key key
     * @return the list
     */
    @Override
    public List<Object> hvals(@NotNull String key) {
        return this.hvals(key, Object.class);
    }

    /**
     * Hvals
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the list
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> hvals(@NotNull String key, @NotNull Class<T> clazz) {
        List<Object> objectList = this.redisTemplate.opsForHash().values(key);

        if (objectList.size() == 0) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        for (Object o : objectList) {
            if (o instanceof CacheValueHolder) {
                list.add(((CacheValueHolder<T>) o).getValue());
            } else {
                list.add((T) o);
            }
        }
        return list;
    }

    /**
     * Hexists
     *
     * @param key   key
     * @param field field
     * @return the boolean
     */
    @Override
    public Boolean hexists(@NotNull String key, @NotNull String field) {
        return this.redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * Hdel
     *
     * @param key   key
     * @param filed filed
     * @return the long
     */
    @Override
    public Long hdel(@NotNull String key, @NotNull String... filed) {
        return this.redisTemplate.opsForHash().delete(key, filed);
    }

    /**
     * Lpush
     *
     * @param key   key
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean lpush(@NotNull String key, @NotNull Object value) {
        try {
            this.redisTemplate.opsForList().leftPush(key, value);
        } catch (Exception e) {
            log.error("写入缓存异常，key：{}。", key);
            return false;
        }
        return true;
    }

    /**
     * Rpush
     *
     * @param key   key
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean rpush(@NotNull String key, @NotNull Object value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            log.error("写入缓存异常，key：{}。", key);
            return false;
        }
        return true;
    }

    /**
     * Llen
     *
     * @param key key
     * @return the long
     */
    @Override
    public Long llen(@NotNull String key) {
        return this.redisTemplate.opsForList().size(key);
    }

    /**
     * Lpop
     *
     * @param key key
     * @return the object
     */
    @Override
    public Object lpop(@NotNull String key) {
        return this.lpop(key, Object.class);
    }

    /**
     * Lpop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T lpop(@NotNull String key, @NotNull Class<T> clazz) {
        Object obj = this.redisTemplate.opsForList().leftPop(key);
        if (obj == null) {
            return null;
        }

        if (obj instanceof CacheValueHolder) {
            return (T) ((CacheValueHolder<?>) obj).getValue();
        }

        return (T) obj;
    }

    /**
     * Rpop
     *
     * @param key key
     * @return the object
     */
    @Override
    public Object rpop(@NotNull String key) {
        return this.rpop(key, Object.class);
    }

    /**
     * Rpop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T rpop(@NotNull String key, @NotNull Class<T> clazz) {
        Object obj = this.redisTemplate.opsForList().rightPop(key);
        if (obj == null) {
            return null;
        }

        if (obj instanceof CacheValueHolder) {
            return (T) ((CacheValueHolder<?>) obj).getValue();
        }

        return (T) obj;
    }

    /**
     * Sadd
     *
     * @param key   key
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean sadd(@NotNull String key, @NotNull Object... value) {
        try {
            this.redisTemplate.opsForSet().add(key, value);
        } catch (Exception e) {
            log.error("写入缓存异常，key：{}。", key);
            return false;
        }
        return true;
    }

    /**
     * Scard
     *
     * @param key key
     * @return the long
     */
    @Override
    public Long scard(@NotNull String key) {
        return this.redisTemplate.opsForSet().size(key);
    }

    /**
     * Srem
     *
     * @param key   key
     * @param value value
     * @return the long
     */
    @Override
    public Long srem(@NotNull String key, Object... value) {
        return this.redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * Spop
     *
     * @param key key
     * @return the object
     */
    @Override
    public Object spop(@NotNull String key) {
        return this.spop(key, Object.class);
    }

    /**
     * Spop
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the t
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T spop(@NotNull String key, Class<T> clazz) {
        Object obj = this.redisTemplate.opsForSet().pop(key);
        if (obj == null) {
            return null;
        }

        if (obj instanceof CacheValueHolder) {
            return (T) ((CacheValueHolder<?>) obj).getValue();
        }

        return (T) obj;
    }

    /**
     * Smembers
     *
     * @param key key
     * @return the set
     */
    @Override
    public Set<Object> smembers(String key) {
        return this.smembers(key, Object.class);
    }

    /**
     * Smembers
     *
     * @param <T>   parameter
     * @param key   key
     * @param clazz clazz
     * @return the set
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> smembers(String key, Class<T> clazz) {
        Set<Object> objects = this.smembers(key);
        if (objects.size() == 0) {
            return new HashSet<>();
        }
        Set<T> sets = new HashSet<>();

        for (Object o : objects) {
            if (o instanceof CacheValueHolder) {
                sets.add(((CacheValueHolder<T>) o).getValue());
            } else {
                sets.add((T) o);
            }
        }
        return sets;
    }

    /**
     * Zadd
     *
     * @param key   key
     * @param score score
     * @param value value
     * @return the boolean
     */
    @Override
    public Boolean zadd(String key, Double score, Object value) {
        return this.redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * Zcard
     *
     * @param key key
     * @return the long
     */
    @Override
    public Long zcard(String key) {
        return this.redisTemplate.opsForZSet().size(key);
    }

    /**
     * Zscore
     *
     * @param key   key
     * @param value value
     * @return the double
     */
    @Override
    public Double zscore(String key, Object value) {
        return this.redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * Zrem
     *
     * @param key   key
     * @param value value
     * @return the long
     */
    @Override
    public Long zrem(String key, Object... value) {
        return this.redisTemplate.opsForZSet().remove(key, value);
    }
}
