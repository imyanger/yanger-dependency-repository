package com.yanger.generator.entity.bo;

import com.yanger.generator.entity.param.GenerateParam;
import com.yanger.generator.enums.TemplateType;

import java.io.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 代码生成结构
 * @Author yanger
 * @Date 2020/9/16 19:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateStructure implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 模板类型 */
    private TemplateType templateType;

    /**  模板位置（路径） */
    private String templatePath;

    /**  代码目录 */
    private String codePackage;

    /** 模板参数对象 */
    private GenerateParam param;

    /** 文件名称 */
    private String fileName;

}
