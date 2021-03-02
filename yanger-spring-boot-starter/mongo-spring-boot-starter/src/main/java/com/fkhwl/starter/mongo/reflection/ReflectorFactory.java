package com.fkhwl.starter.mongo.reflection;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 11:56
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface ReflectorFactory {

    /**
     * Is class cache enabled boolean
     *
     * @return the boolean
     */
    boolean isClassCacheEnabled();

    /**
     * Sets class cache enabled *
     *
     * @param classCacheEnabled class cache enabled
     */
    void setClassCacheEnabled(boolean classCacheEnabled);

    /**
     * Find for class reflector
     *
     * @param type type
     * @return the reflector
     */
    Reflector findForClass(Class<?> type);
}
