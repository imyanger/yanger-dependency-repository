package com.yanger.generator.entity.config;

import java.io.*;

import lombok.Data;

/**
 * @Description 代码生成通用配置
 * @Author yanger
 * @Date 2020/5/26 18:22
 */
@Data
public class GeneralConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 作者名 */
    private String authorName = "yanger";

    /** 生成代码的目标 */
    private String codePath;

    /** 代码包名 */
    private String codePackage;

    /** 是否要lombok */
    private boolean lombok = false;

    /** 对象转换类型 */
    private String convertWay = "beanUtils";

}
