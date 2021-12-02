package com.yanger.starter.test.enums;

import com.yanger.starter.basic.enums.SerializeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileType implements SerializeEnum<Integer> {

    /** 文件 */
    FILE(1, "文件，不限制类型"),

    /** 图片 */
    IMAGE(2, "图片");

    /** code */
    private final Integer value;

    /** desc */
    private final String desc;

}

