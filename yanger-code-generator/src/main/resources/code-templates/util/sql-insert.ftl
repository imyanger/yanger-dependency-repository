<#if columns?exists && columns?size gt 0>
INSERT INTO ${tableName}( <#list columns as column >${column.columnName}<#if column_has_next>, </#if></#list> )
VALUES
	(
    <#list columns as column >
        ''<#if column_has_next>,</#if>
    </#list>
	);
</#if>