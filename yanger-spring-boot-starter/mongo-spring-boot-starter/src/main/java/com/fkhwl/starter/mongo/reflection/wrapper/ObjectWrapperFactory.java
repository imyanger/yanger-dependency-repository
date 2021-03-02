package com.fkhwl.starter.mongo.reflection.wrapper;

import com.fkhwl.starter.mongo.reflection.MetaObject;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 11:54
 * @since 1.0.0
 */

/**
 * @Description
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
