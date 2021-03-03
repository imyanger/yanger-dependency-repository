package com.yanger.starter.mongo.enums;

import org.jetbrains.annotations.Contract;

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
