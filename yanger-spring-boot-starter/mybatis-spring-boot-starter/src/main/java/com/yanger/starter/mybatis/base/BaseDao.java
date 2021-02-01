package com.yanger.starter.mybatis.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanger.starter.basic.entity.BaseDTO;
import com.yanger.starter.basic.entity.BaseQuery;

import org.apache.ibatis.annotations.Param;

import java.io.*;

/**
 * @Description 基础Dao
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface BaseDao<T> extends BaseMapper<T> {

    /**
     * 插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param entity 实体对象
     * @return 更改的条数 int
     */
    int insertIgnore(T entity);

    /**
     * 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param entity 实体对象
     * @return 更改的条数 int
     */
    int replace(T entity);

    /**
     * 通用分页查询接口
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param page  分页参数
     * @param query 查询参数
     * @return the page
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<?>> IPage<D> page(@Param("page") IPage<D> page,
                                                                                      @Param("query") Q query);

}
