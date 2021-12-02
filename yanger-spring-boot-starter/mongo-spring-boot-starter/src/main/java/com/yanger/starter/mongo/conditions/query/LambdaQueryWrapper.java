package com.yanger.starter.mongo.conditions.query;

import com.yanger.starter.mongo.conditions.AbstractLambdaWrapper;
import com.yanger.starter.mongo.mapper.Model;
import com.yanger.starter.mongo.support.SFunction;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuppressWarnings("serial")
public class LambdaQueryWrapper<M extends Model<M>> extends AbstractLambdaWrapper<M, LambdaQueryWrapper<M>>
    implements Query<LambdaQueryWrapper<M>, M, SFunction<M, ?>> {

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaQuery(entity)
     */
    public LambdaQueryWrapper() {
        this((M) null);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaQuery(entity)
     *
     * @param entity entity
     */
    public LambdaQueryWrapper(M entity) {
        super.setEntity(entity);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaQuery(entity)
     *
     * @param entityClass entity class
     */
    public LambdaQueryWrapper(Class<M> entityClass) {
        super.setEntityClass(entityClass);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaQuery(...)
     *
     * @param entity      entity
     * @param entityClass entity class
     */
    LambdaQueryWrapper(M entity, Class<M> entityClass) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
    }

    /**
     * Instance lambda query wrapper
     *
     * @return the lambda query wrapper
     */
    @Override
    protected LambdaQueryWrapper<M> instance() {
        return new LambdaQueryWrapper<>(this.getEntity(), this.getEntityClass());
    }

}
