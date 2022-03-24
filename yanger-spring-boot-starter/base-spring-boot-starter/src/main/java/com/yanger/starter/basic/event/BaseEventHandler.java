package com.yanger.starter.basic.event;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

/**
 * 框架事件处理基类
 * @Author yanger
 * @Date 2021/2/22 18:10
 */
public abstract class BaseEventHandler<T extends ApplicationEvent> {

    protected abstract void handler(@NotNull T event);

}
