package com.yanger.starter.basic.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Description 框架事件基类
 * @Author yanger
 * @Date 2021/2/25 10:22
 */
public abstract class BaseEvent<T> extends ApplicationEvent {

    private static final long serialVersionUID = 8827571122622596500L;

    /** Source */
    protected transient T source;

    public BaseEvent(T source) {
        super(source);
        this.source = source;
    }

    @Override
    public T getSource() {
        return this.source;
    }

}
