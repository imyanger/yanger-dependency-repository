package com.yanger.starter.mongo.reflection.invoker;

import com.yanger.starter.mongo.reflection.Reflector;

import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class GetFieldInvoker implements Invoker {
    /** Field */
    private final Field field;

    /**
     * Get field invoker
     *
     * @param field field
     */
    @Contract(pure = true)
    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    /**
     * Invoke object
     *
     * @param target target
     * @param args   args
     * @return the object
     * @throws IllegalAccessException illegal access exception
     */
    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException {
        try {
            return this.field.get(target);
        } catch (IllegalAccessException e) {
            if (Reflector.canControlMemberAccessible()) {
                this.field.setAccessible(true);
                return this.field.get(target);
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
        return this.field.getType();
    }
}
