package ${entityPackage};

<#list columns as column >
    <#if pkColumnName == column.columnName>
import com.baomidou.mybatisplus.annotation.TableId;
    </#if>
</#list>

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

/**
 * @Description ${modelName}数据，对应数据库${tableName}表
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
public class ${entityName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list columns as column >
    /** ${column.columnComment}，对应数据库${column.columnName}字段 */
    <#if pkColumnName == column.columnName>
	@TableId
    </#if>
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