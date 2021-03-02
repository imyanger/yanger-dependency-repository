package com.fkhwl.starter.mongo.reflection.wrapper;

import com.fkhwl.starter.mongo.reflection.DefaultMetaObject;
import com.fkhwl.starter.mongo.reflection.MetaClass;
import com.fkhwl.starter.mongo.reflection.MetaObject;
import com.fkhwl.starter.mongo.reflection.ReflectionException;
import com.fkhwl.starter.mongo.reflection.factory.ObjectFactory;
import com.fkhwl.starter.mongo.reflection.invoker.Invoker;
import com.fkhwl.starter.mongo.reflection.property.PropertyTokenizer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.hutool.core.exceptions.ExceptionUtil;

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
public class BeanWrapper extends BaseWrapper {

    /** Object */
    private final Object object;
    /** Meta class */
    private final MetaClass metaClass;

    /**
     * Bean wrapper
     *
     * @param metaObject meta object
     * @param object     object
     */
    public BeanWrapper(MetaObject metaObject, @NotNull Object object) {
        super(metaObject);
        this.object = object;
        this.metaClass = MetaClass.forClass(object.getClass(), metaObject.getReflectorFactory());
    }

    /**
     * Get object
     *
     * @param prop prop
     * @return the object
     */
    @Override
    public Object get(@NotNull PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            Object collection = this.resolveCollection(prop, this.object);
            return this.getCollectionValue(prop, collection);
        } else {
            return this.getBeanProperty(prop, this.object);
        }
    }

    /**
     * Set *
     *
     * @param prop  prop
     * @param value value
     */
    @Override
    public void set(@NotNull PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = this.resolveCollection(prop, this.object);
            this.setCollectionValue(prop, collection, value);
        } else {
            this.setBeanProperty(prop, this.object, value);
        }
    }

    /**
     * Find property string
     *
     * @param name                name
     * @param useCamelCaseMapping use camel case mapping
     * @return the string
     */
    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return this.metaClass.findProperty(name, useCamelCaseMapping);
    }

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     */
    @Override
    public String[] getGetterNames() {
        return this.metaClass.getGetterNames();
    }

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     */
    @Override
    public String[] getSetterNames() {
        return this.metaClass.getSetterNames();
    }

    /**
     * Gets setter type *
     *
     * @param name name
     * @return the setter type
     */
    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                return this.metaClass.getSetterType(name);
            } else {
                return metaValue.getSetterType(prop.getChildren());
            }
        } else {
            return this.metaClass.getSetterType(name);
        }
    }

    /**
     * Gets getter type *
     *
     * @param name name
     * @return the getter type
     */
    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                return this.metaClass.getGetterType(name);
            } else {
                return metaValue.getGetterType(prop.getChildren());
            }
        } else {
            return this.metaClass.getGetterType(name);
        }
    }

    /**
     * Has setter boolean
     *
     * @param name name
     * @return the boolean
     */
    @Override
    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.metaClass.hasSetter(prop.getIndexedName())) {
                MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                    return this.metaClass.hasSetter(name);
                } else {
                    return metaValue.hasSetter(prop.getChildren());
                }
            } else {
                return false;
            }
        } else {
            return this.metaClass.hasSetter(name);
        }
    }

    /**
     * Has getter boolean
     *
     * @param name name
     * @return the boolean
     */
    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.metaClass.hasGetter(prop.getIndexedName())) {
                MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                    return this.metaClass.hasGetter(name);
                } else {
                    return metaValue.hasGetter(prop.getChildren());
                }
            } else {
                return false;
            }
        } else {
            return this.metaClass.hasGetter(name);
        }
    }

    /**
     * Instantiate property value meta object
     *
     * @param name          name
     * @param prop          prop
     * @param objectFactory object factory
     * @return the meta object
     */
    @Override
    public MetaObject instantiatePropertyValue(String name, @NotNull PropertyTokenizer prop, ObjectFactory objectFactory) {
        MetaObject metaValue;
        Class<?> type = this.getSetterType(prop.getName());
        try {
            Object newObject = objectFactory.create(type);
            metaValue = MetaObject.forObject(newObject,
                                             this.metaObject.getObjectFactory(),
                                             this.metaObject.getObjectWrapperFactory(),
                                             this.metaObject.getReflectorFactory());
            this.set(prop, newObject);
        } catch (Exception e) {
            throw new ReflectionException("Cannot set value of property '{}' because '{}' is null "
                                          + "and cannot be instantiated on instance of {}. Cause:", name, name, type.getName(),
                                          e.toString(), e);
        }
        return metaValue;
    }

    /**
     * Sets bean property *
     *
     * @param prop   prop
     * @param object object
     * @param value  value
     */
    private void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
        try {
            Invoker method = this.metaClass.getSetInvoker(prop.getName());
            Object[] params = {value};
            try {
                method.invoke(object, params);
            } catch (Throwable t) {
                throw ExceptionUtil.unwrap(t);
            }
        } catch (Throwable t) {
            throw new ReflectionException("Could not set property ' of '{}' with value '{}' Cause: {}", prop.getName(), object.getClass(),
                                          value, t.toString(), t);
        }
    }

    /**
     * Is collection boolean
     *
     * @return the boolean
     */
    @Override
    public boolean isCollection() {
        return false;
    }

    /**
     * Add *
     *
     * @param element element
     */
    @Override
    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Add all *
     *
     * @param <E>  parameter
     * @param list list
     */
    @Override
    public <E> void addAll(List<E> list) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets bean property *
     *
     * @param prop   prop
     * @param object object
     * @return the bean property
     */
    private Object getBeanProperty(PropertyTokenizer prop, Object object) {
        try {
            Invoker method = this.metaClass.getGetInvoker(prop.getName());
            try {
                return method.invoke(object, NO_ARGUMENTS);
            } catch (Throwable t) {
                throw ExceptionUtil.unwrap(t);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new ReflectionException("Could not get property '{}' from {}.  Cause: ", prop.getName(), object.getClass(),
                                          t.toString(), t);
        }
    }

}
