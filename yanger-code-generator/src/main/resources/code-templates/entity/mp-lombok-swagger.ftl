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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ${modelName}数据，对应数据库${tableName}表
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "${modelName}数据")
public class ${entityName} implements Serializable {

    private static final long serialVersionUID = 1L;

<#list columns as column >
    /** ${column.columnComment}，对应数据库${column.columnName}字段 */
    <#if pkColumnName == column.columnName>
	@TableId
    </#if>
    @ApiModelProperty("${column.columnComment}，对应数据库${column.columnName}字段")
    private ${column.fieldClass} ${column.fieldName};

</#list>
}