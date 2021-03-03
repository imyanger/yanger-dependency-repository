package com.yanger.starter.mongo.reflection;

import com.google.common.collect.Maps;

import com.yanger.starter.mongo.reflection.invoker.AmbiguousMethodInvoker;
import com.yanger.starter.mongo.reflection.invoker.GetFieldInvoker;
import com.yanger.starter.mongo.reflection.invoker.Invoker;
import com.yanger.starter.mongo.reflection.invoker.MethodInvoker;
import com.yanger.starter.mongo.reflection.invoker.SetFieldInvoker;
import com.yanger.starter.mongo.reflection.property.PropertyNamer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.ReflectPermission;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Description This class represents a cached set of class definition information
 *     that allows for easy mapping between property names and getter/setter methods.
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class Reflector {

    /** Type */
    private final Class<?> type;
    /** Readable property names */
    private final String[] readablePropertyNames;
    /** Writable property names */
    private final String[] writablePropertyNames;
    /** Set methods */
    private final Map<String, Invoker> setMethods = Maps.newHashMap();
    /** Get methods */
    private final Map<String, Invoker> getMethods = Maps.newHashMap();
    /** Set types */
    private final Map<String, Class<?>> setTypes = Maps.newHashMap();
    /** Get types */
    private final Map<String, Class<?>> getTypes = Maps.newHashMap();
    /** Case insensitive property map */
    private final Map<String, String> caseInsensitivePropertyMap = Maps.newHashMap();
    /** Default constructor */
    private Constructor<?> defaultConstructor;

    /**
     * Reflector
     *
     * @param clazz clazz
     */
    public Reflector(Class<?> clazz) {
        this.type = clazz;
        this.addDefaultConstructor(clazz);
        this.addGetMethods(clazz);
        this.addSetMethods(clazz);
        this.addFields(clazz);
        this.readablePropertyNames = this.getMethods.keySet().toArray(new String[0]);
        this.writablePropertyNames = this.setMethods.keySet().toArray(new String[0]);
        for (String propName : this.readablePropertyNames) {
            this.caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
        for (String propName : this.writablePropertyNames) {
            this.caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
    }

    /**
     * Checks whether can control member accessible.
     *
     * @return If can control member accessible, it return {@literal true}
     */
    public static boolean canControlMemberAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    /**
     * Add default constructor *
     *
     * @param clazz clazz
     */
    private void addDefaultConstructor(@NotNull Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Arrays.stream(constructors).filter(constructor -> constructor.getParameterTypes().length == 0)
            .findAny().ifPresent(constructor -> this.defaultConstructor = constructor);
    }

    /**
     * Add get methods *
     *
     * @param clazz clazz
     */
    private void addGetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingGetters = Maps.newHashMap();
        Method[] methods = this.getClassMethods(clazz);
        Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 0 && PropertyNamer.isGetter(m.getName()))
            .forEach(m -> this.addMethodConflict(conflictingGetters, PropertyNamer.methodToProperty(m.getName()), m));
        this.resolveGetterConflicts(conflictingGetters);
    }

    /**
     * Resolve getter conflicts *
     *
     * @param conflictingGetters conflicting getters
     */
    @SuppressWarnings("checkstyle:EmptyBlock")
    private void resolveGetterConflicts(@NotNull Map<String, List<Method>> conflictingGetters) {
        for (Entry<String, List<Method>> entry : conflictingGetters.entrySet()) {
            Method winner = null;
            String propName = entry.getKey();
            boolean isAmbiguous = false;
            for (Method candidate : entry.getValue()) {
                if (winner == null) {
                    winner = candidate;
                    continue;
                }
                Class<?> winnerType = winner.getReturnType();
                Class<?> candidateType = candidate.getReturnType();
                if (candidateType.equals(winnerType)) {
                    if (!boolean.class.equals(candidateType)) {
                        isAmbiguous = true;
                        break;
                    } else if (candidate.getName().startsWith("is")) {
                        winner = candidate;
                    }
                } else if (candidateType.isAssignableFrom(winnerType)) {
                    // OK getter type is descenda
                } else if (winnerType.isAssignableFrom(candidateType)) {
                    winner = candidate;
                } else {
                    isAmbiguous = true;
                    break;
                }
            }
            this.addGetMethod(propName, winner, isAmbiguous);
        }
    }

    /**
     * Add get method *
     *
     * @param name        name
     * @param method      method
     * @param isAmbiguous is ambiguous
     */
    private void addGetMethod(String name, Method method, boolean isAmbiguous) {
        MethodInvoker invoker = isAmbiguous
                                ? new AmbiguousMethodInvoker(method, MessageFormat.format(
            "Illegal overloaded getter method with ambiguous type for property ''{0}'' in class ''{1}''. "
            + "This breaks the JavaBeans specification and can cause unpredictable results.",
            name, method.getDeclaringClass().getName()))
                                : new MethodInvoker(method);
        this.getMethods.put(name, invoker);
        Type returnType = TypeParameterResolver.resolveReturnType(method, this.type);
        this.getTypes.put(name, this.typeToClass(returnType));
    }

    /**
     * Add set methods *
     *
     * @param clazz clazz
     */
    private void addSetMethods(Class<?> clazz) {
        Map<String, List<Method>> conflictingSetters = Maps.newHashMap();
        Method[] methods = this.getClassMethods(clazz);
        Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 1 && PropertyNamer.isSetter(m.getName()))
            .forEach(m -> this.addMethodConflict(conflictingSetters, PropertyNamer.methodToProperty(m.getName()), m));
        this.resolveSetterConflicts(conflictingSetters);
    }

    /**
     * Add method conflict *
     *
     * @param conflictingMethods conflicting methods
     * @param name               name
     * @param method             method
     */
    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        if (this.isValidPropertyName(name)) {
            List<Method> list = conflictingMethods.computeIfAbsent(name, k -> new ArrayList<>());
            list.add(method);
        }
    }

    /**
     * Resolve setter conflicts *
     *
     * @param conflictingSetters conflicting setters
     */
    private void resolveSetterConflicts(@NotNull Map<String, List<Method>> conflictingSetters) {
        for (String propName : conflictingSetters.keySet()) {
            List<Method> setters = conflictingSetters.get(propName);
            Class<?> getterType = this.getTypes.get(propName);
            boolean isGetterAmbiguous = this.getMethods.get(propName) instanceof AmbiguousMethodInvoker;
            boolean isSetterAmbiguous = false;
            Method match = null;
            for (Method setter : setters) {
                if (!isGetterAmbiguous && setter.getParameterTypes()[0].equals(getterType)) {
                    // should be the best match
                    match = setter;
                    break;
                }
                if (!isSetterAmbiguous) {
                    match = this.pickBetterSetter(match, setter, propName);
                    isSetterAmbiguous = match == null;
                }
            }
            if (match != null) {
                this.addSetMethod(propName, match);
            }
        }
    }

    /**
     * Pick better setter method
     *
     * @param setter1  setter 1
     * @param setter2  setter 2
     * @param property property
     * @return the method
     */
    @Contract("null, _, _ -> param2")
    private @Nullable Method pickBetterSetter(Method setter1, Method setter2, String property) {
        if (setter1 == null) {
            return setter2;
        }
        Class<?> paramType1 = setter1.getParameterTypes()[0];
        Class<?> paramType2 = setter2.getParameterTypes()[0];
        if (paramType1.isAssignableFrom(paramType2)) {
            return setter2;
        } else if (paramType2.isAssignableFrom(paramType1)) {
            return setter1;
        }
        MethodInvoker invoker =
            new AmbiguousMethodInvoker(setter1,
                                       MessageFormat.format(
                                           "Ambiguous setters defined for property ''{0}'' in class ''{1}'' "
                                           + "with types ''{2}'' and ''{3}''.",
                                           property, setter2.getDeclaringClass().getName(), paramType1.getName(), paramType2.getName()));
        this.setMethods.put(property, invoker);
        Type[] paramTypes = TypeParameterResolver.resolveParamTypes(setter1, this.type);
        this.setTypes.put(property, this.typeToClass(paramTypes[0]));
        return null;
    }

    /**
     * Add set method *
     *
     * @param name   name
     * @param method method
     */
    private void addSetMethod(String name, Method method) {
        MethodInvoker invoker = new MethodInvoker(method);
        this.setMethods.put(name, invoker);
        Type[] paramTypes = TypeParameterResolver.resolveParamTypes(method, this.type);
        this.setTypes.put(name, this.typeToClass(paramTypes[0]));
    }

    /**
     * Type to class class
     *
     * @param src src
     * @return the class
     */
    private Class<?> typeToClass(Type src) {
        Class<?> result = null;
        if (src instanceof Class) {
            result = (Class<?>) src;
        } else if (src instanceof ParameterizedType) {
            result = (Class<?>) ((ParameterizedType) src).getRawType();
        } else if (src instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) src).getGenericComponentType();
            if (componentType instanceof Class) {
                result = Array.newInstance((Class<?>) componentType, 0).getClass();
            } else {
                Class<?> componentClass = this.typeToClass(componentType);
                result = Array.newInstance(componentClass, 0).getClass();
            }
        }
        if (result == null) {
            result = Object.class;
        }
        return result;
    }

    /**
     * Add fields *
     *
     * @param clazz clazz
     */
    private void addFields(@NotNull Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!this.setMethods.containsKey(field.getName())) {
                // issue #379 - removed the check for final because JDK 1.5 allows
                // modification of final fields through reflection (JSR-133). (JGB)
                // pr #16 - final static can only be set by the classloader
                int modifiers = field.getModifiers();
                if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
                    this.addSetField(field);
                }
            }
            if (!this.getMethods.containsKey(field.getName())) {
                this.addGetField(field);
            }
        }
        if (clazz.getSuperclass() != null) {
            this.addFields(clazz.getSuperclass());
        }
    }

    /**
     * Add set field *
     *
     * @param field field
     */
    private void addSetField(@NotNull Field field) {
        if (this.isValidPropertyName(field.getName())) {
            this.setMethods.put(field.getName(), new SetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field, this.type);
            this.setTypes.put(field.getName(), this.typeToClass(fieldType));
        }
    }

    /**
     * Add get field *
     *
     * @param field field
     */
    private void addGetField(@NotNull Field field) {
        if (this.isValidPropertyName(field.getName())) {
            this.getMethods.put(field.getName(), new GetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field, this.type);
            this.getTypes.put(field.getName(), this.typeToClass(fieldType));
        }
    }

    /**
     * Is valid property name boolean
     *
     * @param name name
     * @return the boolean
     */
    private boolean isValidPropertyName(@NotNull String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }

    /**
     * This method returns an array containing all methods
     * declared in this class and any superclass.
     * We use this method, instead of the simpler <code>Class.getMethods()</code>,
     * because we want to look for private methods as well.
     *
     * @param clazz The class
     * @return An array containing all methods in this class
     */
    private Method[] getClassMethods(Class<?> clazz) {
        Map<String, Method> uniqueMethods = Maps.newHashMap();
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            this.addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());

            // we also need to look for interface methods -
            // because the class may be abstract
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                this.addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }

            currentClass = currentClass.getSuperclass();
        }

        Collection<Method> methods = uniqueMethods.values();

        return methods.toArray(new Method[0]);
    }

    /**
     * Add unique methods *
     *
     * @param uniqueMethods unique methods
     * @param methods       methods
     */
    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method @NotNull [] methods) {
        for (Method currentMethod : methods) {
            if (!currentMethod.isBridge()) {
                String signature = this.getSignature(currentMethod);
                // check to see if the method is already known
                // if it is known, then an extended class must have
                // overridden a method
                if (!uniqueMethods.containsKey(signature)) {
                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }
    }

    /**
     * Gets signature *
     *
     * @param method method
     * @return the signature
     */
    private @NotNull String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        sb.append(returnType.getName()).append('#');
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            sb.append(i == 0 ? ':' : ',').append(parameters[i].getName());
        }
        return sb.toString();
    }

    /**
     * Gets the name of the class the instance provides information for.
     *
     * @return The class name
     */
    public Class<?> getType() {
        return this.type;
    }

    /**
     * Gets default constructor *
     *
     * @return the default constructor
     */
    public Constructor<?> getDefaultConstructor() {
        if (this.defaultConstructor != null) {
            return this.defaultConstructor;
        } else {
            throw new ReflectionException("There is no default constructor for " + this.type);
        }
    }

    /**
     * Has default constructor boolean
     *
     * @return the boolean
     */
    public boolean hasDefaultConstructor() {
        return this.defaultConstructor != null;
    }

    /**
     * Gets set invoker *
     *
     * @param propertyName property name
     * @return the set invoker
     */
    public Invoker getSetInvoker(String propertyName) {
        Invoker method = this.setMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + this.type + "'");
        }
        return method;
    }

    /**
     * Gets get invoker *
     *
     * @param propertyName property name
     * @return the get invoker
     */
    public Invoker getGetInvoker(String propertyName) {
        Invoker method = this.getMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + this.type + "'");
        }
        return method;
    }

    /**
     * Gets the type for a property setter.
     *
     * @param propertyName - the name of the property
     * @return The Class of the property setter
     */
    public Class<?> getSetterType(String propertyName) {
        Class<?> clazz = this.setTypes.get(propertyName);
        if (clazz == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + this.type + "'");
        }
        return clazz;
    }

    /**
     * Gets the type for a property getter.
     *
     * @param propertyName - the name of the property
     * @return The Class of the property getter
     */
    public Class<?> getGetterType(String propertyName) {
        Class<?> clazz = this.getTypes.get(propertyName);
        if (clazz == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + this.type + "'");
        }
        return clazz;
    }

    /**
     * Gets an array of the readable properties for an object.
     *
     * @return The array
     */
    public String[] getGetablePropertyNames() {
        return this.readablePropertyNames;
    }

    /**
     * Gets an array of the writable properties for an object.
     *
     * @return The array
     */
    public String[] getSetablePropertyNames() {
        return this.writablePropertyNames;
    }

    /**
     * Check to see if a class has a writable property by name.
     *
     * @param propertyName - the name of the property to check
     * @return True if the object has a writable property by the name
     */
    public boolean hasSetter(String propertyName) {
        return this.setMethods.containsKey(propertyName);
    }

    /**
     * Check to see if a class has a readable property by name.
     *
     * @param propertyName - the name of the property to check
     * @return True if the object has a readable property by the name
     */
    public boolean hasGetter(String propertyName) {
        return this.getMethods.containsKey(propertyName);
    }

    /**
     * Find property name string
     *
     * @param name name
     * @return the string
     */
    public String findPropertyName(@NotNull String name) {
        return this.caseInsensitivePropertyMap.get(name.toUpperCase(Locale.ENGLISH));
    }
}
