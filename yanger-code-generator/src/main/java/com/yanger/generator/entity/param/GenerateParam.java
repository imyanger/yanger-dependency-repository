package com.yanger.generator.entity.param;

import com.yanger.generator.enums.PageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description 代码生成参数
 * @Author yanger
 * @Date 2020/7/21 9:47
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateParam {

    /** 作者名 */
    private String authorName;

    /** 代码包名 */
    private String codePackage;

    /** 实体名称 */
    private String modelName;

    /** 实体包名 */
    private String modelPackage;

    /** 分页类型 */
    private PageType pageType = PageType.PAGE_HELPER;

}
