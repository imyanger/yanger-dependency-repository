package com.yanger.starter.basic.entity;

import java.io.*;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

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
public class BaseQuery<T extends Serializable> extends AbstractBaseEntity<T> {

    private static final long serialVersionUID = -3550589993340031894L;

    /** BaseQuery常量-当前页 */
    public static final String PAGE_NO = "pageNo";

    /** BaseQuery常量-每页的数量 */
    public static final String PAGE_SIZE = "pageSize";

    /** BaseQuery常量-升序的字段名 */
    public static final String ASCS = "ascs";

    /** BaseQuery常量-降序的字段名 */
    public static final String DESCS = "descs";

    /** BaseQuery常量-待查询记录的开始时间 */
    public static final String START_TIME = "startTime";

    /** BaseQuery常量-待查询记录的结束时间 */
    public static final String END_TIME = "endTime";

    /** 当前页 */
    @Builder.Default
    @ApiModelProperty(value = "当前页")
    protected Integer pageNo = 1;

    /** 每页的数量 */
    @Builder.Default
    @ApiModelProperty(value = "每页的数量")
    protected Integer pageSize = 10;

    /** 升序的字段名 */
    @ApiModelProperty(hidden = true)
    protected String ascs;

    /** 降序的字段名 */
    @ApiModelProperty(hidden = true)
    protected String descs;

    /** 待查询记录的开始时间. 格式: [yyyy-MM-dd HH:mm:ss] */
    @ApiModelProperty(value = "待查询记录的开始时间. 格式: [yyyy-MM-dd HH:mm:ss]")
    protected Date startTime;

    /** 待查询记录的结束时间. 格式: [yyyy-MM-dd HH:mm:ss] */
    @ApiModelProperty(value = "待查询记录的结束时间. 格式: [yyyy-MM-dd HH:mm:ss]")
    protected Date endTime;

}
