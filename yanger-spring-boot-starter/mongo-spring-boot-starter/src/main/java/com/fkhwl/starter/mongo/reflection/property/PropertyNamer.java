package com.fkhwl.starter.mongo.reflection.property;

import com.fkhwl.starter.mongo.reflection.ReflectionException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 11:47
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public final class PropertyNamer {
    /** BOOLEAN_FIELD_PREFIX */
    private static final String BOOLEAN_FIELD_PREFIX = "is";
    /** GET_FIELD_PREFIX */
    private static final String GET_FIELD_PREFIX = "get";
    /** SET_FIELD_PREFIX */
    private static final String SET_FIELD_PREFIX = "set";

    /**
     * Property namer
     */
    @Contract(pure = true)
    private PropertyNamer() {
        // Prevent Instantiation of Static Class
    }

    /**
     * Method to property string
     *
     * @param name name
     * @return the string
     */
    public static @NotNull String methodToProperty(@NotNull String name) {
        if (name.startsWith(BOOLEAN_FIELD_PREFIX)) {
            name = name.substring(2);
        } else if (name.startsWith(GET_FIELD_PREFIX) || name.startsWith(SET_FIELD_PREFIX)) {
            name = name.substring(3);
        } else {
            throw new ReflectionException("Error parsing property name '{}' .  Didn't start with 'is', 'get' or 'set'.", name);
        }

        boolean matched = name.length() > 1 && !Character.isUpperCase(name.charAt(1));
        if (name.length() == 1 || matched) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    /**
     * Is property boolean
     *
     * @param name name
     * @return the boolean
     */
    public static boolean isProperty(String name) {
        return isGetter(name) || isSetter(name);
    }

    /**
     * Is getter boolean
     *
     * @param name name
     * @return the boolean
     */
    public static boolean isGetter(@NotNull String name) {
        return (name.startsWith("get") && name.length() > 3) || (name.startsWith("is") && name.length() > 2);
    }

    /**
     * Is setter boolean
     *
     * @param name name
     * @return the boolean
     */
    public static boolean isSetter(@NotNull String name) {
        return name.startsWith("set") && name.length() > 3;
    }

}
