package com.yanger.web.exception;

import com.yanger.general.bundle.DynamicBundle;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @Description 消息外置, 需要在 resources/messages 创建对应的 [CoreBundle.properties] 文件
 * @Author yanger
 * @Date 2020/12/25 13:47
 */
public final class BasicBundle extends DynamicBundle {

    /** BUNDLE */
    @NonNls
    private static final String BUNDLE = "messages.message";

    /** INSTANCE */
    private static final BasicBundle INSTANCE = new BasicBundle();

    /**
     * Plugin bundle
     */
    @Contract(pure = true)
    private BasicBundle() {
        super(BUNDLE);
    }

    /**
     * Message
     *
     * @param key    key
     * @param params params
     * @return the string
     */
    public static String message(@NotNull String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }

}