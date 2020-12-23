package com.yanger.generator.enums;

import lombok.Getter;

/**
 * @Description 分页对象类型
 * @Author yanger
 * @Date 2020/7/20 18:06
 */
@Getter
public enum PageType {

    PAGE_HELPER(3, "pageHelper对象"),

    JPA_PAGE(4, "JPA的分页对象"),

    MYBATIS_PLUS_PAGE(5, "mybatis-plus分页对象");

    private Integer value;

    private String desc;

    PageType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
