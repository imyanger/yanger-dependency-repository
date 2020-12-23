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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @Description ${modelName}的接口Controller类
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
@Api
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
	@ApiOperation(value="保存${modelName}", tags={"${modelName}${apiSuffixName}接口"}, notes="保存${modelName}")
    public ApiResponse<Void> save(@ApiParam(name = "${saveObjName?uncap_first}", value = "${modelName}数据", required = true) @RequestBody ${saveObjName} ${saveObjName?uncap_first}) {
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
	@ApiOperation(value="批量保存${modelName}", tags={"${modelName}${apiSuffixName}接口"}, notes="批量保存${modelName}")
    public ApiResponse<Void> saveList(@ApiParam(name = "${saveObjName?uncap_first}s", value = "${modelName}数据集合", required = true) @RequestBody List <${saveObjName}> ${saveObjName?uncap_first}s) {
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
	@ApiOperation(value="根据id删除${modelName}", tags={"${modelName}${apiSuffixName}接口"}, notes="根据id删除${modelName}")
    public ApiResponse<Void> remove(@ApiParam(name = "id", value = "主键", required = true) @PathVariable Long id) {
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
	@ApiOperation(value="根据多个id批量删除${modelName}", tags={"${modelName}${apiSuffixName}接口"}, notes="根据多个id批量删除${modelName}")
    public ApiResponse<Void> removeList(@ApiParam(name = "ids", value = "主键集合", required = true) @RequestBody List <Long> ids) {
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
	@ApiOperation(value="更新${modelName}", tags={"${modelName}${apiSuffixName}接口"}, notes="更新${modelName}")
    public ApiResponse<Void> update(@ApiParam(name = "${saveObjName?uncap_first}", value = "${modelName}数据", required = true) @RequestBody ${saveObjName} ${saveObjName?uncap_first}) {
        <#if (acp.saveOrUpdateCp)??>
        ${acp.saveOrUpdateCp}
        </#if>
		${modelName?uncap_first}Service.update(${serviceObjName?uncap_first});
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
	@ApiOperation(value="根据id查找${modelName}", tags={"${modelName}${apiSuffixName}接口"}, notes="根据id查找${modelName}")
    public ApiResponse<${retObjName}> find(@ApiParam(name = "id", value = "主键", required = true) @PathVariable Long id) {
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
	@ApiOperation(value="根据条件查询${modelName}", tags={"${modelName}${apiSuffixName}接口"}, notes="根据条件查询${modelName}")
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
	@ApiOperation(value="根据条件分页查询${modelName}", tags={"${modelName}${apiSuffixName}接口"}, notes="根据条件分页查询${modelName}")
    public ApiResponse<<#if 3 == pageType.value>PageInfo<#elseif 4 == pageType.value>Page<#elseif 5 == pageType.value>Page</#if><${retObjName}>> findPageByQuery(${queryObjName} ${queryObjName?uncap_first}, @RequestParam(value = "1") int pageNo, @RequestParam(value = "10") int pageSize) {
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
