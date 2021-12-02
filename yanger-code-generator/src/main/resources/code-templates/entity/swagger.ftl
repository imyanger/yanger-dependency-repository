package ${entityPackage};

import java.io.*;
<#list columns as column >
    <#if "Date" == column.fieldClass>
import java.util.Date;
        <#break>
    </#if>
</#list>
<#list columns as column >
    <#if "BigDecimal" == column.fieldClass>
import java.math.BigDecimal;
        <#break>
    </#if>
</#list>

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ${modelName}数据，对应数据库${tableName}表
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
@ApiModel(value = "${modelName}数据")
public class ${entityName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list columns as column >
    /** ${column.columnComment}，对应数据库${column.columnName}字段 */
    @ApiModelProperty("${column.columnComment}，对应数据库${column.columnName}字段")
    private ${column.fieldClass} ${column.fieldName};

</#list>
<#list columns as column >
    public ${column.fieldClass} get${column.fieldName?cap_first}() {
        return ${column.fieldName};
    }

    public void set${column.fieldName?cap_first}(${column.fieldClass} ${column.fieldName}) {
        this.${column.fieldName} = ${column.fieldName};
    }

</#list>
}