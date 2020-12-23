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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description ${modelName}数据，对应数据库${tableName}表
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ${entityName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list columns as column >
    /** ${column.columnComment}，对应数据库${column.columnName}字段 */
	<#if pkColumnName == column.columnName>
	@TableId
	</#if>
    private ${column.fieldClass} ${column.fieldName};

</#list>
}