package com.fkhwl.starter.mongo.core;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: ${description}</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:49
 * @since 1.0.0
 */
public interface IMongoQuery {
    /**
     * Query.
     *
     * @param criteria the criteria
     * @since 1.0.0
     */
    void query(Criteria criteria);
}
