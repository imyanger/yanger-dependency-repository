package com.yanger.tools.general.bundle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 国际化配置文件动态绑定基类, 所有的业务端都继承此类实现自己的业务配置
 * @Author yanger
 * @Date 2020/12/25 11:54
 */
@Slf4j
public abstract class DynamicBundle extends AbstractBundle {

    /** INSTANCE */
    public static final DynamicBundle INSTANCE = new DynamicBundle("") {};

    /**
     * Dynamic bundle
     *
     * @param pathToBundle path to bundle
     */
    protected DynamicBundle(@NotNull String pathToBundle) {
        super(pathToBundle);
    }

    /**
     * Returns the class this method was called 'framesToSkip' frames up the caller hierarchy.
     * <p>
     * NOTE:
     * <b>Extremely expensive!
     * Please consider not using it.
     * These aren't the droids you're looking for!</b>
     *
     * @param framesToSkip frames to skip
     * @return the class
     */
    @Nullable
    public static Class<?> findCallerClass(int framesToSkip) {
        try {
            Class<?>[] stack = MySecurityManager.INSTANCE.getStack();
            int indexFromTop = 1 + framesToSkip;
            return stack.length > indexFromTop ? stack[indexFromTop] : null;
        } catch (Exception e) {
            log.warn("", e);
            return null;
        }
    }

    private static final class MySecurityManager extends SecurityManager {

        /** INSTANCE */
        private static final MySecurityManager INSTANCE = new MySecurityManager();

        /**
         * Get stack
         *
         * @return the class [ ]
         */
        Class<?>[] getStack() {
            return this.getClassContext();
        }
    }

}
