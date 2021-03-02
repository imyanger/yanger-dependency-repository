package com.fkhwl.starter.mongo.reflection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 11:54
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class DefaultReflectorFactory implements ReflectorFactory {
    /** Reflector map */
    private final ConcurrentMap<Class<?>, Reflector> reflectorMap = new ConcurrentHashMap<>();
    /** Class cache enabled */
    private boolean classCacheEnabled = true;

    /**
     * Default reflector factory
     */
    public DefaultReflectorFactory() {
    }

    /**
     * Is class cache enabled boolean
     *
     * @return the boolean
     */
    @Override
    public boolean isClassCacheEnabled() {
        return this.classCacheEnabled;
    }

    /**
     * Sets class cache enabled *
     *
     * @param classCacheEnabled class cache enabled
     */
    @Override
    public void setClassCacheEnabled(boolean classCacheEnabled) {
        this.classCacheEnabled = classCacheEnabled;
    }

    /**
     * Find for class reflector
     *
     * @param type type
     * @return the reflector
     */
    @Override
    public Reflector findForClass(Class<?> type) {
        if (this.classCacheEnabled) {
            // synchronized (type) removed see issue #461
            return this.reflectorMap.computeIfAbsent(type, Reflector::new);
        } else {
            return new Reflector(type);
        }
    }

}
