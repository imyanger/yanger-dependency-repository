package com.yanger.starter.basic.config;

import com.yanger.starter.basic.util.AppStartUtils;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置类接口
 * @Author yanger
 * @Date 2021/1/27 19:29
 */
public interface BaseAutoConfiguration extends InitializingBean, DisposableBean {

    /**
     * 构造方法执行完成后执行
     */
    @PostConstruct
    default void init() {
        String processName = processName(getClass().getName());
        AppStartUtils.loadComponent(processName);
        // 保存所有的自动装配类名
        Constant.COMPONENTS.add(processName);
    }

    /**
     * 处理实现类类名
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
     * bean 初始化完成后执行
     */
    @Override
    default void afterPropertiesSet() {
        execute();
    }

    /**
     * 自动装配类检查
     */
    default void execute() { }

    /**
     * 容器销毁后执行
     */
    @Override
    default void destroy() { }

    @UtilityClass
    class Constant {
        /** 保存所有的自动装配类名 */
        public static final List<String> COMPONENTS = new ArrayList<>();
    }

}