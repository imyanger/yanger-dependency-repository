package com.yanger.starter.mongo.enums;

import com.yanger.starter.mongo.exception.QueryException;

import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public enum Between {
    /** Eq between */
    EQ,
    /** Neq between */
    NEQ,
    /** Feq between */
    FEQ,
    /** Eeq between */
    EEQ;

    /**
     * Between *
     *
     * @param criteria criteria
     * @param begin    begin
     * @param end      end
     */
    public void between(Criteria criteria, Object begin, Object end) {
        switch (this) {
            case EQ:
                criteria.lte(end).gte(begin);
                break;
            case NEQ:
                criteria.lt(end).gt(begin);
                break;
            case FEQ:
                criteria.lt(end).gte(begin);
                break;
            case EEQ:
                criteria.lte(end).gt(begin);
                break;
            default:
                throw new QueryException("no Between enum");
        }
    }
}
