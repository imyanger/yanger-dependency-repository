package ${serviceInterfacePackage};

import org.springframework.data.domain.Page;
import ${serviceObjPackage}.${serviceObjName};

import java.util.List;

/**
 * ${modelName}的业务逻辑Service接口类
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
public interface ${serviceInterfaceName} {

    /**
     * 保存${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first} ${modelName}数据
     * @return void
     */
    void save(${serviceObjName} ${serviceObjName?uncap_first});

    /**
     * 批量保存${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first}s ${modelName}数据集合
     * @return void
     */
    void saveList(List<${serviceObjName}> ${serviceObjName?uncap_first}s);

    /**
     * 根据id删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return void
     */
    void deleteById(Long id);

    /**
     * 根据多个id批量删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ids 主键集合
     * @return void
     */
    void deleteByIds(List<Long> ids);

    /**
     * 更新${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first} ${modelName}数据
     * @return void
     */
    void update(${serviceObjName} ${serviceObjName?uncap_first});

    /**
     * 根据id查找${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return ${serviceObjPackage}.${serviceObjName} ${modelName}数据
     */
    ${serviceObjName} find(Long id);

    /**
     * 根据条件查询${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first} 查询条件对象
     * @return java.util.List<${serviceObjPackage}.${serviceObjName}> ${modelName}集合数据
     */
    List<${serviceObjName}> find(${serviceObjName} ${serviceObjName?uncap_first});

    /**
     * 根据条件分页查询${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${serviceObjName?uncap_first} 查询条件对象
     * @param pageNo 当前页，JPA的页码从0开始
     * @param pageSize 每页大小
     * @return org.springframework.data.domain.Page<${serviceObjPackage}.${serviceObjName}> ${modelName}分页数据
     */
	Page<${serviceObjName}> findPage(${serviceObjName} ${serviceObjName?uncap_first}, int pageNo, int pageSize);

}
