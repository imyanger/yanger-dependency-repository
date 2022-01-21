package com.yanger.tools.general.tools;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LRU cache 缓存容器
 * @Author yanger
 * @Date 2020/12/21 10:27
 */
@SuppressWarnings("all")
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = -5167631809472116969L;

    /** 默认加载因子 */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /** 默认最大容量 */
    private static final int DEFAULT_MAX_CAPACITY = 1000;

    /** Lock */
    private final Lock lock = new ReentrantLock();

    /** 最大容量 */
    private volatile int maxCapacity;

    /**
     * 无参构造
     */
    public LRUCache() {
        this(DEFAULT_MAX_CAPACITY);
    }

    /**
     * 自定义最大容量构造
     * @param maxCapacity max capacity
     */
    public LRUCache(int maxCapacity) {
        super(16, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
    }

    /**
     * 是否需要删除最旧的记录
     * @param eldest
     * @return {@link boolean}
     * @Author yanger
     * @Date 2022/01/06 0:29
     */
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return this.size() > this.maxCapacity;
    }

    /**
     * 是否包含指定 key
     * @param key key
     * @return {@link boolean}
     * @Author yanger
     * @Date 2022/01/06 0:32
     */
    @Override
    public boolean containsKey(Object key) {
        this.lock.lock();
        try {
            return super.containsKey(key);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 根据 key 获取 value
     * @param key key
     * @return {@link V}
     * @Author yanger
     * @Date 2022/01/06 0:33
     */
    @Override
    public V get(Object key) {
        this.lock.lock();
        try {
            return super.get(key);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 设置 key-value 缓存
     * @param key key
     * @param value value
     * @return {@link V}
     * @Author yanger
     * @Date 2022/01/06 0:34
     */
    @Override
    public V put(K key, V value) {
        this.lock.lock();
        try {
            return super.put(key, value);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 根据 key 删除缓存
     * @param key key
     * @return {@link V}
     * @Author yanger
     * @Date 2022/01/06 0:34
     */
    @Override
    public V remove(Object key) {
        this.lock.lock();
        try {
            return super.remove(key);
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 获取缓存容器大小
     * @return {@link int}
     * @Author yanger
     * @Date 2022/01/06 0:35
     */
    @Override
    public int size() {
        this.lock.lock();
        try {
            return super.size();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 清空缓存
     * @Author yanger
     * @Date 2022/01/06 0:35
     */
    @Override
    public void clear() {
        this.lock.lock();
        try {
            super.clear();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 获取容器的最大容量
     * @return {@link int} 最大容量
     * @Author yanger
     * @Date 2022/01/06 0:36
     */
    public int getMaxCapacity() {
        return this.maxCapacity;
    }

    /**
     * 设置容器的最大容量
     * @param maxCapacity 最大容量
     * @Author yanger
     * @Date 2022/01/06 0:37
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

}
