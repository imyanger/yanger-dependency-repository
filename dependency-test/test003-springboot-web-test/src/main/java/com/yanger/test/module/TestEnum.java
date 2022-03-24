package com.yanger.test.module;

import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.starter.web.annotation.EnumName;
import lombok.Getter;

/**
 * @Author yanger
 * @Date 2022/3/22/022 0:18
 */
@Getter
@EnumName(value = "test")
public enum TestEnum implements SerializeEnum {

    A(1, "one"),

    B(2, "two");

    private Integer value;

    private String desc;

    TestEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
}
