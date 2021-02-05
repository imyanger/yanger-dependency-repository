package com.yanger.starter.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.yanger.starter.basic.entity.BaseDTO;
import com.yanger.starter.basic.entity.BaseQuery;
import com.yanger.starter.mybatis.injector.MybatisSqlMethod;
import com.yanger.starter.mybatis.support.Condition;
import com.yanger.tools.web.tools.BeanUtils;

import org.apache.ibatis.session.SqlSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * @Description BaseService 实现类（ 泛型: M 是 mapper 对象,T 是实体 , PK 是主键泛型 ）
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class BaseServiceImpl<M extends BaseDao<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    /**
     * Save ignore boolean
     *
     * @param entity entity
     * @return the boolean
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
     * 插入数据，使用自定义id（即使是自增id的情况）
     *
     * @param entity 实体对象
     * @return 更改的条数 int
     */
    @Override
    public boolean insertUseId(T entity) {
        return SqlHelper.retBool(this.baseMapper.insertUseId(entity));
    }

    /**
     * 分页查询接口
     *
     * @param <Q>   {@link BaseQuery} 子类
     * @param page  分页参数
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     */
    @Override
    public <Q extends BaseQuery<Long>> IPage<T> page(IPage<T> page, Q query) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        Type sType = this.getClass().getGenericSuperclass();
        Type[] generics = ((ParameterizedType) sType).getActualTypeArguments();
        Class<T> tClass = (Class<T>) (generics[1]);
        queryWrapper.setEntity(BeanUtils.copy(query, tClass));
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 分页查询接口
     *
     * @param pageNo   当前页
     * @param pageSize 一页条数
     * @return the {@link IPage} 的子类 {@link Page}
     */
    @Override
    public IPage<T> page(Integer pageNo, Integer pageSize) {
        BaseQuery<Long> baseQuery = new BaseQuery<>();
        baseQuery.setPageNo(pageNo);
        baseQuery.setPageSize(pageSize);
        return this.page(baseQuery);
    }

    /**
     * 分页查询接口
     *
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the {@link IPage} 的子类 {@link Page}
     */
    @Override
    public <Q extends BaseQuery<Long>> IPage<T> page(Q query) {
        return this.page(Condition.getPage(query), query);
    }

    /**
     * List
     *
     * @param <Q>   {@link BaseQuery} 子类
     * @param query 业务查询参数
     * @return the list
     */
    @Override
    public <Q extends BaseQuery<Long>> List<T> list(Q query) {
        query.setPageSize(-1);
        IPage<T> page = Condition.getPage(query);
        return this.page(page, query).getRecords();
    }


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
    @Override
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(IPage<T> page, Q query, Class<D> dClass) {
        IPage<T> tPage = page(page, query);
        return tPage.convert(t -> BeanUtils.copy(t, dClass));
    }

    /**
     * 分页查询接口
     *
     * @param <D>      {@link BaseDTO} 子类
     * @param pageNo   当前页
     * @param pageSize 页数大小
     * @param dClass   查询对象的泛型类型 {@link BaseDTO} 子类
     * @return the {@link IPage} 的子类 {@link Page}
     */
    @Override
    public <D extends BaseDTO<? extends Serializable>> IPage<D> page(Integer pageNo, Integer pageSize, Class<D> dClass) {
        BaseQuery<Long> baseQuery = new BaseQuery<>();
        baseQuery.setPageNo(pageNo);
        baseQuery.setPageSize(pageSize);
        return this.page(baseQuery, dClass);
    }

    /**
     * 分页查询接口
     *
     * @param <D>    {@link BaseDTO} 子类
     * @param <Q>    {@link BaseQuery} 子类
     * @param query  业务查询参数
     * @param dClass 查询对象的泛型类型 {@link BaseDTO} 子类
     * @return the {@link IPage} 的子类 {@link Page}
     */
    @Override
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> IPage<D> page(Q query, Class<D> dClass) {
        return this.page(Condition.getPage(query), query, dClass);
    }

    /**
     * 根据条件查询所有记录
     *
     * @param <D>    {@link BaseDTO} 子类
     * @param <Q>    {@link BaseQuery} 子类
     * @param query  业务查询参数
     * @param dClass 查询对象的泛型类型 {@link BaseDTO} 子类
     * @return the list
     */
    @Override
    public <D extends BaseDTO<? extends Serializable>, Q extends BaseQuery<Long>> List<D> list(@NotNull Q query, Class<D> dClass) {
        query.setPageSize(-1);
        IPage<T> page = Condition.getPage(query);
        return this.page(page, query, dClass).getRecords();
    }

}
