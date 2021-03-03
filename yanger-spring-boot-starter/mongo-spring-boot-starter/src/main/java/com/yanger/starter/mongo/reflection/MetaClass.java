package com.yanger.starter.mongo.reflection;

import com.yanger.starter.mongo.reflection.invoker.GetFieldInvoker;
import com.yanger.starter.mongo.reflection.invoker.Invoker;
import com.yanger.starter.mongo.reflection.invoker.MethodInvoker;
import com.yanger.starter.mongo.reflection.property.PropertyTokenizer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public final class MetaClass {

    /** Reflector factory */
    private final ReflectorFactory reflectorFactory;
    /** Reflector */
    private final Reflector reflector;

    /**
     * Meta class
     *
     * @param type             type
     * @param reflectorFactory reflector factory
     */
    private MetaClass(Class<?> type, @NotNull ReflectorFactory reflectorFactory) {
        this.reflectorFactory = reflectorFactory;
        this.reflector = reflectorFactory.findForClass(type);
    }

    /**
     * For class meta class
     *
     * @param type             type
     * @param reflectorFactory reflector factory
     * @return the meta class
     */
    @Contract("_, _ -> new")
    public static @NotNull MetaClass forClass(Class<?> type, ReflectorFactory reflectorFactory) {
        return new MetaClass(type, reflectorFactory);
    }

    /**
     * Meta class for property meta class
     *
     * @param name name
     * @return the meta class
     */
    public MetaClass metaClassForProperty(String name) {
        Class<?> propType = this.reflector.getGetterType(name);
        return MetaClass.forClass(propType, this.reflectorFactory);
    }

    /**
     * Find property string
     *
     * @param name name
     * @return the string
     */
    public String findProperty(String name) {
        StringBuilder prop = this.buildProperty(name, new StringBuilder());
        return prop.length() > 0 ? prop.toString() : null;
    }

    /**
     * Find property string
     *
     * @param name                name
     * @param useCamelCaseMapping use camel case mapping
     * @return the string
     */
    public String findProperty(String name, boolean useCamelCaseMapping) {
        if (useCamelCaseMapping) {
            name = name.replace("_", "");
        }
        return this.findProperty(name);
    }

    /**
     * Get getter names string [ ]
     *
     * @return the string [ ]
     */
    public String[] getGetterNames() {
        return this.reflector.getGetablePropertyNames();
    }

    /**
     * Get setter names string [ ]
     *
     * @return the string [ ]
     */
    public String[] getSetterNames() {
        return this.reflector.getSetablePropertyNames();
    }

    /**
     * Gets setter type *
     *
     * @param name name
     * @return the setter type
     */
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaClass metaProp = this.metaClassForProperty(prop.getName());
            return metaProp.getSetterType(prop.getChildren());
        } else {
            return this.reflector.getSetterType(prop.getName());
        }
    }

    /**
     * Gets getter type *
     *
     * @param name name
     * @return the getter type
     */
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaClass metaProp = this.metaClassForProperty(prop);
            return metaProp.getGetterType(prop.getChildren());
        }
        return this.getGetterType(prop);
    }

    /**
     * Meta class for property meta class
     *
     * @param prop prop
     * @return the meta class
     */
    private @NotNull MetaClass metaClassForProperty(PropertyTokenizer prop) {
        Class<?> propType = this.getGetterType(prop);
        return MetaClass.forClass(propType, this.reflectorFactory);
    }

    /**
     * Gets getter type *
     *
     * @param prop prop
     * @return the getter type
     */
    private Class<?> getGetterType(@NotNull PropertyTokenizer prop) {
        Class<?> type = this.reflector.getGetterType(prop.getName());
        if (prop.getIndex() != null && Collection.class.isAssignableFrom(type)) {
            Type returnType = this.getGenericGetterType(prop.getName());
            if (returnType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    returnType = actualTypeArguments[0];
                    if (returnType instanceof Class) {
                        type = (Class<?>) returnType;
                    } else if (returnType instanceof ParameterizedType) {
                        type = (Class<?>) ((ParameterizedType) returnType).getRawType();
                    }
                }
            }
        }
        return type;
    }

    /**
     * Gets generic getter type *
     *
     * @param propertyName property name
     * @return the generic getter type
     */
    private @Nullable Type getGenericGetterType(String propertyName) {
        try {
            Invoker invoker = this.reflector.getGetInvoker(propertyName);
            if (invoker instanceof MethodInvoker) {
                Field methodField = MethodInvoker.class.getDeclaredField("method");
                methodField.setAccessible(true);
                Method method = (Method) methodField.get(invoker);
                return TypeParameterResolver.resolveReturnType(method, this.reflector.getType());
            } else if (invoker instanceof GetFieldInvoker) {
                Field field = GetFieldInvoker.class.getDeclaredField("field");
                field.setAccessible(true);
                return TypeParameterResolver.resolveFieldType((Field) field.get(invoker), this.reflector.getType());
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }

    /**
     * Has setter boolean
     *
     * @param name name
     * @return the boolean
     */
    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.reflector.hasSetter(prop.getName())) {
                MetaClass metaProp = this.metaClassForProperty(prop.getName());
                return metaProp.hasSetter(prop.getChildren());
            } else {
                return false;
            }
        } else {
            return this.reflector.hasSetter(prop.getName());
        }
    }

    /**
     * Has getter boolean
     *
     * @param name name
     * @return the boolean
     */
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.reflector.hasGetter(prop.getName())) {
                MetaClass metaProp = this.metaClassForProperty(prop);
                return metaProp.hasGetter(prop.getChildren());
            } else {
                return false;
            }
        } else {
            return this.reflector.hasGetter(prop.getName());
        }
    }

    /**
     * Gets get invoker *
     *
     * @param name name
     * @return the get invoker
     */
    public Invoker getGetInvoker(String name) {
        return this.reflector.getGetInvoker(name);
    }

    /**
     * Gets set invoker *
     *
     * @param name name
     * @return the set invoker
     */
    public Invoker getSetInvoker(String name) {
        return this.reflector.getSetInvoker(name);
    }

    /**
     * Build property string builder
     *
     * @param name    name
     * @param builder builder
     * @return the string builder
     */
    private StringBuilder buildProperty(String name, StringBuilder builder) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            String propertyName = this.reflector.findPropertyName(prop.getName());
            if (propertyName != null) {
                builder.append(propertyName);
                builder.append(".");
                MetaClass metaProp = this.metaClassForProperty(propertyName);
                metaProp.buildProperty(prop.getChildren(), builder);
            }
        } else {
            String propertyName = this.reflector.findPropertyName(name);
            if (propertyName != null) {
                builder.append(propertyName);
            }
        }
        return builder;
    }

    /**
     * Has default constructor boolean
     *
     * @return the boolean
     */
    public boolean hasDefaultConstructor() {
        return this.reflector.hasDefaultConstructor();
    }

}
