package com.yanger.starter.mongo.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * MyBatis uses an ObjectFactory to create all needed new Objects
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface ObjectFactory {

    /**
     * Sets configuration properties.
     * @param properties configuration properties
     */
    default void setProperties(Properties properties) {
    }

    /**
     * Creates a new object with default constructor.
     * @param <T>  parameter
     * @param type Object type
     * @return t t
     */
    <T> T create(Class<T> type);

    /**
     * Creates a new object with the specified constructor and params.
     * @param <T>                 parameter
     * @param type                Object type
     * @param constructorArgTypes Constructor argument types
     * @param constructorArgs     Constructor argument values
     * @return t t
     */
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

    /**
     * Returns true if this object can have a set of other objects.
     * It's main purpose is to support non-java.util.Collection objects like Scala collections.
     * @param <T>  parameter
     * @param type Object type
     * @return whether it is a collection or not
     */
    <T> boolean isCollection(Class<T> type);

}
