package com.fkhwl.starter.mybatis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fkhwl.starter.common.base.BaseDTO;
import com.fkhwl.starter.common.base.BaseQuery;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: </p>
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 18:21
 * @since 1.0.0
 */
public interface BaseService<T> extends IService<T> {
    /**
     * 插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param entity entity
     * @return 是否成功 boolean
     * @since 1.0.0
     */
    boolean saveIgnore(T entity);

    /**
     * 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param entity entity
     * @return 是否成功 boolean
     * @since 1.0.0
     */
    boolean saveReplace(T entity);

    /**
     * 插入（批量）,插入如果中已经存在相同的记录,则忽略当前新数据
     *
     * @param entityList 实体对象集合
     * @return 是否成功 boolean
     * @since 1.0.0
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
     * @since 1.0.0
     */
    boolean saveIgnoreBatch(Collection<T> entityList, int batchSize);

    /**
     * 插入（批量）,表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
     *
     * @param entityList 实体对象集合
     * @return 是否成功 boolean
     * @since 1.0.0
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
     * @since 1.0.0
     */
    boolean saveReplaceBatch(Collection<T> entityList, int batchSize);

    /**
     * 分页查询接口
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param page  分页参数
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     * @since 1.6.0
     */
    @Deprecated
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(IPage<D> page, Q query);

    /**
     * 分页查询接口
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     * @since 1.6.0
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(Q query);

    /**
     * List
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the list
     * @since 1.6.0
     */
    <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> List<D> list(Q query);
}
