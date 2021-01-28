package com.fkhwl.starter.mybatis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.fkhwl.starter.common.base.BaseDTO;
import com.fkhwl.starter.common.base.BaseDao;
import com.fkhwl.starter.common.base.BaseQuery;
import com.fkhwl.starter.mybatis.injector.MybatisSqlMethod;
import com.fkhwl.starter.mybatis.service.BaseService;
import com.fkhwl.starter.mybatis.support.Condition;

import org.apache.ibatis.session.SqlSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: BaseService 实现类（ 泛型: M 是 mapper 对象,T 是实体 , PK 是主键泛型 ）</p>
 *
 * @param <M> the type parameter
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.10 17:48
 * @since 1.0.0
 */
public class BaseServiceImpl<M extends BaseDao<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    /**
     * Save ignore boolean
     *
     * @param entity entity
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean saveIgnore(T entity) {
        return SqlHelper.retBool(this.baseMapper.insertIgnore(entity));
    }

    /**
     * Save replace boolean
     *
     * @param entity entity
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean saveReplace(T entity) {
        return SqlHelper.retBool(this.baseMapper.replace(entity));
    }

    /**
     * Save ignore batch boolean
     *
     * @param entityList entity list
     * @param batchSize  batch size
     * @return the boolean
     * @since 1.0.0
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveIgnoreBatch(Collection<T> entityList, int batchSize) {
        return this.saveBatch(entityList, batchSize, MybatisSqlMethod.INSERT_IGNORE_ONE);
    }

    /**
     * Save replace batch boolean
     *
     * @param entityList entity list
     * @param batchSize  batch size
     * @return the boolean
     * @since 1.0.0
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
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
     * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.6.0
     */
    @Override
    @Deprecated
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(IPage<D> page, Q query) {
        Condition.getPage(query);
        return this.baseMapper.page(page, query);
    }

    /**
     * 分页查询接口
     *
     * @param <D>   {@link BaseDTO} 子类
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     * @since 1.6.0
     */
    @Override
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
     * @since 1.6.0
     */
    @Override
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> List<D> list(@NotNull Q query) {
        query.setLimit(-1);
        IPage<D> page = Condition.getPage(query);
        return this.page(page, query).getRecords();
    }
}
