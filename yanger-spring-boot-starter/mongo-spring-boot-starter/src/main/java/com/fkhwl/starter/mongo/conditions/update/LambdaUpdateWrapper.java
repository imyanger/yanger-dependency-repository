package com.fkhwl.starter.mongo.conditions.update;

import com.fkhwl.starter.mongo.conditions.AbstractLambdaWrapper;
import com.fkhwl.starter.mongo.mapper.Model;
import com.fkhwl.starter.mongo.support.SFunction;

/**
 * @Description Lambda 更新封装
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuppressWarnings("serial")
public class LambdaUpdateWrapper<M extends Model<M>> extends AbstractLambdaWrapper<M, LambdaUpdateWrapper<M>>
    implements Update<LambdaUpdateWrapper<M>, SFunction<M, ?>> {

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaUpdate()
     */
    public LambdaUpdateWrapper() {
        // 如果无参构造函数, 请注意实体 NULL 情况 SET 必须有否则 SQL 异常
        this((M) null);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaUpdate(entity)
     *
     * @param entity entity
     */
    public LambdaUpdateWrapper(M entity) {
        super.setEntity(entity);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaUpdate(entity)
     *
     * @param entityClass entity class
     */
    public LambdaUpdateWrapper(Class<M> entityClass) {
        super.setEntityClass(entityClass);
    }

    /**
     * 不建议直接 new 该实例, 使用 Wrappers.lambdaUpdate(...)
     *
     * @param entity      entity
     * @param entityClass entity class
     */
    private LambdaUpdateWrapper(M entity, Class<M> entityClass) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
    }

    /**
     * Instance lambda update wrapper
     *
     * @return the lambda update wrapper
     */
    @Override
    protected LambdaUpdateWrapper<M> instance() {
        return new LambdaUpdateWrapper<>(this.getEntity(), this.getEntityClass());
    }

}
