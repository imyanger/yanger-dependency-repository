<#if columns?exists && columns?size gt 0>
DELETE FROM ${tableName}
WHERE
    <#list columns as column >
    ${column.columnName} = ''<#if column_has_next>,</#if>
    </#list>
;
</#if>