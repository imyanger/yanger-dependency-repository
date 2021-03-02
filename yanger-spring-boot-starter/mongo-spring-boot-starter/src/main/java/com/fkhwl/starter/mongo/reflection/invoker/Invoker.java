package com.fkhwl.starter.mongo.reflection.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 11:46
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface Invoker {
    /**
     * Invoke object
     *
     * @param target target
     * @param args   args
     * @return the object
     * @throws IllegalAccessException    illegal access exception
     * @throws InvocationTargetException invocation target exception
     */
    Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;

    /**
     * Gets type *
     *
     * @return the type
     */
    Class<?> getType();
}
