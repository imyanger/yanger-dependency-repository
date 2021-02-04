package com.yanger.starter.basic.entity;

import java.io.*;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 时间字段基础DTO对象
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Data
public abstract class BaseTimeDTO<T extends Serializable> extends BaseDTO<T> {

    private static final long serialVersionUID = -8444534935163656524L;

    /** 创建时间 (公共字段) */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /** 最后更新时间 (公共字段) */
    @ApiModelProperty("最后更新时间")
    private Date updateTime;

}
