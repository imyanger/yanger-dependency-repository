package ${daoInterfacePackage};

import ${modelPackage}.${modelName};

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    @Options(useGeneratedKeys=true, keyProperty="id")
    @Insert({"INSERT INTO `${tableName}`(<#list columns as column ><#if pkColumnName != column.columnName>${column.columnName}<#if column_has_next>, </#if></#if></#list>) ",
             "VALUES(<#list columns as column ><#if pkColumnName != column.columnName>${r"#{"}${column.fieldName}${r"}"}<#if column_has_next>, </#if></#if></#list>)"})
    int insert(${modelName} ${modelName?uncap_first});

    /**
     * @Description 批量保存${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first}s ${modelName}数据集合
     * @return int 影响数据行数
     */
    @Options(useGeneratedKeys=true, keyProperty="id")
    @Insert({"<script>",
                 "INSERT INTO `${tableName}`(<#list columns as column >${column.columnName}<#if column_has_next>, </#if></#list>)",
                 "VALUES",
                 "<foreach collection='list' item='entity' index='index' separator=','>",
                     "(",
                     <#list columns as column >
                         ${r'"'}${r"#{entity."}${column.fieldName}${r"}"}<#if column_has_next>,</#if>${r'",'}
                     </#list>
                     ")",
                 "</foreach>",
             "</script>"})
    int batchInsert(List<${modelName}> ${modelName?uncap_first}s);

    /**
     * @Description 根据条件删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first} ${modelName}数据（条件参数）
     * @return int 影响数据行数
     */
    @Delete({"<script>",
                 "DELETE FROM `${tableName}`",
                 "<where>",
                     <#list columns as column >
                        <#if pkColumnName != column.columnName>
                     ${r'"'}${r"<if test='null != "}${column.fieldName}${r"'>"}${r'",'}
                         ${r'"'}${r"AND "}${column.columnName}${r" = #{"}${column.fieldName}${r"}"}${r'",'}
                     ${r'"'}${r"</if>"}${r'",'}
                        </#if>
                     </#list>
                 "</where>",
             "</script>"})
    int delete(${modelName} ${modelName?uncap_first});

    /**
     * @Description 根据id删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return int 影响数据行数
     */
    @Delete("DELETE FROM `${tableName}` WHERE ${pkColumnName}${r" = #{"}${pkFieldName}${r"}"}")
    int deleteById(Long id);

    /**
     * @Description 根据多个id批量删除${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ids 主键集合
     * @return int 影响数据行数
     */
    @Delete({"<script>",
                 "DELETE FROM `${tableName}` WHERE ${pkColumnName} in ",
                 "(",
                    "<foreach collection='ids' item='id' index='index' separator=','>",
                        "${r'#{id}'}",
                    "</foreach>",
                 ")",
             "</script>"})
    int deleteByIds(@Param("ids") List<Long> ids);

    /**
     * @Description 更新${modelName}，字段为null时覆盖原数据
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first}
     * @return int 影响数据行数
     */
    @Update( {"UPDATE `${tableName}` ",
              "SET <#list columns as column ><#if pkColumnName != column.columnName>${column.columnName}${r" = #{"}${column.fieldName}${r"}"}<#if column_has_next>, </#if></#if></#list> ",
              "WHERE ${pkColumnName}${r" = #{"}${pkFieldName}${r"}"}"})
    int update(${modelName} ${modelName?uncap_first});

    /**
     * @Description 可选择的更新${modelName}，字段为null时不覆盖原数据
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first}
     * @return int 影响数据行数
     */
    @Update({"<script>",
                "UPDATE `${tableName}`",
             "<set>",
                 <#list columns as column >
                     <#if pkColumnName != column.columnName>
                 ${r'"'}${r"<if test='null != "}${column.fieldName}${r"'>"}${r'",'}
                     ${r'"'}${column.columnName}${r" = #{"}${column.fieldName}${r"}"}<#if column_has_next>,</#if>${r'",'}
                 ${r'"'}${r"</if>"}${r'",'}
                     </#if>
                 </#list>
             "</set>",
             "WHERE id= ${r'#{id}'}",
             "</script>"})
    int updateSelective(${modelName} ${modelName?uncap_first});

    /**
     * @Description 根据条件查询${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first} ${modelName}数据（条件参数）
     * @return java.util.List<${modelPackage}.${modelName}> ${modelName}集合数据
     */
    @Results(id = "BaseResultMap", value = {
        <#list columns as column >
        @Result(column = "${column.columnName}", property = "${column.fieldName}"<#if pkColumnName == column.columnName>, id = true</#if>)<#if column_has_next>,</#if>
        </#list>
    })
    @Select({"<script>",
                 "SELECT <#list columns as column >${column.columnName}<#if column_has_next>, </#if></#list> FROM `${tableName}`",
                "<where>",
                     <#list columns as column >
                        <#if pkColumnName != column.columnName>
                     ${r'"'}${r"<if test='null != "}${column.fieldName}${r"'>"}${r'",'}
                        ${r'"'}${r"AND "}${column.columnName}${r" = #{"}${column.fieldName}${r"}"}${r'",'}
                     ${r'"'}${r"</if>"}${r'",'}
                        </#if>
                     </#list>
                "</where>",
             "</script>"})
    List<${modelName}> select(${modelName} ${modelName?uncap_first});

    /**
     * @Description 根据id查找${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param id 主键
     * @return ${modelPackage}.${modelName} ${modelName}数据
     */
    @ResultMap("BaseResultMap")
    @Select("SELECT <#list columns as column >${column.columnName}<#if column_has_next>, </#if></#list> FROM `${tableName}` WHERE ${pkColumnName}${r" = #{"}${pkFieldName}${r"}"}")
    ${modelName} selectById(Long id);

    /**
     * @Description 根据多个id批量查找${modelName}
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ids 主键集合
     * @return java.util.List<${modelPackage}.${modelName}> ${modelName}集合数据
     */
    @ResultMap("BaseResultMap")
    @Select({"<script>",
                "SELECT <#list columns as column >${column.columnName}<#if column_has_next>, </#if></#list> FROM `${tableName}`",
                    "WHERE ${pkColumnName} in",
                    "(",
                        "<foreach collection='ids' item='id' index='index' separator=','>",
                            "${r'#{id}'}",
                        "</foreach>",
                    ")",
             "</script>"})
    List<${modelName}> selectByIds(@Param("ids") List<Long> ids);

    /**
     * @Description 获取满足条件的${modelName}数量
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @param ${modelName?uncap_first} ${modelName}数据（条件参数）
     * @return int 满足条件的数据行数
     */
    @Select({"<script>",
             "SELECT COUNT(1) FROM `${tableName}`",
	             "<where>",
	                 <#list columns as column >
	                     <#if pkColumnName != column.columnName>
	                 ${r'"'}${r"<if test='null != "}${column.fieldName}${r"'>"}${r'",'}
	                     ${r'"'}${r"AND "}${column.columnName}${r" = #{"}${column.fieldName}${r"}"}${r'",'}
	                 ${r'"'}${r"</if>"}${r'",'}
	                     </#if>
	                 </#list>
	             "</where>",
             "</script>"})
    int getCount(${modelName} ${modelName?uncap_first});

    /**
     * @Description 获取总条数
     * @author ${authorName}
     * @date ${.now?string('yyyy-MM-dd HH:mm:ss')}
     * @return int 总数据行数
     */
    @Select("SELECT COUNT(1) FROM `${tableName}`")
    int getTotalCount();

}