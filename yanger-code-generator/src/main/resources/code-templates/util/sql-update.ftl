<#if columns?exists && columns?size gt 0>
UPDATE ${tableName}
SET
	<#list columns as column >
    ${column.columnName} = ''<#if column_has_next>,</#if>
	</#list>
WHERE
    <#list columns as column >
    <#if column_index != 0>AND </#if>${column.columnName} = ''
    </#list>
;
</#if>