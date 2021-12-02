package com.yanger.starter.mongo.reflection.property;

import com.yanger.starter.mongo.reflection.Reflector;

import org.jetbrains.annotations.Contract;

import java.lang.reflect.Field;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public final class PropertyCopier {

    /**
     * Property copier
     */
    @Contract(pure = true)
    private PropertyCopier() {
        // Prevent Instantiation of Static Class
    }

    /**
     * Copy bean properties *
     *
     * @param type            type
     * @param sourceBean      source bean
     * @param destinationBean destination bean
     */
    public static void copyBeanProperties(Class<?> type, Object sourceBean, Object destinationBean) {
        Class<?> parent = type;
        while (parent != null) {
            Field[] fields = parent.getDeclaredFields();
            for (Field field : fields) {
                try {
                    try {
                        field.set(destinationBean, field.get(sourceBean));
                    } catch (IllegalAccessException e) {
                        if (Reflector.canControlMemberAccessible()) {
                            field.setAccessible(true);
                            field.set(destinationBean, field.get(sourceBean));
                        } else {
                            throw e;
                        }
                    }
                } catch (Exception e) {
                    // Nothing useful to do, will only fail on final fields, which will be ignored.
                }
            }
            parent = parent.getSuperclass();
        }
    }

}
