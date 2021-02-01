package com.yanger.starter.mybatis.enums;

import com.yanger.starter.basic.enums.SerializeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 逻辑删除枚举
 * @Author yanger
 * @Date 2021/1/29 9:40
 */
@Getter
@AllArgsConstructor
public enum DeleteEnum implements SerializeEnum<Boolean> {

    /** N delete enum */
    N(false, "未删除"),

    /** Y delete enum */
    Y(true, "已删除");

    /** 数据库存储的值 */
    private final Boolean value;

    /** 枚举描述 */
    private final String desc;

}
