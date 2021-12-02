package com.yanger.starter.mongo.reflection.invoker;

import com.yanger.starter.mongo.reflection.Reflector;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class SetFieldInvoker implements Invoker {
    /** Field */
    private final Field field;

    /**
     * Set field invoker
     *
     * @param field field
     */
    @Contract(pure = true)
    public SetFieldInvoker(Field field) {
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
    public Object invoke(Object target, Object @NotNull [] args) throws IllegalAccessException {
        try {
            this.field.set(target, args[0]);
        } catch (IllegalAccessException e) {
            if (Reflector.canControlMemberAccessible()) {
                this.field.setAccessible(true);
                this.field.set(target, args[0]);
            } else {
                throw e;
            }
        }
        return null;
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
