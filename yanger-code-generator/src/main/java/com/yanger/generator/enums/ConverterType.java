package com.yanger.generator.enums;

import lombok.Getter;

/**
 * 对象转换类型
 * @Author yanger
 * @Date 2020/7/20 18:06
 */
@Getter
public enum ConverterType {

    OBJ(1, "普通对象"),

    LIST(2, "集合"),

    PAGE_HELPER(3, "pageHelper对象"),

    JPA_PAGE(4, "JPA的分页对象"),

    MYBATIS_PLUS_PAGE(5, "mybatis-plus分页对象");

    private Integer value;

    private String desc;

    ConverterType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
