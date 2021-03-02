package com.fkhwl.starter.mongo.enums;


import com.yanger.tools.web.exception.BasicException;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: ${description}</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:43
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public enum OP {
    /** Or op */
    OR,
    /** And op */
    AND,
    /** Nor op */
    NOR;

    /**
     * Op *
     *
     * @param criteria  criteria
     * @param criterias criterias
     */
    @SuppressWarnings("checkstyle:ReturnCount")
    public void op(Criteria criteria, Criteria[] criterias) {
        switch (this) {
            case OR:
                criteria.orOperator(criterias);
                return;
            case AND:
                criteria.andOperator(criterias);
                return;
            case NOR:
                criteria.norOperator(criterias);
                return;
            default:
                throw new BasicException("not support");
        }
    }
}
