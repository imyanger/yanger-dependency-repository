<#if columns?exists && columns?size gt 0>
    <#list columns as column>
// ${column.columnComment}
${column.fieldClass} ${column.fieldName} = ${modelName?uncap_first}.get${column.fieldName?cap_first}();
    </#list>
</#if>


<#if columns?exists && columns?size gt 0>
    <#list columns as column>
// ${column.columnComment}
${modelName?uncap_first}.set${column.fieldName?cap_first}();
    </#list>
</#if>


<#if columns?exists && columns?size gt 0>
    <#list columns as column>
// ${column.columnComment}
${modelName?uncap_first}.set${column.fieldName?cap_first}(${column.fieldName});
    </#list>
</#if>


<#if columns?exists && columns?size gt 0>
    <#list columns as column>
// ${column.columnComment}
${column.fieldClass} ${column.fieldName} = ${modelName?uncap_first}Old.get${column.fieldName?cap_first}();
${modelName?uncap_first}.set${column.fieldName?cap_first}(${column.fieldName});
    </#list>
</#if>


<#if columns?exists && columns?size gt 0>
    <#list columns as column>
// ${column.columnComment}
${modelName?uncap_first}.set${column.fieldName?cap_first}(${modelName?uncap_first}Old.get${column.fieldName?cap_first}());
    </#list>
</#if>