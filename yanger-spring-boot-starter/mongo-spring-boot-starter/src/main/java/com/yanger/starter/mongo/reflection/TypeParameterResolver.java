package com.yanger.starter.mongo.reflection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Arrays;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public final class TypeParameterResolver {

    /**
     * Type parameter resolver
     */
    @Contract(pure = true)
    private TypeParameterResolver() {
        super();
    }

    /**
     * Resolve field type type
     * @param field   field
     * @param srcType src type
     * @return The field type as {@link Type}. If it has type parameters in the declaration,     they will be resolved to the actual
     *     runtime {@link Type}s.
     */
    public static Type resolveFieldType(@NotNull Field field, Type srcType) {
        Type fieldType = field.getGenericType();
        Class<?> declaringClass = field.getDeclaringClass();
        return resolveType(fieldType, srcType, declaringClass);
    }

    /**
     * Resolve return type type
     * @param method  method
     * @param srcType src type
     * @return The return type of the method as {@link Type}. If it has type parameters in the declaration,     they will be resolved to
     *     the actual runtime {@link Type}s.
     */
    public static Type resolveReturnType(@NotNull Method method, Type srcType) {
        Type returnType = method.getGenericReturnType();
        Class<?> declaringClass = method.getDeclaringClass();
        return resolveType(returnType, srcType, declaringClass);
    }

    /**
     * Resolve param types type @ not null [ ]
     * @param method  method
     * @param srcType src type
     * @return The parameter types of the method as an array of {@link Type}s. If they have type parameters in the declaration,     they
     *     will be resolved to the actual runtime {@link Type}s.
     */
    public static Type @NotNull [] resolveParamTypes(@NotNull Method method, Type srcType) {
        Type[] paramTypes = method.getGenericParameterTypes();
        Class<?> declaringClass = method.getDeclaringClass();
        Type[] result = new Type[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            result[i] = resolveType(paramTypes[i], srcType, declaringClass);
        }
        return result;
    }

    /**
     * Resolve type type
     * @param type           type
     * @param srcType        src type
     * @param declaringClass declaring class
     * @return the type
     */
    @Contract("null, _, _ -> null")
    private static Type resolveType(Type type, Type srcType, Class<?> declaringClass) {
        if (type instanceof TypeVariable) {
            return resolveTypeVar((TypeVariable<?>) type, srcType, declaringClass);
        } else if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType) type, srcType, declaringClass);
        } else if (type instanceof GenericArrayType) {
            return resolveGenericArrayType((GenericArrayType) type, srcType, declaringClass);
        } else {
            return type;
        }
    }

    /**
     * Resolve generic array type type
     * @param genericArrayType generic array type
     * @param srcType          src type
     * @param declaringClass   declaring class
     * @return the type
     */
    private static Type resolveGenericArrayType(@NotNull GenericArrayType genericArrayType, Type srcType, Class<?> declaringClass) {
        Type componentType = genericArrayType.getGenericComponentType();
        Type resolvedComponentType = null;
        if (componentType instanceof TypeVariable) {
            resolvedComponentType = resolveTypeVar((TypeVariable<?>) componentType, srcType, declaringClass);
        } else if (componentType instanceof GenericArrayType) {
            resolvedComponentType = resolveGenericArrayType((GenericArrayType) componentType, srcType, declaringClass);
        } else if (componentType instanceof ParameterizedType) {
            resolvedComponentType = resolveParameterizedType((ParameterizedType) componentType, srcType, declaringClass);
        }
        if (resolvedComponentType instanceof Class) {
            return Array.newInstance((Class<?>) resolvedComponentType, 0).getClass();
        } else {
            return new GenericArrayTypeImpl(resolvedComponentType);
        }
    }

    /**
     * Resolve parameterized type parameterized type
     * @param parameterizedType parameterized type
     * @param srcType           src type
     * @param declaringClass    declaring class
     * @return the parameterized type
     */
    @Contract("_, _, _ -> new")
    private static @NotNull ParameterizedType resolveParameterizedType(@NotNull ParameterizedType parameterizedType,
                                                                       Type srcType, Class<?> declaringClass) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        Type[] typeArgs = parameterizedType.getActualTypeArguments();
        Type[] args = resolveWildcardTypeBounds(typeArgs, srcType, declaringClass);
        return new ParameterizedTypeImpl(rawType, null, args);
    }

    /**
     * Resolve wildcard type type
     * @param wildcardType   wildcard type
     * @param srcType        src type
     * @param declaringClass declaring class
     * @return the type
     */
    private static @NotNull Type resolveWildcardType(@NotNull WildcardType wildcardType, Type srcType, Class<?> declaringClass) {
        Type[] lowerBounds = resolveWildcardTypeBounds(wildcardType.getLowerBounds(), srcType, declaringClass);
        Type[] upperBounds = resolveWildcardTypeBounds(wildcardType.getUpperBounds(), srcType, declaringClass);
        return new WildcardTypeImpl(lowerBounds, upperBounds);
    }

    /**
     * Resolve wildcard type bounds type @ not null [ ]
     * @param bounds         bounds
     * @param srcType        src type
     * @param declaringClass declaring class
     * @return the type @ not null [ ]
     */
    private static @NotNull Type[] resolveWildcardTypeBounds(@NotNull Type[] bounds, Type srcType, Class<?> declaringClass) {
        Type[] result = new Type[bounds.length];
        for (int i = 0; i < bounds.length; i++) {
            if (bounds[i] instanceof TypeVariable) {
                result[i] = resolveTypeVar((TypeVariable<?>) bounds[i], srcType, declaringClass);
            } else if (bounds[i] instanceof ParameterizedType) {
                result[i] = resolveParameterizedType((ParameterizedType) bounds[i], srcType, declaringClass);
            } else if (bounds[i] instanceof WildcardType) {
                result[i] = resolveWildcardType((WildcardType) bounds[i], srcType, declaringClass);
            } else {
                result[i] = bounds[i];
            }
        }
        return result;
    }

    /**
     * Resolve type var type
     * @param typeVar        type var
     * @param srcType        src type
     * @param declaringClass declaring class
     * @return the type
     */
    @Contract("_, null, _ -> fail")
    private static Type resolveTypeVar(TypeVariable<?> typeVar, Type srcType, Class<?> declaringClass) {
        Type result;
        Class<?> clazz;
        if (srcType instanceof Class) {
            clazz = (Class<?>) srcType;
        } else if (srcType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) srcType;
            clazz = (Class<?>) parameterizedType.getRawType();
        } else {
            throw new IllegalArgumentException("The 2nd arg must be Class or ParameterizedType, but was: " + srcType.getClass());
        }

        if (clazz == declaringClass) {
            Type[] bounds = typeVar.getBounds();
            if (bounds.length > 0) {
                return bounds[0];
            }
            return Object.class;
        }

        Type superclass = clazz.getGenericSuperclass();
        result = scanSuperTypes(typeVar, srcType, declaringClass, clazz, superclass);
        if (result != null) {
            return result;
        }

        Type[] superInterfaces = clazz.getGenericInterfaces();
        for (Type superInterface : superInterfaces) {
            result = scanSuperTypes(typeVar, srcType, declaringClass, clazz, superInterface);
            if (result != null) {
                return result;
            }
        }
        return Object.class;
    }

    /**
     * Scan super types type
     * @param typeVar        type var
     * @param srcType        src type
     * @param declaringClass declaring class
     * @param clazz          clazz
     * @param superclass     superclass
     * @return the type
     */
    private static @Nullable Type scanSuperTypes(TypeVariable<?> typeVar,
                                                 Type srcType,
                                                 Class<?> declaringClass,
                                                 Class<?> clazz,
                                                 Type superclass) {
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parentAsType = (ParameterizedType) superclass;
            Class<?> parentAsClass = (Class<?>) parentAsType.getRawType();
            TypeVariable<?>[] parentTypeVars = parentAsClass.getTypeParameters();
            if (srcType instanceof ParameterizedType) {
                parentAsType = translateParentTypeVars((ParameterizedType) srcType, clazz, parentAsType);
            }
            if (declaringClass == parentAsClass) {
                for (int i = 0; i < parentTypeVars.length; i++) {
                    if (typeVar == parentTypeVars[i]) {
                        return parentAsType.getActualTypeArguments()[i];
                    }
                }
            }
            if (declaringClass.isAssignableFrom(parentAsClass)) {
                return resolveTypeVar(typeVar, parentAsType, declaringClass);
            }
        } else if (superclass instanceof Class && declaringClass.isAssignableFrom((Class<?>) superclass)) {
            return resolveTypeVar(typeVar, superclass, declaringClass);
        }
        return null;
    }

    /**
     * Translate parent type vars parameterized type
     * @param srcType    src type
     * @param srcClass   src class
     * @param parentType parent type
     * @return the parameterized type
     */
    private static @NotNull ParameterizedType translateParentTypeVars(@NotNull ParameterizedType srcType,
                                                                      @NotNull Class<?> srcClass,
                                                                      @NotNull ParameterizedType parentType) {
        Type[] parentTypeArgs = parentType.getActualTypeArguments();
        Type[] srcTypeArgs = srcType.getActualTypeArguments();
        TypeVariable<?>[] srcTypeVars = srcClass.getTypeParameters();
        Type[] newParentArgs = new Type[parentTypeArgs.length];
        boolean noChange = true;
        for (int i = 0; i < parentTypeArgs.length; i++) {
            if (parentTypeArgs[i] instanceof TypeVariable) {
                for (int j = 0; j < srcTypeVars.length; j++) {
                    if (srcTypeVars[j] == parentTypeArgs[i]) {
                        noChange = false;
                        newParentArgs[i] = srcTypeArgs[j];
                    }
                }
            } else {
                newParentArgs[i] = parentTypeArgs[i];
            }
        }
        return noChange ? parentType : new ParameterizedTypeImpl((Class<?>) parentType.getRawType(), null, newParentArgs);
    }

    static class ParameterizedTypeImpl implements ParameterizedType {

        /** Raw type */
        private final Class<?> rawType;

        /** Owner type */
        private final Type ownerType;

        /** Actual type arguments */
        private final Type[] actualTypeArguments;

        /**
         * Parameterized type
         * @param rawType             raw type
         * @param ownerType           owner type
         * @param actualTypeArguments actual type arguments
         */
        @Contract(pure = true)
        ParameterizedTypeImpl(Class<?> rawType, Type ownerType, Type[] actualTypeArguments) {
            super();
            this.rawType = rawType;
            this.ownerType = ownerType;
            this.actualTypeArguments = actualTypeArguments;
        }

        /**
         * Get actual type arguments type [ ]
         * @return the type [ ]
         */
        @Override
        public Type[] getActualTypeArguments() {
            return this.actualTypeArguments;
        }

        /**
         * Gets raw type
         * @return the raw type
         */
        @Override
        public Type getRawType() {
            return this.rawType;
        }

        /**
         * Gets owner type
         * @return the owner type
         */
        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }

        /**
         * To string string
         * @return the string
         */
        @Override
        public String toString() {
            return "ParameterizedTypeImpl [rawType="
                   + this.rawType
                   + ", ownerType="
                   + this.ownerType
                   + ", actualTypeArguments="
                   + Arrays.toString(this.actualTypeArguments)
                   + "]";
        }
    }

    static class WildcardTypeImpl implements WildcardType {

        /** Lower bounds */
        private final Type[] lowerBounds;

        /** Upper bounds */
        private final Type[] upperBounds;

        /**
         * Wildcard type
         * @param lowerBounds lower bounds
         * @param upperBounds upper bounds
         */
        WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
            super();
            this.lowerBounds = lowerBounds;
            this.upperBounds = upperBounds;
        }

        /**
         * Get upper bounds type [ ]
         * @return the type [ ]
         */
        @Override
        public Type[] getUpperBounds() {
            return this.upperBounds;
        }

        /**
         * Get lower bounds type [ ]
         * @return the type [ ]
         */
        @Override
        public Type[] getLowerBounds() {
            return this.lowerBounds;
        }
    }

    static class GenericArrayTypeImpl implements GenericArrayType {

        /** Generic component type */
        private final Type genericComponentType;

        /**
         * Generic array type
         * @param genericComponentType generic component type
         */
        @Contract(pure = true)
        GenericArrayTypeImpl(Type genericComponentType) {
            super();
            this.genericComponentType = genericComponentType;
        }

        /**
         * Gets generic component type
         * @return the generic component type
         */
        @Override
        public Type getGenericComponentType() {
            return this.genericComponentType;
        }
    }

}
