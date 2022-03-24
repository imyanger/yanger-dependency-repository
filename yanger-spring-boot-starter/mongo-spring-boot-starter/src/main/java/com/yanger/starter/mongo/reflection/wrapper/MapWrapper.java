package com.yanger.starter.mongo.reflection.wrapper;

import com.google.common.collect.Maps;
import com.yanger.starter.mongo.reflection.DefaultMetaObject;
import com.yanger.starter.mongo.reflection.MetaObject;
import com.yanger.starter.mongo.reflection.factory.ObjectFactory;
import com.yanger.starter.mongo.reflection.property.PropertyTokenizer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class MapWrapper extends BaseWrapper {

    /** Map */
    private final Map<String, Object> map;

    /**
     * Map wrapper
     * @param metaObject meta object
     * @param map        map
     */
    public MapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject);
        this.map = map;
    }

    /**
     * Get object
     * @param prop prop
     * @return the object
     */
    @Override
    public Object get(@NotNull PropertyTokenizer prop) {
        if (prop.getIndex() != null) {
            Object collection = this.resolveCollection(prop, this.map);
            return this.getCollectionValue(prop, collection);
        } else {
            return this.map.get(prop.getName());
        }
    }

    /**
     * Set
     * @param prop  prop
     * @param value value
     */
    @Override
    public void set(@NotNull PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = this.resolveCollection(prop, this.map);
            this.setCollectionValue(prop, collection, value);
        } else {
            this.map.put(prop.getName(), value);
        }
    }

    /**
     * Find property string
     * @param name                name
     * @param useCamelCaseMapping use camel case mapping
     * @return the string
     */
    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return name;
    }

    /**
     * Get getter names string [ ]
     * @return the string [ ]
     */
    @Override
    public String[] getGetterNames() {
        return this.map.keySet().toArray(new String[0]);
    }

    /**
     * Get setter names string [ ]
     * @return the string [ ]
     */
    @Override
    public String[] getSetterNames() {
        return this.map.keySet().toArray(new String[0]);
    }

    /**
     * Gets setter type
     * @param name name
     * @return the setter type
     */
    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                return Object.class;
            } else {
                return metaValue.getSetterType(prop.getChildren());
            }
        } else {
            if (this.map.get(name) != null) {
                return this.map.get(name).getClass();
            } else {
                return Object.class;
            }
        }
    }

    /**
     * Gets getter type
     * @param name name
     * @return the getter type
     */
    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
            if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                return Object.class;
            } else {
                return metaValue.getGetterType(prop.getChildren());
            }
        } else {
            if (this.map.get(name) != null) {
                return this.map.get(name).getClass();
            } else {
                return Object.class;
            }
        }
    }

    /**
     * Has setter boolean
     * @param name name
     * @return the boolean
     */
    @Override
    public boolean hasSetter(String name) {
        return true;
    }

    /**
     * Has getter boolean
     * @param name name
     * @return the boolean
     */
    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.map.containsKey(prop.getIndexedName())) {
                MetaObject metaValue = this.metaObject.metaObjectForProperty(prop.getIndexedName());
                if (metaValue == DefaultMetaObject.NULL_META_OBJECT) {
                    return true;
                } else {
                    return metaValue.hasGetter(prop.getChildren());
                }
            } else {
                return false;
            }
        } else {
            return this.map.containsKey(prop.getName());
        }
    }

    /**
     * Instantiate property value meta object
     * @param name          name
     * @param prop          prop
     * @param objectFactory object factory
     * @return the meta object
     */
    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        HashMap<String, Object> map = Maps.newHashMap();
        this.set(prop, map);
        return MetaObject.forObject(map,
                                    this.metaObject.getObjectFactory(),
                                    this.metaObject.getObjectWrapperFactory(),
                                    this.metaObject.getReflectorFactory());
    }

    /**
     * Is collection boolean
     * @return the boolean
     */
    @Override
    public boolean isCollection() {
        return false;
    }

    /**
     * Add
     * @param element element
     */
    @Override
    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Add all
     * @param <E>     parameter
     * @param element element
     */
    @Override
    public <E> void addAll(List<E> element) {
        throw new UnsupportedOperationException();
    }

}
