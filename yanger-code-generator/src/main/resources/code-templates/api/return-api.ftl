package ${codePackage}.api;

<#if 3 == pageType.value>
import com.github.pagehelper.PageInfo;
<#elseif 4 == pageType.value>
import org.springframework.data.domain.Page;
<#elseif 5 == pageType.value>
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
</#if>

import ${savePackage}.${saveObjName};
<#if queryPackage != savePackage && queryObjName != saveObjName>
import ${queryPackage}.${queryObjName};
</#if>
<#if serviceObjPackage != savePackage && serviceObjName != saveObjName>
import ${serviceObjPackage}.${serviceObjName};
</#if>
<#if retPackage != savePackage && retObjName != saveObjName>
import ${retPackage}.${retObjName};
</#if>
import ${serviceInterfacePackage}.${serviceInterfaceName};
import com.yanger.generator.entity.ApiResponse;
<#if acp.needImprotUtil>
import ${codePackage}.util.${modelName}Converter;
</#if>

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description ${modelName}的接口Controller类
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
@RestController
@RequestMapping("${mappingName}")
public class ${modelName}${apiSuffixName} {

    @Autowired
    private ${serviceInterfaceName} ${modelName?uncap_first}Service;

    /**
     * @Description 保存${modelName}
     * @Author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${saveObjName?uncap_first} ${modelName}数据
     * @return ApiResponse<Void>
     */
    @PostMapping
    public ApiResponse<Void> save(@RequestBody ${saveObjName} ${saveObjName?uncap_first}) {
        <#if (acp.saveOrUpdateCp)??>
        ${acp.saveOrUpdateCp}
        </#if>
        ${modelName?uncap_first}Service.save(${serviceObjName?uncap_first});
		return ApiResponse.ok();
    }

    /**
     * @Description 批量保存${modelName}
     * @Author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${saveObjName?uncap_first}s ${modelName}数据集合
     * @return ApiResponse<Void>
     */
    @PostMapping("list")
    public ApiResponse<Void> saveList(@RequestBody List <${saveObjName}> ${saveObjName?uncap_first}s) {
        <#if (acp.saveListCp)??>
        ${acp.saveListCp}
        </#if>
        ${modelName?uncap_first}Service.saveList(${serviceObjName?uncap_first}s);
		return ApiResponse.ok();
    }

    /**
     * @Description 根据id删除${modelName}
     * @Author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return ApiResponse<Void>
     */
    @DeleteMapping("{id}")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        ${modelName?uncap_first}Service.deleteById(id);
        return ApiResponse.ok();
    }

    /**
     * @Description 根据多个id批量删除${modelName}
     * @Author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ids 主键集合
     * @return ApiResponse<Void>
     */
    @DeleteMapping("list")
    public ApiResponse<Void> removeList(@RequestBody List <Long> ids) {
        ${modelName?uncap_first}Service.deleteByIds(ids);
        return ApiResponse.ok();
    }

    /**
     * @Description 更新${modelName}
     * @Author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${saveObjName?uncap_first} ${modelName}数据
     * @return ApiResponse<Void>
     */
    @PutMapping
    public ApiResponse<Void> update(@RequestBody ${saveObjName} ${saveObjName?uncap_first}) {
        ${modelName?uncap_first}Service.update(${modelName}Converter.form2bo(${saveObjName?uncap_first}));
        return ApiResponse.ok();
    }

    /**
     * @Description 根据id查找${modelName}
     * @Author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return ${retPackage}.${retObjName} ${modelName}数据
     */
    @GetMapping("{id}")
    public ApiResponse<${retObjName}> find(@PathVariable Long id) {
        ${serviceObjName} ${serviceObjName?uncap_first} = ${modelName?uncap_first}Service.find(id);
        <#if (acp.returnCp)??>
        ${acp.returnCp}
        </#if>
		return ApiResponse.ok(${retObjName?uncap_first});
    }

    /**
     * @Description 根据条件查询${modelName}
     * @Author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${queryObjName?uncap_first} 查询条件对象
     * @return java.util.List<${retPackage}.${retObjName}> ${modelName}集合数据
     */
    @GetMapping("list")
    public ApiResponse<List<${retObjName}>> findListByQuery(${queryObjName} ${queryObjName?uncap_first}) {
        <#if (acp.queryCp)??>
        ${acp.queryCp}
        </#if>
		List<${serviceObjName}> ${serviceObjName?uncap_first}s = ${modelName?uncap_first}Service.find(${serviceObjName?uncap_first});
        <#if (acp.returnListCp)??>
        ${acp.returnListCp}
        </#if>
		return ApiResponse.ok(${retObjName?uncap_first}s);
    }

    /**
     * @Description 根据条件分页查询${modelName}
     * @Author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${queryObjName?uncap_first} 查询条件对象
     * @param: pageNo 当前页码
     * @param: pageSize 一页数据条数
    <#if 3 == pageType.value>
	* @return com.github.pagehelper.PageInfo<${retPackage}.${retObjName}> ${modelName}分页数据
    <#elseif 4 == pageType.value>
	* @return org.springframework.data.domain.Page<${retPackage}.${retObjName}> ${modelName}分页数据
    <#elseif 5 == pageType.value>
	* @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<${retPackage}.${retObjName}> ${modelName}分页数据
    </#if>
     */
    @GetMapping("page")
    public ApiResponse<<#if 3 == pageType.value>PageInfo<#elseif 4 == pageType.value>Page<#elseif 5 == pageType.value>Page</#if><${retObjName}>> findPageByQuery(${queryObjName} ${queryObjName?uncap_first}, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        <#if (acp.queryCp)??>
        ${acp.queryCp}
        </#if>
        <#if 3 == pageType.value>PageInfo<#elseif 4 == pageType.value>Page<#elseif 5 == pageType.value>Page</#if><${serviceObjName}> ${serviceObjName?uncap_first}Page = ${modelName?uncap_first}Service.findPage(${serviceObjName?uncap_first}, pageNo, pageSize);
        <#if (acp.returnPageCp)??>
        ${acp.returnPageCp}
        </#if>
		return ApiResponse.ok(${retObjName?uncap_first}Page);
    }

}
