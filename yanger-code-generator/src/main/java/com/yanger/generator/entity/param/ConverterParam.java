package com.yanger.generator.entity.param;

import com.yanger.generator.enums.ConverterType;

import java.io.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 转换信息
 * @Author yanger
 * @Date 2020/7/27 9:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConverterParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 转换类型 */
    private ConverterType converterType = ConverterType.OBJ;

    /** 源名称 */
    private String sourceName;

    /** 源包名 */
    private String sourcePackage;

    /** 目标名称 */
    private String targetName;

    /** 目标包名 */
    private String targetPackage;

}
