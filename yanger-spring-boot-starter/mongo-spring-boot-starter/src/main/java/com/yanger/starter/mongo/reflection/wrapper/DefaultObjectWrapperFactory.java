package com.yanger.starter.mongo.reflection.wrapper;

import com.yanger.starter.mongo.reflection.MetaObject;
import com.yanger.starter.mongo.reflection.ReflectionException;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {

    /**
     * Has wrapper for boolean
     * @param object object
     * @return the boolean
     */
    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    /**
     * Gets wrapper for
     * @param metaObject meta object
     * @param object     object
     * @return the wrapper for
     */
    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new ReflectionException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }

}
