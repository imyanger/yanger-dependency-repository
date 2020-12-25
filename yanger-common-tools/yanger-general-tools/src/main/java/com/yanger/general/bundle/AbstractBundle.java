package com.yanger.general.bundle;

import com.yanger.general.support.ConcurrentFactoryMap;
import com.yanger.general.support.ConcurrentSoftValueHashMap;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 国际化配置文件绑定抽象类
 * @Author yanger
 * @Date 2020/12/25 11:53
 */
@Slf4j
public abstract class AbstractBundle {

    /** ourCache */
    private static final Map<ClassLoader, Map<String, ResourceBundle>> CACHE = ConcurrentFactoryMap.createWeakMap(k -> new ConcurrentSoftValueHashMap<>());

    /** My path to bundle */
    @NonNls
    private final String myPathToBundle;

    /** My bundle */
    private Reference<ResourceBundle> myBundle;

    /**
     * Abstract bundle
     *
     * @param pathToBundle path to bundle
     */
    @Contract(pure = true)
    protected AbstractBundle(@NonNls @NotNull String pathToBundle) {
        this.myPathToBundle = pathToBundle;
    }

    /**
     * Gets lazy message *
     *
     * @param key    key
     * @param params params
     * @return the lazy message
     */
    @NotNull
    public Supplier<String> getLazyMessage(@NotNull String key, Object... params) {
        return () -> this.getMessage(key, params);
    }

    /**
     * Gets message *
     *
     * @param key    key
     * @param params params
     * @return the message
     */
    @NotNull
    public String getMessage(@NotNull String key, Object... params) {
        return message(this.getResourceBundle(), key, params);
    }

    /**
     * Message
     *
     * @param bundle bundle
     * @param key    key
     * @param params params
     * @return the string
     */
    @Nls
    @NotNull
    public static String message(@NotNull ResourceBundle bundle, @NotNull String key, Object... params) {
        return BundleBase.message(bundle, key, params);
    }

    /**
     * Gets resource bundle *
     *
     * @return the resource bundle
     */
    public ResourceBundle getResourceBundle() {
        return this.getResourceBundle(null);
    }

    /**
     * Gets resource bundle *
     *
     * @param classLoader class loader
     * @return the resource bundle
     */
    @NotNull
    protected ResourceBundle getResourceBundle(@Nullable ClassLoader classLoader) {
        ResourceBundle bundle = this.myBundle == null ? null : this.myBundle.get();
        if (bundle == null) {
            bundle = this.getResourceBundle(this.myPathToBundle, classLoader == null ? this.getClass().getClassLoader() : classLoader);
            this.myBundle = new SoftReference<>(bundle);
        }
        return bundle;
    }

    /**
     * Gets resource bundle *
     *
     * @param pathToBundle path to bundle
     * @param loader       loader
     * @return the resource bundle
     */
    @NotNull
    public ResourceBundle getResourceBundle(@NotNull String pathToBundle, @NotNull ClassLoader loader) {
        Map<String, ResourceBundle> map = CACHE.get(loader);
        ResourceBundle result = map.get(pathToBundle);
        if (result == null) {
            try {
                ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
                result = this.findBundle(pathToBundle, loader, control);
            } catch (MissingResourceException e) {
                log.info("Cannot load resource bundle from *.properties file, falling back to slow class loading: " + pathToBundle);
                ResourceBundle.clearCache(loader);
                result = ResourceBundle.getBundle(pathToBundle, Locale.getDefault(), loader);
            }
            map.put(pathToBundle, result);
        }
        return result;
    }

    /**
     * Find bundle
     *
     * @param pathToBundle path to bundle
     * @param loader       loader
     * @param control      control
     * @return the resource bundle
     */
    protected ResourceBundle findBundle(@NotNull String pathToBundle, @NotNull ClassLoader loader,
                                        @NotNull ResourceBundle.Control control) {
        return ResourceBundle.getBundle(pathToBundle, Locale.getDefault(), loader, control);
    }

    /**
     * Message of null
     *
     * @param key    key
     * @param params params
     * @return the string
     */
    @Nullable
    public String messageOfNull(@NotNull String key, Object... params) {
        return messageOrNull(this.getResourceBundle(), key, params);
    }

    /**
     * Message or null
     *
     * @param bundle bundle
     * @param key    key
     * @param params params
     * @return the string
     */
    @Nullable
    public static String messageOrNull(@NotNull ResourceBundle bundle, @NotNull String key, Object... params) {
        String value = messageOrDefault(bundle, key, key, params);
        if (key.equals(value)) {
            return null;
        }
        return value;
    }

    /**
     * Message or default
     *
     * @param bundle       bundle
     * @param key          key
     * @param defaultValue default value
     * @param params       params
     * @return the string
     */
    @Contract("null, _, _, _ -> param3")
    public static String messageOrDefault(@Nullable ResourceBundle bundle,
                                          @NotNull String key,
                                          @Nullable String defaultValue,
                                          Object... params) {
        if (bundle == null) {
            return defaultValue;
        } else if (!bundle.containsKey(key)) {
            return BundleBase.postprocessValue(bundle, BundleBase.useDefaultValue(bundle, key, defaultValue), params);
        }
        return BundleBase.messageOrDefault(bundle, key, defaultValue, params);
    }

    /**
     * Message or default
     *
     * @param key          key
     * @param defaultValue default value
     * @param params       params
     * @return the string
     */
    public String messageOrDefault(@NotNull String key,
                                   @Nullable String defaultValue,
                                   Object... params) {
        return messageOrDefault(this.getResourceBundle(), key, defaultValue, params);
    }

    /**
     * Contains key
     *
     * @param key key
     * @return the boolean
     */
    public boolean containsKey(@NotNull String key) {
        return this.getResourceBundle().containsKey(key);
    }
}