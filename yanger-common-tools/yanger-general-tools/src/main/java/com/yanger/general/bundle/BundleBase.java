package com.yanger.general.bundle;

import com.yanger.general.format.OrdinalFormat;
import com.yanger.general.tools.SystemUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import cn.hutool.core.text.StrFormatter;

/**
 * @Description 国际化配置文件绑定基类
 * @Author yanger
 * @Date 2020/12/25 11:53
 */
public abstract class BundleBase {

    /** MNEMONIC */
    public static final char MNEMONIC = 0x1B;

    /** MNEMONIC_STRING */
    public static final String MNEMONIC_STRING = Character.toString(MNEMONIC);

    /** L10N_MARKER */
    public static final String L10N_MARKER = "";

    /** SHOW_LOCALIZED_MESSAGES */
    public static final boolean SHOW_LOCALIZED_MESSAGES = Boolean.getBoolean("idea.l10n");

    /** SUFFIXES */
    private static final String[] SUFFIXES = {"</body></html>", "</html>"};

    /** assertOnMissedKeys */
    private static boolean assertOnMissedKeys = true;

    /**
     * Assert on missed keys
     *
     * @param doAssert do assert
     */
    public static void assertOnMissedKeys(boolean doAssert) {
        assertOnMissedKeys = doAssert;
    }

    /**
     * Message
     *
     * @param bundle bundle
     * @param key    key
     * @param params params
     * @return the string
     */
    @NotNull
    public static String message(@NotNull ResourceBundle bundle, @NotNull String key, @NotNull Object... params) {
        return messageOrDefault(bundle, key, null, params);
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
                                          @NotNull Object... params) {
        if (bundle != null) {
            String value;
            try {
                value = bundle.getString(key);
            } catch (MissingResourceException e) {
                value = useDefaultValue(bundle, key, defaultValue);
            }

            String result = postprocessValue(bundle, value, params);

            if (SHOW_LOCALIZED_MESSAGES) {
                return appendLocalizationMarker(result);
            }
            return result;
        }

        return defaultValue;
    }

    /**
     * Use default value
     *
     * @param bundle       bundle
     * @param key          key
     * @param defaultValue default value
     * @return the string
     */
    @Contract(value = "_, _, !null -> param3", pure = true)
    @NotNull
    static String useDefaultValue(ResourceBundle bundle, @NotNull String key, @Nullable String defaultValue) {
        if (defaultValue != null) {
            return defaultValue;
        }

        if (assertOnMissedKeys) {
            return "'" + key + "' is not found in " + bundle.getBaseBundleName();
        }
        return "!" + key + "!";
    }

    /**
     * 处理占位符, 同时兼容 {} 和 {0} 格式
     *
     * @param bundle bundle
     * @param value  value
     * @param params params
     * @return the string
     */
    static String postprocessValue(@NotNull ResourceBundle bundle, @NotNull String value, @NotNull Object[] params) {
        value = replaceMnemonicAmpersand(value);

        if (params.length > 0 && value.indexOf('{') >= 0) {
            if (value.contains("{0")) {
                Locale locale = bundle.getLocale();
                try {
                    MessageFormat format = locale != null ? new MessageFormat(value, locale) : new MessageFormat(value);
                    OrdinalFormat.apply(format);
                    value = format.format(params);
                } catch (IllegalArgumentException e) {
                    value = "!invalid format: `" + value + "`!";
                }
            } else {
                value = StrFormatter.format(value, params);
            }
        }

        return value;
    }

    /**
     * Append localization marker
     *
     * @param result result
     * @return the string
     */
    @NotNull
    protected static String appendLocalizationMarker(@NotNull String result) {
        for (String suffix : SUFFIXES) {
            if (result.endsWith(suffix)) {
                return result.substring(0, result.length() - suffix.length()) + L10N_MARKER + suffix;
            }
        }
        return result + L10N_MARKER;
    }

    /**
     * Replace mnemonic ampersand
     *
     * @param value value
     * @return the string
     */
    @Contract("null -> null")
    public static String replaceMnemonicAmpersand(@Nullable String value) {
        if (value == null || value.indexOf('&') < 0) {
            return value;
        }

        StringBuilder builder = new StringBuilder();
        boolean macMnemonic = value.contains("&&");
        int i = 0;
        while (i < value.length()) {
            char c = value.charAt(i);
            if (c == '\\') {
                if (i < value.length() - 1 && value.charAt(i + 1) == '&') {
                    builder.append('&');
                    i++;
                } else {
                    builder.append(c);
                }
            } else if (c == '&') {
                if (i < value.length() - 1 && value.charAt(i + 1) == '&') {
                    if (SystemUtils.isMac()) {
                        builder.append(MNEMONIC);
                    }
                    i++;
                } else if (!SystemUtils.isMac() || !macMnemonic) {
                    builder.append(MNEMONIC);
                }
            } else {
                builder.append(c);
            }
            i++;
        }
        return builder.toString();
    }
}
