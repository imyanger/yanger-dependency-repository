package com.yanger.starter.mongo.reflection.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface Invoker {

    /**
     * Invoke object
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
