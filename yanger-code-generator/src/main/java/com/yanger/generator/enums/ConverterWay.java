package com.yanger.generator.enums;

import lombok.Getter;

/**
 * 对象转换工具类型
 * @Author yanger
 * @Date 2020/7/20 18:06
 */
@Getter
public enum ConverterWay {

    BEAN_UTILS(1, "beanUtils"),

    GET_SET(2, "get-set");

    private Integer value;

    private String desc;

    ConverterWay(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
