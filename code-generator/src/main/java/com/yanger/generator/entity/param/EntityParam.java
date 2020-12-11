package com.yanger.generator.entity.param;

import com.yanger.generator.entity.sql.ColumnInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author yanger
 * @Description 模型类生成的参数
 * @date 2020/7/25
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EntityParam extends GenerateParam {

    /** 列属性集合 */
    private List<ColumnInfo> columns;

    /** 主键列名 */
    private String pkColumnName;

    /** 主键字段名 */
    private String pkFieldName;

    /** 实体名称 */
    private String entityName;

    /** 实体包名 */
    private String entityPackage;

    /** 表名 */
    private String tableName;

}
