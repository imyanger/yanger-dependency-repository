package ${daoInterfacePackage};

import ${modelPackage}.${modelName};

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description ${modelName}的数据持久层Dao类接口类
 * @Author ${authorName}
 * @Date ${.now?string('yyyy-MM-dd HH:mm:ss')}
 */
@Mapper
public interface ${daoInterfaceName} {

    /**
     * @Description 保存${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first} ${modelName}数据
     * @return int 影响数据行数
     */
    int insert(${modelName} ${modelName?uncap_first});

    /**
     * @Description 批量保存${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first}s ${modelName}数据集合
     * @return int 影响数据行数
     */
    int batchInsert(List<${modelName}> ${modelName?uncap_first}s);

    /**
     * @Description 根据条件删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first} ${modelName}数据（条件参数）
     * @return int 影响数据行数
     */
    int delete(${modelName} ${modelName?uncap_first});

    /**
     * @Description 根据id删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return int 影响数据行数
     */
    int deleteById(Long id);

    /**
     * @Description 根据多个id批量删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ids 主键集合
     * @return int 影响数据行数
     */
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * @Description 更新${modelName}，字段为null时覆盖原数据
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first}
     * @return int 影响数据行数
     */
    int update(${modelName} ${modelName?uncap_first});

    /**
     * @Description 可选择的更新${modelName}，字段为null时不覆盖原数据
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first}
     * @return int 影响数据行数
     */
    int updateSelective(${modelName} ${modelName?uncap_first});

    /**
     * @Description 根据条件查询${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first} ${modelName}数据（条件参数）
     * @return java.util.List<${modelPackage}.${modelName}> ${modelName}集合数据
     */
    List<${modelName}> select(${modelName} ${modelName?uncap_first});

    /**
     * @Description 根据id查找${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return ${modelPackage}.${modelName} ${modelName}数据
     */
    ${modelName} selectById(Long id);

    /**
     * @Description 根据多个id批量查找${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ids 主键集合
     * @return java.util.List<${modelPackage}.${modelName}> ${modelName}集合数据
     */
    List<${modelName}> selectByIds(@Param("ids") List<Long> ids);

    /**
     * @Description 获取满足条件的${modelName}数量
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first} ${modelName}数据（条件参数）
     * @return int 满足条件的数据行数
     */
    int getCount(${modelName} ${modelName?uncap_first});

    /**
     * @Description 获取总条数
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @return int 总数据行数
     */
    int getTotalCount();

}
