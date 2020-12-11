package com.yanger.generator.util;

/**
 * @Description ConverterUtils的回调类
 * @Author yanger
 * @Date 2020/7/17 18:36
 */
@FunctionalInterface
public interface ConverterUtilsCallBack <S, T> {

    /**
     * @Description 定义默认回调方法
     * @author yanger
     * @date 2020/7/17
     * @param t
     * @param s
     * @return void
     */
    void callBack(S t, T s);

}