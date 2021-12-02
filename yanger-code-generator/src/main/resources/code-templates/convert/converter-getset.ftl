package ${converterPackage};

<#list importClassList as importClass >
import ${importClass};
</#list>

<#list converterParams as converterParam >
    <#if 3 == converterParam.converterType.value>
import com.github.pagehelper.PageInfo;
        <#break>
    </#if>
</#list>

<#list converterParams as converterParam >
    <#if 5 == converterParam.converterType.value>
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
        <#break>
    </#if>
</#list>

<#list converterParams as converterParam >
    <#if 4 == converterParam.converterType.value>
import org.springframework.data.domain.Page;
        <#break>
    </#if>
</#list>

<#list converterParams as converterParam >
    <#if 2 == converterParam.converterType.value>
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
        <#break>
    </#if>
</#list>

/**
 * ${modelName}数据转换工具类
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
public class ${modelName}Converter {

<#list converterParams as converterParam >
    <#if 1 == converterParam.converterType.value>
    /**
     * ${converterParam.sourceName}对象转换为${converterParam.targetName}对象
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${converterParam.sourceName?uncap_first} ${converterParam.sourceName}对象
     * @return ${converterParam.targetPackage}.${converterParam.targetName} ${converterParam.targetName}对象
     */
    public static ${converterParam.targetName} ${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}(${converterParam.sourceName} ${converterParam.sourceName?uncap_first}) {
        if (${converterParam.sourceName?uncap_first} == null) {
            return null;
        }
        ${converterParam.targetName} ${converterParam.targetName?uncap_first} = new ${converterParam.targetName}();
        <#list columns as column>
	    // ${column.columnComment}
        ${converterParam.targetName?uncap_first}.set${column.fieldName?cap_first}(${converterParam.sourceName?uncap_first}.get${column.fieldName?cap_first}());
        </#list>
        return ${converterParam.targetName?uncap_first};
    }
    <#elseif 2 == converterParam.converterType.value>
    /**
     * ${converterParam.sourceName}集合转换为${converterParam.targetName}集合
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${converterParam.sourceName?uncap_first}s ${converterParam.sourceName}集合
     * @return java.util.List<${converterParam.targetPackage}.${converterParam.targetName}> ${converterParam.targetName}集合
     */
    public static List<${converterParam.targetName}> ${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}s2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}s(List<${converterParam.sourceName}> ${converterParam.sourceName?uncap_first}s) {
        if (${converterParam.sourceName?uncap_first}s == null || ${converterParam.sourceName?uncap_first}s.size() == 0) {
            return Collections.emptyList();
        }
        return ${converterParam.sourceName?uncap_first}s.stream().map(${modelName}Converter::${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}).collect(Collectors.toList());
    }
    <#elseif 3 == converterParam.converterType.value>
    /**
     * PageInfo<${converterParam.sourceName}>转换为PageInfo<${converterParam.targetName}>集合
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param page ${converterParam.sourceName}的PageInfo分页对象
     * @return com.github.pagehelper.PageInfo<${converterParam.targetName}> ${converterParam.targetName}的PageInfo分页对象
     */
    public static PageInfo<${converterParam.targetName}> ${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}s2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}s(PageInfo<${converterParam.sourceName}> page) {
        if (page == null || page.getList() == null || page.getList().size() == 0) {
            return new PageInfo <>();
        }
        List <${converterParam.targetName}> ${converterParam.targetName?uncap_first}s = ${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}s2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}s(page.getList());
	    Page<${converterParam.targetName}> newPage = new Page<>(page.getPageNum(), page.getPageSize());
	    newPage.setTotal(page.getTotal());
	    PageInfo<${converterParam.targetName}> pageInfo = new PageInfo<>(newPage);
	    pageInfo.setList(${converterParam.targetName?uncap_first}s);
        return pageInfo;
    }
    <#elseif 5 == converterParam.converterType.value>
    /**
     * Page<${converterParam.sourceName}>转换为Page<${converterParam.targetName}>集合
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param page ${converterParam.sourceName}的Page分页对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<${converterParam.targetName}> ${converterParam.targetName}的Page分页对象
     */
    public static Page<${converterParam.targetName}> ${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}s2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}s(Page<${converterParam.sourceName}> page) {
        if (page == null || page.getRecords() == null || page.getRecords().size() == 0) {
            return new Page<>();
        }
		List<${converterParam.targetName}> ${converterParam.targetName?uncap_first}s = ${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}s2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}s(page.getRecords());
		Page<${converterParam.targetName}> newPage = new Page<>(page.getCurrent(), page.getSize(),page.getTotal());
		newPage.setRecords(${converterParam.targetName?uncap_first}s);
        return newPage;
    }
	<#elseif 4 == converterParam.converterType.value>
    /**
     * Page<${converterParam.sourceName}>转换为Page<${converterParam.targetName}>集合
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param page ${converterParam.sourceName}的Page分页对象
     * @return org.springframework.data.domain.Page<${converterParam.targetName}> ${converterParam.targetName}的Page分页对象
     */
    public static Page<${converterParam.targetName}> ${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}s2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}s(Page<${converterParam.sourceName}> page) {
        if (page == null || page.getRecords() == null || page.getRecords().size() == 0) {
            return new Page<>();
        }
		List<${converterParam.targetName}> ${converterParam.targetName?uncap_first}s = ${(modelName!=converterParam.sourceName)?string((converterParam.sourceName?replace(modelName,"")?uncap_first),"po")}s2${(modelName!=converterParam.targetName)?string((converterParam.targetName?replace(modelName,"")?uncap_first),"po")}s(page.getRecords());
		Page<${converterParam.targetName}> newPage = new Page<>(page.getCurrent(), page.getSize(),page.getTotal());
		newPage.setRecords(${converterParam.targetName?uncap_first}s);
        return newPage;
    }
    </#if>

</#list>
}
