package com.yanger.test.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Author yanger
 * @Date 2022/3/20/020 21:41
 */
@Data
@Builder
@AllArgsConstructor
public class TestVo {

    private String name;

    private Integer age;

    private TestEnum testEnum;

}
