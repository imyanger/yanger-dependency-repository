<#if columns?exists && columns?size gt 0>
{
	<#list columns as column>
	"${column.fieldName}":""<#if column_has_next>,</#if>
    </#list>
}
</#if>