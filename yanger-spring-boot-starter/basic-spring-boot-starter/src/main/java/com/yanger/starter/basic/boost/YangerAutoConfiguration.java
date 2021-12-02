package com.yanger.starter.basic.boost;

import com.yanger.starter.basic.enums.LibraryEnum;
import com.yanger.starter.basic.util.AppStartUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.experimental.UtilityClass;

/**
 * TODO
 * @Author yanger
 * @Date 2021/1/27 19:29
 */
public interface YangerAutoConfiguration extends InitializingBean, DisposableBean {

    /**
     * 构造方法执行完成后执行
     */
    @PostConstruct
    default void init() {
        AppStartUtils.loadComponent(processName(getClass().getName()));
        if (getLibraryType() != null) {
            Constant.COMPONENTS.add(getLibraryType());
        }
    }

    /**
     * 处理实现类类名
     *
     * @param className the class name
     * @return the string
     */
    static String processName(@NotNull String className) {
        int pos = className.indexOf("$$");
        if (pos > 0) {
            className = className.substring(0, className.indexOf("$$"));
        }
        return className;
    }

    /**
     * 启动完成后输出提示信息
     */
    default LibraryEnum getLibraryType() {
        return null;
    }

    /**
     * bean 初始化完成后执行
     */
    @Override
    default void afterPropertiesSet() {
        execute();
    }

    /**
     * 自动装配类检查
     */
    default void execute() {}

    /**
     * 容器销毁后执行
     */
    @Override
    default void destroy() {}

    @UtilityClass
    class Constant {
        /** 保存所有的自动装配类名, 启动时输出 */
        public static final List<LibraryEnum> COMPONENTS = new ArrayList<>();
    }

}