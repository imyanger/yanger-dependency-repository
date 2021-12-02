package com.yanger.generator.enums;

import lombok.Getter;

/**
 * 模板类型
 * @Author yanger
 * @Date 2020/7/20 18:06
 */
@Getter
public enum TemplateType {

    API(1, "api"),

    CONVERT(2, "convert"),

    DAO(3, "dao"),

    ENTITY(4, "entity"),

    MAPPER(5, "mapper"),

    SERVICE(61, "service"),

    SERVICE_IMPL(62, "service-impl"),

    UTIL(7, "util");

    private Integer value;

    private String desc;

    TemplateType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
