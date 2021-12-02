package com.yanger.starter.mongo.conditions.query;

import com.yanger.starter.mongo.conditions.AbstractWrapper;
import com.yanger.starter.mongo.mapper.Model;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuppressWarnings("serial")
public class QueryWrapper<M extends Model<M>> extends AbstractWrapper<M, Object, QueryWrapper<M>>
    implements Query<QueryWrapper<M>, M, Object> {

    /**
     * Query wrapper
     */
    public QueryWrapper() {
        this(null);
    }

    /**
     * Query wrapper
     *
     * @param entity entity
     */
    public QueryWrapper(M entity) {
        super.setEntity(entity);
    }

    /**
     * Query wrapper
     *
     * @param entity      entity
     * @param entityClass entity class
     */
    private QueryWrapper(M entity, Class<M> entityClass) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
    }

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     *
     * @return the lambda query wrapper
     */
    public LambdaQueryWrapper<M> lambda() {
        return new LambdaQueryWrapper<>(this.getEntity(), this.getEntityClass());
    }

    /**
     * Instance query wrapper
     *
     * @return the query wrapper
     */
    @Override
    protected QueryWrapper<M> instance() {
        return new QueryWrapper<>(this.getEntity(), this.getEntityClass());
    }

}
