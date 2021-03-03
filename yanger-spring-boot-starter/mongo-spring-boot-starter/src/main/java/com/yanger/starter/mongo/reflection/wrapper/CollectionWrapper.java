package com.yanger.starter.mongo.reflection.wrapper;

import com.yanger.starter.mongo.reflection.MetaObject;
import com.yanger.starter.mongo.reflection.factory.ObjectFactory;
import com.yanger.starter.mongo.reflection.property.PropertyTokenizer;

import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.List;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class CollectionWrapper implements ObjectWrapper {

    /** Object */
    private final Collection<Object> object;

    /**
     * Collection wrapper
     *
     * @param metaObject meta object
     * @param object     object
     */
    @Contract(pure = true)
    public CollectionWrapper(MetaObject metaObject, Collection<Object> object) {
        this.object = object;
    }

    /**
     * Get object
     *
     * @param prop prop
     * @return the object
     */
    @Override
    public Object get(PropertyTokenizer prop) {
        throw new UnsupportedOperationException();
    }

    /**
     * Set *
     *
     * @param prop  prop
     * @param value value
     */
    @Override
    public void set(PropertyTokenizer prop, Object value) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     */
    @Override
    public String[] getGetterNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     */
    @Override
    public String[] getSetterNames() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets setter type *
     *
     * @param name name
     * @return the setter type
     */
    @Override
    public Class<?> getSetterType(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets getter type *
     *
     * @param name name
     * @return the getter type
     */
    @Override
    public Class<?> getGetterType(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Has setter boolean
     *
     * @param name name
     * @return the boolean
     */
    @Override
    public boolean hasSetter(String name) {
        throw new UnsupportedOperationException();
    }

    /**
     * Has getter boolean
     *
     * @param name name
     * @return the boolean
     */
    @Override
    public boolean hasGetter(String name) {
        throw new UnsupportedOperationException();
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
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Is collection boolean
     *
     * @return the boolean
     */
    @Override
    public boolean isCollection() {
        return true;
    }

    /**
     * Add *
     *
     * @param element element
     */
    @Override
    public void add(Object element) {
        this.object.add(element);
    }

    /**
     * Add all *
     *
     * @param <E>     parameter
     * @param element element
     */
    @Override
    public <E> void addAll(List<E> element) {
        this.object.addAll(element);
    }

}
