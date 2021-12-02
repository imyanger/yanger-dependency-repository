package com.yanger.starter.mybatis.entity;

import com.yanger.starter.mybatis.enums.StatusEnum;

/**
 * 逻辑删除字段接口
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface DataStatus {

    /** STATUS */
    String STATUS = "status";

    /**
     * Gets status
     */
    StatusEnum getStatus();

}
