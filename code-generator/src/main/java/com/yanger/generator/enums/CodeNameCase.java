package com.yanger.generator.enums;

import lombok.Getter;

/**
 * @Description 对象转换类型
 * @Author yanger
 * @Date 2020/7/20 18:06
 */
@Getter
public enum CodeNameCase {

    CAMEL("camel","驼峰命名", 1),

    UNDER_LINE("underline","下划线命名", 2);

    private String value;

    private String desc;

    private Integer code;

    CodeNameCase(String value, String desc, Integer code) {
        this.value = value;
        this.desc = desc;
        this.code = code;
    }

}
