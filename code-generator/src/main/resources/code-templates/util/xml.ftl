<#if columns?exists && columns?size gt 0>
<${modelName}>
    <#list columns as column>
	<${column.fieldName}></${column.fieldName}>
    </#list>
</${modelName}>
</#if>