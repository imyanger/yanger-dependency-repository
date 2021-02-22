package com.fkhwl.starter.mongo.conditions.update;

import com.fkhwl.starter.mongo.conditions.AbstractWrapper;
import com.fkhwl.starter.mongo.mapper.Model;

/**
 * Update 条件封装
 *
 * @param <M> parameter
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 18:32
 * @since 2018 -05-30
 */
@SuppressWarnings("serial")
public class UpdateWrapper<M extends Model<M>> extends AbstractWrapper<M, String, UpdateWrapper<M>>
    implements Update<UpdateWrapper<M>, String> {

    /**
     * Update wrapper
     *
     * @since 1.0.0
     */
    public UpdateWrapper() {
        // 如果无参构造函数, 请注意实体 NULL 情况 SET 必须有否则 SQL 异常
        this(null);
    }

    /**
     * Update wrapper
     *
     * @param entity entity
     * @since 1.0.0
     */
    public UpdateWrapper(M entity) {
        super.setEntity(entity);
    }


    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     *
     * @return the lambda update wrapper
     * @since 1.0.0
     */
    public LambdaUpdateWrapper<M> lambda() {
        return new LambdaUpdateWrapper<>(this.getEntity());
    }

    /**
     * Instance update wrapper
     *
     * @return the update wrapper
     * @since 1.0.0
     */
    @Override
    protected UpdateWrapper<M> instance() {
        return new UpdateWrapper<>(this.getEntity());
    }

}
