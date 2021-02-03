package com.yanger.starter.basic.entity;

import java.io.*;
import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

// import io.swagger.annotations.ApiModelProperty;

/**
 * @Description 分页查询参数
 * @Author yanger
 * @Date 2021/1/29 10:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BaseQuery<T extends Serializable> extends AbstractBaseEntity<T> {

    private static final long serialVersionUID = -3550589993340031894L;

    /** PAGE */
    public static final String PAGE = "page";

    /** LIMIT */
    public static final String LIMIT = "limit";

    /** ASCS */
    public static final String ASCS = "ascs";

    /** DESCS */
    public static final String DESCS = "descs";

    /** START_TIME */
    public static final String START_TIME = "startTime";

    /** END_TIME */
    public static final String END_TIME = "endTime";

    /** 当前页 */
    @Builder.Default
    // @ApiModelProperty(value = "当前页")
    protected Integer page = 1;

    /** 每页的数量 */
    @Builder.Default
    // @ApiModelProperty(value = "每页的数量")
    protected Integer limit = 10;

    /** 排序的字段名 */
    // @ApiModelProperty(hidden = true)
    protected String ascs;

    /** 排序方式 */
    // @ApiModelProperty(hidden = true)
    protected String descs;

    /** Start time */
    // @ApiModelProperty(value = "待查询记录的开始时间. 格式: [yyyy-MM-dd HH:mm:ss]")
    protected Date startTime;

    /** End time */
    // @ApiModelProperty(value = "待查询记录的结束时间. 格式: [yyyy-MM-dd HH:mm:ss]")
    protected Date endTime;

}
