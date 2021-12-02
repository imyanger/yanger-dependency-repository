package com.yanger.generator.entity.param;

import com.yanger.generator.entity.sql.ColumnInfo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Dao代码生成的参数
 * @Author yanger
 * @Date 2020/7/22 18:19
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DaoParam extends GenerateParam {

    /** dao接口类名 */
    private String daoInterfaceName;

    /** dao接口类包名 */
    private String daoInterfacePackage;

    /** 表名 */
    private String tableName;

    /** 列属性集合 */
    private List<ColumnInfo> columns;

    /** 主键列名 */
    private String pkColumnName;

    /** 主键字段名 */
    private String pkFieldName;

    /** 主键类型 */
    private String pkFieldClass;

}
