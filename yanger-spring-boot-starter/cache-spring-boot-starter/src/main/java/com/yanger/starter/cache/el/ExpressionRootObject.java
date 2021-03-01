package com.yanger.starter.cache.el;

import lombok.Data;

/**
 * @Description Expression root object
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Data
public class ExpressionRootObject {

    /** Object */
    private final Object object;

    /** Args */
    private final Object[] args;

    /**
     * Expression root object
     *
     * @param object object
     * @param args   args
     */
    public ExpressionRootObject(Object object, Object[] args) {
        this.object = object;
        this.args = args;
    }

}
