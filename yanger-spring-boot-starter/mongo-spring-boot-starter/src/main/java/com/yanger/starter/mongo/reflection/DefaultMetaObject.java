package com.yanger.starter.mongo.reflection;

import com.yanger.starter.mongo.reflection.factory.DefaultObjectFactory;
import com.yanger.starter.mongo.reflection.factory.ObjectFactory;
import com.yanger.starter.mongo.reflection.wrapper.DefaultObjectWrapperFactory;
import com.yanger.starter.mongo.reflection.wrapper.ObjectWrapperFactory;
import org.jetbrains.annotations.Contract;

/**
 * 默认的实体元数据包装对象, 使用 {@link DefaultMetaObject#forObject} 生成 {@link MetaObject}
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
     * @param object object
     * @return the meta object
     */
    @Contract("!null -> new")
    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
    }

    private static class NullObject {
    }

}
