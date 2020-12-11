package com.yanger.generator.entity.sql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * 解析的Table信息
 */
@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tableName;

    private String className;

	private String tableComment;

	private List<ColumnInfo> columns;

    private List<ColumnInfo> pkColumns;

    public ColumnInfo getPkColumnInfo() {
        if(pkColumns != null && pkColumns.size() > 0) {
            if (pkColumns.size() > 1) {
                log.warn("暂不支持联合主键的生成，选取 " + pkColumns.get(0).getColumnName() + " 列作为主键，请自行进行调整");
            }
            return pkColumns.get(0);
        } else {
            log.warn("未获取到主键，默认以 id 作为主键，请自行调整");
            return ColumnInfo.builder().columnComment("主键").columnName("id").fieldClass("Long").fieldName("id").build();
        }
    }


}