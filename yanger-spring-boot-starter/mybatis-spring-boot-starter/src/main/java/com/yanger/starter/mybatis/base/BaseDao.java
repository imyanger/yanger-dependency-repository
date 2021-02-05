package com.yanger.starter.mybatis.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description 基础Dao
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface BaseDao<T> extends BaseMapper<T> {

    /**
     * 插入如果中已经存在相同的记录,则忽略冲突并插入新数据
     *
     * @param entity 实体对象
     * @return 更改的条数 int
     */
    int insertIgnore(T entity);

    /**
     * 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样
     *
     * @param entity 实体对象
     * @return 更改的条数 int
     */
    int replace(T entity);

    /**
     * 插入数据，使用自定义id（即使是自增id的情况）
     *
     * @param entity 实体对象
     * @return 更改的条数 int
     */
    int insertUseId(T entity);

}
