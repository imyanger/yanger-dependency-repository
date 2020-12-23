package ${serviceImplPackage};

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ${daoInterfacePackage}.${daoInterfaceName};
import ${modelPackage}.${modelName};
<#if serviceObjPackage != modelPackage && serviceObjName != modelName>
import ${serviceObjPackage}.${serviceObjName};
</#if>
<#if scp.needImprotUtil>
import ${codePackage}.util.${modelName}Converter;
</#if>

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description ${modelName}的业务逻辑Service类
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
@Service
@Transactional
public class ${serviceImplName} {

    @Autowired
    private ${daoInterfaceName} ${daoInterfaceName?uncap_first};

    /**
     * @Description 保存${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first} ${modelName}数据
     * @return void
     */
    public void save(${serviceObjName} ${serviceObjName?uncap_first}) {
        <#if (scp.saveOrUpdateCp)??>
        ${scp.saveOrUpdateCp}
        </#if>
        ${daoInterfaceName?uncap_first}.insert(${modelName?uncap_first});
    }

    /**
     * @Description 批量保存${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first}s ${modelName}数据集合
     * @return void
     */
    public void saveList(List<${serviceObjName}> ${serviceObjName?uncap_first}s) {
        <#if (scp.saveListCp)??>
        ${scp.saveListCp}
        </#if>
        ${daoInterfaceName?uncap_first}.batchInsert(${modelName?uncap_first}s);
    }

    /**
     * @Description 根据id删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return void
     */
    public void deleteById(Long id) {
        ${daoInterfaceName?uncap_first}.deleteById(id);
    }

    /**
     * @Description 根据多个id批量删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ids 主键集合
     * @return void
     */
    public void deleteByIds(List<Long> ids) {
        ${daoInterfaceName?uncap_first}.deleteByIds(ids);
    }

    /**
     * @Description 更新${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first} ${modelName}数据
     * @return void
     */
    public void update(${serviceObjName} ${serviceObjName?uncap_first}) {
        <#if (scp.saveOrUpdateCp)??>
        ${scp.saveOrUpdateCp}
        </#if>
        ${daoInterfaceName?uncap_first}.update(${modelName?uncap_first});
    }

    /**
     * @Description 根据id查找${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return ${serviceObjPackage}.${serviceObjName} ${modelName}数据
     */
    public ${serviceObjName} find(Long id) {
        ${modelName} ${modelName?uncap_first} = ${daoInterfaceName?uncap_first}.selectById(id);
        <#if (scp.returnCp)??>
        ${scp.returnCp}
        </#if>
        return ${serviceObjName?uncap_first};
    }

    /**
     * @Description 根据条件查询${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first} 查询条件对象
     * @return java.util.List<${serviceObjPackage}.${serviceObjName}> ${modelName}集合数据
     */
    public List<${serviceObjName}> find(${serviceObjName} ${serviceObjName?uncap_first}) {
        <#if (scp.serviceCp)??>
        ${scp.serviceCp}
        </#if>
        List<${modelName}> ${modelName?uncap_first}s = ${daoInterfaceName?uncap_first}.select(${modelName?uncap_first});
        <#if (scp.returnListCp)??>
        ${scp.returnListCp}
        </#if>
        return ${serviceObjName?uncap_first}s;
    }

    /**
     * @Description 根据条件分页查询${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first} 查询条件对象
     * @param pageNo 当前页
     * @param pageSize 每页大小
     * @return com.github.pagehelper.PageInfo<${serviceObjPackage}.${serviceObjName}> ${modelName}分页数据
     */
    public PageInfo<${serviceObjName}> findPage(${serviceObjName} ${serviceObjName?uncap_first}, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        <#if (scp.serviceCp)??>
        ${scp.serviceCp}
        </#if>
        List<${modelName}> ${modelName?uncap_first}s = ${daoInterfaceName?uncap_first}.select(${modelName?uncap_first});
        <#if (scp.returnPageCp)??>
        PageInfo<${modelName}> ${modelName?uncap_first}Page = new PageInfo<>(${modelName?uncap_first}s);
        ${scp.returnPageCp}
        </#if>
        return ${serviceObjName?uncap_first}Page;
    }

}
