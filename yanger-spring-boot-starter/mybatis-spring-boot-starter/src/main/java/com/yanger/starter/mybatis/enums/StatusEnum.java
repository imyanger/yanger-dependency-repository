package com.yanger.starter.mybatis.enums;

import com.yanger.starter.basic.enums.SerializeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据状态枚举
 * @Author yanger
 * @Date 2021/1/29 9:40
 */
@Getter
@AllArgsConstructor
public enum StatusEnum implements SerializeEnum<Integer> {

    /** 正常 */
    NORMAL(1, "正常"),

    /** 禁用 */
    DISABLED(0, "禁用"),

    /** 已删除 */
    DELETED(-1, "已删除");

    /** 数据库存储的值 */
    private final Integer value;

    /** 枚举描述 */
    private final String desc;

}
