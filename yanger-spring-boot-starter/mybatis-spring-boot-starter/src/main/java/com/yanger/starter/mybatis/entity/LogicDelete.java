package com.yanger.starter.mybatis.entity;

import com.yanger.starter.mybatis.enums.DeleteEnum;

/**
 * @Description 逻辑删除字段接口
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface LogicDelete {

    /** DELETED */
    String DELETED = "deleted";

    /**
     * Gets deleted
     */
    DeleteEnum getDeleted();

}
