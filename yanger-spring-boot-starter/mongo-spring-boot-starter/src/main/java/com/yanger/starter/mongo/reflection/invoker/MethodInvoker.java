package com.yanger.starter.mongo.reflection.invoker;

import com.yanger.starter.mongo.reflection.Reflector;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class MethodInvoker implements Invoker {

    /** Type */
    private final Class<?> type;
    /** Method */
    private final Method method;

    /**
     * Method invoker
     *
     * @param method method
     */
    public MethodInvoker(@NotNull Method method) {
        this.method = method;

        if (method.getParameterTypes().length == 1) {
            this.type = method.getParameterTypes()[0];
        } else {
            this.type = method.getReturnType();
        }
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
        try {
            return this.method.invoke(target, args);
        } catch (IllegalAccessException e) {
            if (Reflector.canControlMemberAccessible()) {
                this.method.setAccessible(true);
                return this.method.invoke(target, args);
            } else {
                throw e;
            }
        }
    }

    /**
     * Gets type *
     *
     * @return the type
     */
    @Override
    public Class<?> getType() {
        return this.type;
    }
}
