package com.fkhwl.starter.mongo.enums;

import org.jetbrains.annotations.Contract;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: ${description}</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:44
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public enum Order {
    /** Desc order */
    desc(-1),
    /** Asc order */
    asc(1);

    /** Order value */
    private final int orderValue;

    /**
     * Order
     *
     * @param orderValue order value
     */
    @Contract(pure = true)
    Order(int orderValue) {
        this.orderValue = orderValue;
    }

    /**
     * Gets order value *
     *
     * @return the order value
     */
    @Contract(pure = true)
    public int getOrderValue() {
        return orderValue;
    }
}
