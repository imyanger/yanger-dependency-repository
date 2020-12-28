package com.yanger.tools.general.bundle;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 国际化配置文件绑定工具类
 * @Author yanger
 * @Date 2020/12/25 11:53
 */
@Slf4j
@UtilityClass
public class BundleUtils {

    /** SET_PARENT */
    private final static Method SET_PARENT = getDeclaredMethod(ResourceBundle.class, "setParent", ResourceBundle.class);

    /**
     * Load language bundle
     *
     * @param pluginClassLoader plugin class loader
     * @param name              name
     * @return the resource bundle
     */
    @Contract("null, _ -> null")
    @Nullable
    public static ResourceBundle loadLanguageBundle(@Nullable ClassLoader pluginClassLoader, String name) {
        if (pluginClassLoader == null) {
            return null;
        }
        ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        ResourceBundle pluginBundle = ResourceBundle.getBundle(name, Locale.getDefault(), pluginClassLoader, control);

        if (pluginBundle == null) {
            return null;
        }
        ResourceBundle base = ResourceBundle.getBundle(name);
        try {
            if (SET_PARENT != null) {
                SET_PARENT.invoke(pluginBundle, base);
            }
        } catch (Exception e) {
            log.warn("", e);
            return null;
        }

        return pluginBundle;
    }

    /**
     * Gets declared method *
     *
     * @param clazz      a class
     * @param name       name
     * @param parameters parameters
     * @return the declared method
     */
    @Nullable
    public static Method getDeclaredMethod(@NotNull Class<?> clazz, @NonNls @NotNull String name, Class<?>... parameters) {
        try {
            return makeAccessible(clazz.getDeclaredMethod(name, parameters));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Make accessible
     *
     * @param method method
     * @return the method
     */
    @Contract("_ -> param1")
    private static @NotNull Method makeAccessible(@NotNull Method method) {
        method.setAccessible(true);
        return method;
    }

}