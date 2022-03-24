package com.yanger.tools.web.support;

/**
 * ConverterUtils的回调类
 * @Author yanger
 * @Date 2020/7/17 18:36
 */
@FunctionalInterface
public interface ConverterUtilsCallBack<S, T> {

    /**
     * 定义默认回调方法
     * @param t t
     * @param s s
     * @date 2020/7/17
     */
    void callBack(S t, T s);

}