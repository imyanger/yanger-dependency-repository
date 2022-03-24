package com.yanger.starter.mongo.enums;

/**
 * @Author yanger
 * @Date 2021/3/2 11:53
 */
public enum FieldFill {

    /**
     * 默认不处理
     */
    DEFAULT,

    /**
     * 插入时填充字段
     */
    INSERT,

    /**
     * 更新时填充字段
     */
    UPDATE,

    /**
     * 插入和更新时填充字段
     */
    INSERT_UPDATE

}
