package com.fkhwl.starter.mongo.core;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface IMongoQuery {
    /**
     * Query.
     *
     * @param criteria the criteria
     */
    void query(Criteria criteria);
}
