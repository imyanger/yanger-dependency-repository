package com.yanger.generator.enums;

import lombok.Getter;

/**
 * 模型对象类型
 * @Author yanger
 * @Date 2020/7/20 18:06
 */
@Getter
public enum ModelType {

    VO(1, "Vo"),

    QUERY(2, "Query"),

    FROM(3, "Form"),

    DTO(4, "Dto"),

    BO(5, "Bo"),

    PO(6, "Po");

    private Integer value;

    private String desc;

    ModelType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
