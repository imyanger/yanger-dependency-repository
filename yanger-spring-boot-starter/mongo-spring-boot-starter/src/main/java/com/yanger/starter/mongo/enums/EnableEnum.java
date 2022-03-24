package com.yanger.starter.mongo.enums;

import com.yanger.starter.basic.enums.SerializeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author yanger
 * @Date 2021/3/2 16:20
 */
@Getter
@AllArgsConstructor
public enum EnableEnum implements SerializeEnum<Boolean> {

    /** On enable enum */
    ON(Boolean.TRUE, "可用状态"),

    /** Off enable enum */
    OFF(Boolean.FALSE, "不可用状态");

    /** 数据库存储的值 */
    private final Boolean value;

    /** 枚举描述 */
    private final String desc;

}

