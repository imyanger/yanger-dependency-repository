package com.yanger.generator.entity.param;

import com.yanger.generator.entity.sql.ColumnInfo;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description 工具类代码生成参数
 * @Author yanger
 * @Date 2020/7/27 9:29
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UtilsParam extends GenerateParam {

    /** 转换工具类属性集合 */
    private List<ConverterParam> converterParams;

    /** 转换类包名 */
    private String converterPackage;

    /** 表名 */
    private String tableName;

    /** 列属性集合 */
    private List<ColumnInfo> columns;

    /** 要导入的包 */
    private Set<String> importClassList;

}
