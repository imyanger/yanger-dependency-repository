/*
 * Copyright (c)  2019. houbinbin Inc.
 * idoc All rights reserved.
 */

package com.github.houbb.idoc.common.util;


import com.github.houbb.idoc.common.handler.IHandler;

import java.util.*;

/**
 * 集合工具类
 * @author binbin.hou
 * @since 0.0.1
 */
public final class CollectionUtil {

    /**
     * 是否为空
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return null == collection
                || collection.size() == 0;
    }

    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 构建结果列表
     * @param targets 原始信息
     * @param handler 处理接口
     * @param <T> 入参
     * @param <R> 出参
     * @return 结果
     */
    public static <T, R> List<R> buildList(final Collection<T> targets, final IHandler<T, R> handler) {
        if(isEmpty(targets)) {
            return Collections.emptyList();
        }
        List<R> rList = new ArrayList<>(targets.size());
        for(T t : targets) {
            R r = handler.handle(t);
            if(ObjectUtil.isNull(t)) {
                continue;
            }
            rList.add(r);
        }
        return rList;
    }

    /**
     * 将数组的内容添加到集合
     * @param collection 集合
     * @param array 数组
     * @param <T> 泛型
     */
    public static <T> void addArray(final Collection<T> collection, final T[] array) {
        if(ArrayUtil.isEmpty(array)) {
            return;
        }

        collection.addAll(Arrays.asList(array));
    }

}
