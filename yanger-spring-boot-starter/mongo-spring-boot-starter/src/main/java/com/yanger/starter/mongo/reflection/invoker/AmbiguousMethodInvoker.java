package com.yanger.starter.mongo.reflection.invoker;

import com.yanger.starter.mongo.reflection.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class AmbiguousMethodInvoker extends MethodInvoker {
    /** Exception message */
    private final String exceptionMessage;

    /**
     * Ambiguous method invoker
     *
     * @param method           method
     * @param exceptionMessage exception message
     */
    public AmbiguousMethodInvoker(Method method, String exceptionMessage) {
        super(method);
        this.exceptionMessage = exceptionMessage;
    }

    /**
     * Invoke object
     *
     * @param target target
     * @param args   args
     * @return the object
     * @throws IllegalAccessException    illegal access exception
     * @throws InvocationTargetException invocation target exception
     */
    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        throw new ReflectionException(this.exceptionMessage);
    }
}
