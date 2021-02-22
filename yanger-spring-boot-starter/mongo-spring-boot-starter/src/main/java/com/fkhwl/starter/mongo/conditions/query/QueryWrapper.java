package com.fkhwl.starter.mongo.conditions.query;

import com.fkhwl.starter.mongo.conditions.AbstractWrapper;
import com.fkhwl.starter.mongo.mapper.Model;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @param <M> parameter
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 17:50
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class QueryWrapper<M extends Model<M>> extends AbstractWrapper<M, Object, QueryWrapper<M>>
    implements Query<QueryWrapper<M>, M, Object> {

    /**
     * Query wrapper
     *
     * @since 1.0.0
     */
    public QueryWrapper() {
        this(null);
    }

    /**
     * Query wrapper
     *
     * @param entity entity
     * @since 1.0.0
     */
    public QueryWrapper(M entity) {
        super.setEntity(entity);
    }

    /**
     * Query wrapper
     *
     * @param entity      entity
     * @param entityClass entity class
     * @since 1.0.0
     */
    private QueryWrapper(M entity, Class<M> entityClass) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
    }

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     *
     * @return the lambda query wrapper
     * @since 1.0.0
     */
    public LambdaQueryWrapper<M> lambda() {
        return new LambdaQueryWrapper<>(this.getEntity(), this.getEntityClass());
    }

    /**
     * Instance query wrapper
     *
     * @return the query wrapper
     * @since 1.0.0
     */
    @Override
    protected QueryWrapper<M> instance() {
        return new QueryWrapper<>(this.getEntity(), this.getEntityClass());
    }

}
