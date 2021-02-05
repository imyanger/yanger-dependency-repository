package com.yanger.starter.mybatis.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanger.starter.basic.entity.BaseDTO;
import com.yanger.starter.basic.entity.BaseQuery;

import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * @Description 基础 Service
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface BaseService<T> extends IService<T> {
    /**
     * 插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param entity entity
     * @return 是否成功 boolean
     */
    boolean saveIgnore(T entity);

    /**
     * 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param entity entity
     * @return 是否成功 boolean
     */
    boolean saveReplace(T entity);

    /**
     * 插入（批量）,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param entityList 实体对象集合
     * @return 是否成功 boolean
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveIgnoreBatch(Collection<T> entityList) {
        return this.saveIgnoreBatch(entityList, 1000);
    }

    /**
     * 插入（批量）,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param entityList 实体对象集合
     * @param batchSize  批次大小
     * @return 是否成功 boolean
     */
    boolean saveIgnoreBatch(Collection<T> entityList, int batchSize);

    /**
     * 插入（批量）,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param entityList 实体对象集合
     * @return 是否成功 boolean
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean saveReplaceBatch(Collection<T> entityList) {
        return this.saveReplaceBatch(entityList, 1000);
    }

    /**
     * 插入（批量）,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param entityList 实体对象集合
     * @param batchSize  批次大小
     * @return 是否成功 boolean
     */
    boolean saveReplaceBatch(Collection<T> entityList, int batchSize);

    /**
     * 插入数据，使用自定义id（即使是自增id的情况）
     *
     * @param entity 实体对象
     * @return 更改的条数 int
     */
    boolean insertUseId(T entity);

    /**
     * 分页查询接口
     *
     * @param <Q>   {@link BaseQuery} 子类
     * @param page  分页参数
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     */
    <Q extends BaseQuery<Long>> IPage<T> page(IPage<T> page, Q query);

    /**
     * 分页查询接口
     *
     * @param pageNo    当前页
     * @param pageSize  一页条数
     * @return the {@link IPage} 的子类 {@link Page}
     */
    IPage<T> page(Integer pageNo, Integer pageSize);

    /**
     * 分页查询接口
     *
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     */
    <Q extends BaseQuery<Long>> IPage<T> page(Q query);

    /**
     * List
     *
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the list
     */
    <Q extends BaseQuery<Long>> List<T> list(Q query);

    /**
     * 分页查询接口
     *
     * @param <D>    {@link BaseDTO} 子类
     * @param <Q>    {@link BaseQuery} 子类
     * @param page   分页参数
     * @param query  业务查询参数
     * @param dClass 查询对象的泛型类型 {@link BaseDTO} 子类
     * @return the {@link IPage} 的子类 {@link Page}
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(IPage<T> page, Q query, Class<D> dClass);

    /**
     * 分页查询接口
     *
     * @param page  当前页
     * @param limit 一页条数
     * @param dClass 查询对象的泛型类型 {@link BaseDTO} 子类
     * @return the {@link IPage} 的子类 {@link Page}
     */
    <D extends BaseDTO<? extends Serializable>> IPage<D> page(Integer page, Integer limit, Class<D> dClass);

    /**
     * 分页查询接口
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @param dClass 查询对象的泛型类型 {@link BaseDTO} 子类
     * @return the {@link IPage} 的子类 {@link Page}
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(Q query, Class<D> dClass);

    /**
     * List
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @param dClass 查询对象的泛型类型 {@link BaseDTO} 子类
     * @return the list
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> List<D> list(Q query, Class<D> dClass);

}
