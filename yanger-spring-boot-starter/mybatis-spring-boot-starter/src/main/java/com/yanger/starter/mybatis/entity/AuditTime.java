package com.yanger.starter.mybatis.entity;

import java.util.Date;

/**
 * @Description 时间字段接口
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface AuditTime {

    /** CREATE_TIME */
    String CREATE_TIME = "create_time";

    /** UPDATE_TIME */
    String UPDATE_TIME = "update_time";

    /**
     * Gets create time
     */
    Date getCreateTime();

    /**
     * Gets update time
     */
    Date getUpdateTime();

}
