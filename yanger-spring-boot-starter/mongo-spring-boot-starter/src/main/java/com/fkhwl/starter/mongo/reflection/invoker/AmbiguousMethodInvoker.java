package com.fkhwl.starter.mongo.reflection.invoker;

import com.fkhwl.starter.mongo.reflection.ReflectionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
