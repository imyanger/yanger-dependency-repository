package com.fkhwl.starter.mongo.conditions.query;

import com.fkhwl.starter.mongo.conditions.AbstractLambdaWrapper;
import com.fkhwl.starter.mongo.mapper.Model;
import com.fkhwl.starter.mongo.support.SFunction;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @param <M> parameter
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 17:58
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class LambdaQueryWrapper<M extends Model<M>> extends AbstractLambdaWrapper<M, LambdaQueryWrapper<M>>
    implements Query<LambdaQueryWrapper<M>, M, SFunction<M, ?>> {

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaQuery(entity)
     *
     * @since 1.0.0
     */
    public LambdaQueryWrapper() {
        this((M) null);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaQuery(entity)
     *
     * @param entity entity
     * @since 1.0.0
     */
    public LambdaQueryWrapper(M entity) {
        super.setEntity(entity);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaQuery(entity)
     *
     * @param entityClass entity class
     * @since 1.0.0
     */
    public LambdaQueryWrapper(Class<M> entityClass) {
        super.setEntityClass(entityClass);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaQuery(...)
     *
     * @param entity      entity
     * @param entityClass entity class
     * @since 1.0.0
     */
    LambdaQueryWrapper(M entity, Class<M> entityClass) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
    }

    /**
     * Instance lambda query wrapper
     *
     * @return the lambda query wrapper
     * @since 1.0.0
     */
    @Override
    protected LambdaQueryWrapper<M> instance() {
        return new LambdaQueryWrapper<>(this.getEntity(), this.getEntityClass());
    }

}
