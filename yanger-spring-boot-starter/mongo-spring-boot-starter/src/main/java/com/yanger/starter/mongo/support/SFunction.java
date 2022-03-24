package com.yanger.starter.mongo.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 支持序列化的 Function
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuppressWarnings("all")
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {

}
