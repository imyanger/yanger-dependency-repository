package com.yanger.starter.mybatis.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.yanger.starter.basic.entity.BaseDTO;
import com.yanger.starter.basic.entity.BaseQuery;
import com.yanger.starter.mybatis.injector.MybatisSqlMethod;
import com.yanger.starter.mybatis.support.Condition;

import org.apache.ibatis.session.SqlSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * @Description BaseService 实现类（ 泛型: M 是 mapper 对象,T 是实体 , PK 是主键泛型 ）
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class BaseServiceImpl<M extends BaseDao<T>, T> extends ServiceImpl<M, T> {

    /**
     * Save ignore boolean
     *
     * @param entity entity
     * @return the boolean
     */
    public boolean saveIgnore(T entity) {
        return SqlHelper.retBool(this.baseMapper.insertIgnore(entity));
    }

    /**
     * Save replace boolean
     *
     * @param entity entity
     * @return the boolean
     */
    public boolean saveReplace(T entity) {
        return SqlHelper.retBool(this.baseMapper.replace(entity));
    }

    /**
     * Save ignore batch boolean
     *
     * @param entityList entity list
     * @param batchSize  batch size
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveIgnoreBatch(Collection<T> entityList, int batchSize) {
        return this.saveBatch(entityList, batchSize, MybatisSqlMethod.INSERT_IGNORE_ONE);
    }

    /**
     * Save replace batch boolean
     *
     * @param entityList entity list
     * @param batchSize  batch size
     * @return the boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveReplaceBatch(Collection<T> entityList, int batchSize) {
        return this.saveBatch(entityList, batchSize, MybatisSqlMethod.REPLACE_ONE);
    }

    /**
     * Save batch boolean
     *
     * @param entityList entity list
     * @param batchSize  batch size
     * @param sqlMethod  sql method
     * @return the boolean
     */
    private boolean saveBatch(@NotNull Collection<T> entityList, int batchSize, MybatisSqlMethod sqlMethod) {
        String sqlStatement = this.mybatisSqlStatement(sqlMethod);
        try (SqlSession batchSqlSession = SqlHelper.sqlSessionBatch(this.entityClass)) {
            int i = 0;
            for (T anEntityList : entityList) {
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    /**
     * Mybatis sql statement string.
     *
     * @param sqlMethod ignore
     * @return sql string
     */
    protected String mybatisSqlStatement(@NotNull MybatisSqlMethod sqlMethod) {
        return SqlHelper.table(this.currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    /**
     * 分页查询接口
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param page  分页参数
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     */
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(IPage<D> page, Q query) {
        return this.baseMapper.page(page, query);
    }

    /**
     * 分页查询接口
     *
     * @param <D>       {@link BaseDTO} 子类
     * @param pageNo    当前页
     * @param pageSize  页数大小
     * @return the {@link IPage} 的子类 {@link Page}
     */
    public <D extends BaseDTO<? extends Serializable>> IPage<D> page(Integer pageNo, Integer pageSize) {
        BaseQuery baseQuery = new BaseQuery();
        baseQuery.setPageNo(pageNo);
        baseQuery.setPageSize(pageSize);
        return this.page(baseQuery);
    }

    /**
     * 分页查询接口
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     */
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(Q query) {
        return this.page(Condition.getPage(query), query);
    }

    /**
     * 根据条件查询所有记录
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the list
     */
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> List<D> list(@NotNull Q query) {
        query.setPageSize(-1);
        IPage<D> page = Condition.getPage(query);
        return this.page(page, query).getRecords();
    }

}
