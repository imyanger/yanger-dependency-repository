package com.yanger.starter.mongo.enums;


import com.yanger.tools.web.exception.BasicException;

import org.springframework.data.mongodb.core.query.Criteria;

/**

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
