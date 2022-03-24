package com.yanger.starter.mongo.reflection;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface ReflectorFactory {

    /**
     * Is class cache enabled boolean
     * @return the boolean
     */
    boolean isClassCacheEnabled();

    /**
     * Sets class cache enabled
     * @param classCacheEnabled class cache enabled
     */
    void setClassCacheEnabled(boolean classCacheEnabled);

    /**
     * Find for class reflector
     * @param type type
     * @return the reflector
     */
    Reflector findForClass(Class<?> type);
}
