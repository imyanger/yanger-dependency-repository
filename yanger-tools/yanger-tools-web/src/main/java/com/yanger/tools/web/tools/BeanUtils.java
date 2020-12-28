package com.yanger.tools.web.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Description 对象拷贝类
 * @Author yanger
 * @Date 2020/7/17 18:34
 */
@SuppressWarnings("all")
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 单个对象拷贝，方便函数式调用
     *
     * @param source 数据源类
     * @param target 目标类::new(eg: UserVO::new)
     * @return T
     * @date 2020/7/17
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target) {
        T t = target.get();
        copyProperties(source, t);
        return t;
    }

    /**
     * 单个对象拷贝，方便函数式调用，增加回调方法（可自定义字段拷贝规则）
     *
     * @param source   数据源类
     * @param target   目标类::new(eg: UserVO::new)
     * @param callBack 回调函数
     * @return T
     * @date 2020/7/17
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target, ConverterUtilsCallBack<S, T> callBack) {
        T t = target.get();
        copyProperties(source, t);
        callBack.callBack(source, t);
        return t;
    }

    /**
     * 集合数据的拷贝
     *
     * @param sources 数据源类
     * @param target  目标类::new(eg: UserVO::new)
     * @return java.util.List<T>
     * @date 2020/7/17
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
     *
     * @param sources  数据源类
     * @param target   目标类::new(eg: UserVO::new)
     * @param callBack 回调函数
     * @return java.util.List<T>
     * @date 2020/7/17
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, ConverterUtilsCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
        }
        return list;
    }

}
