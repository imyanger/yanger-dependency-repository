package com.yanger.starter.mongo.reflection.wrapper;


import com.yanger.starter.mongo.reflection.MetaObject;
import com.yanger.starter.mongo.reflection.factory.ObjectFactory;
import com.yanger.starter.mongo.reflection.property.PropertyTokenizer;

import java.util.List;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface ObjectWrapper {

    /**
     * Get object
     *
     * @param prop prop
     * @return the object
     */
    Object get(PropertyTokenizer prop);

    /**
     * Set *
     *
     * @param prop  prop
     * @param value value
     */
    void set(PropertyTokenizer prop, Object value);

    /**
     * Find property string
     *
     * @param name                name
     * @param useCamelCaseMapping use camel case mapping
     * @return the string
     */
    String findProperty(String name, boolean useCamelCaseMapping);

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     */
    String[] getGetterNames();

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     */
    String[] getSetterNames();

    /**
     * Gets setter type *
     *
     * @param name name
     * @return the setter type
     */
    Class<?> getSetterType(String name);

    /**
     * Gets getter type *
     *
     * @param name name
     * @return the getter type
     */
    Class<?> getGetterType(String name);

    /**
     * Has setter boolean
     *
     * @param name name
     * @return the boolean
     */
    boolean hasSetter(String name);

    /**
     * Has getter boolean
     *
     * @param name name
     * @return the boolean
     */
    boolean hasGetter(String name);

    /**
     * Instantiate property value meta object
     *
     * @param name          name
     * @param prop          prop
     * @param objectFactory object factory
     * @return the meta object
     */
    MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);

    /**
     * Is collection boolean
     *
     * @return the boolean
     */
    boolean isCollection();

    /**
     * Add *
     *
     * @param element element
     */
    void add(Object element);

    /**
     * Add all *
     *
     * @param <E>     parameter
     * @param element element
     */
    <E> void addAll(List<E> element);

}
