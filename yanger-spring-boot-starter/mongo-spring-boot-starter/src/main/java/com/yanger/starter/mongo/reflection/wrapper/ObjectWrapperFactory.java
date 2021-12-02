package com.yanger.starter.mongo.reflection.wrapper;

import com.yanger.starter.mongo.reflection.MetaObject;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface ObjectWrapperFactory {

    /**
     * Has wrapper for boolean
     *
     * @param object object
     * @return the boolean
     */
    boolean hasWrapperFor(Object object);

    /**
     * Gets wrapper for *
     *
     * @param metaObject meta object
     * @param object     object
     * @return the wrapper for
     */
    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);

}
