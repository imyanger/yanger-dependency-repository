package com.fkhwl.starter.mongo.reflection;

import com.fkhwl.starter.mongo.reflection.factory.DefaultObjectFactory;
import com.fkhwl.starter.mongo.reflection.factory.ObjectFactory;
import com.fkhwl.starter.mongo.reflection.wrapper.DefaultObjectWrapperFactory;
import com.fkhwl.starter.mongo.reflection.wrapper.ObjectWrapperFactory;

import org.jetbrains.annotations.Contract;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 默认的实体元数据包装对象, 使用 {@link DefaultMetaObject#forObject} 生成 {@link MetaObject}</p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 11:30
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public final class DefaultMetaObject {

    /** DEFAULT_OBJECT_FACTORY */
    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    /** DEFAULT_OBJECT_WRAPPER_FACTORY */
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    /** NULL_META_OBJECT */
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class,
                                                                           DEFAULT_OBJECT_FACTORY,
                                                                           DEFAULT_OBJECT_WRAPPER_FACTORY,
                                                                           new DefaultReflectorFactory());

    /**
     * Default meta object
     */
    @Contract(pure = true)
    private DefaultMetaObject() {
    }

    /**
     * For object meta object
     *
     * @param object object
     * @return the meta object
     */
    @Contract("!null -> new")
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
    }

    /**
     * <p>Company: 成都返空汇网络技术有限公司 </p>
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.3.0
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.04.12 11:30
     */
    private static class NullObject {
    }

}
