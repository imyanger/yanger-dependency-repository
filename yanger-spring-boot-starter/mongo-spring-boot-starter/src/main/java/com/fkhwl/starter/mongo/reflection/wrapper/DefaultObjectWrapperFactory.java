package com.fkhwl.starter.mongo.reflection.wrapper;

import com.fkhwl.starter.mongo.reflection.MetaObject;
import com.fkhwl.starter.mongo.reflection.ReflectionException;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 11:52
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory {

    /**
     * Has wrapper for boolean
     *
     * @param object object
     * @return the boolean
     */
    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    /**
     * Gets wrapper for *
     *
     * @param metaObject meta object
     * @param object     object
     * @return the wrapper for
     */
    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new ReflectionException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }

}
