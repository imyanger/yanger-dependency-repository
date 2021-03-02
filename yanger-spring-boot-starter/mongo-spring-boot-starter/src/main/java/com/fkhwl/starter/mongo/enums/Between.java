package com.fkhwl.starter.mongo.enums;

import com.fkhwl.starter.mongo.exception.QueryException;

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
