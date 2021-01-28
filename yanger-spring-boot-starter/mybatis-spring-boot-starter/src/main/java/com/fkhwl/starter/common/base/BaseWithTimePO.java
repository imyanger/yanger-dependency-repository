package com.fkhwl.starter.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.*;
import java.util.Date;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: </p>
 * 1. 新增记录时, 不需要以下 4 个公共字段, 将根据注解自动生成;
 * 2. 更新记录时, 也不需设置 updateTime, 会自动更新时间 {@link com.fkhwl.starter.autoconfigure.mybatis.handler.TimeMetaHandler}
 * 注意: 子类不能使用 builder 模式! 子类不能使用 builder 模式! 子类不能使用 builder 模式!
 *
 * @param <T> parameter
 * @param <M> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.22 22:49
 * @since 1.0.0
 */
public abstract class BaseWithTimePO<T extends Serializable, M extends Model<M>> extends BasePO<T, M> implements AuditTime {
    /** serialVersionUID */
    private static final long serialVersionUID = -8444534935163656524L;

    /** 创建时间 (公共字段) */
    @TableField(value = CREATE_TIME, fill = FieldFill.INSERT)
    private Date createTime;
    /** 最后更新时间 (公共字段) */
    @TableField(value = UPDATE_TIME, fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * Gets create time *
     *
     * @return the create time
     * @since 1.0.0
     */
    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * Sets create time *
     *
     * @param createTime create time
     * @return the create time
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public M setCreateTime(Date createTime) {
        this.createTime = createTime;
        return (M) this;
    }

    /**
     * Sets update time *
     *
     * @param updateTime update time
     * @return the update time
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public M setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return (M) this;
    }

    /**
     * Gets update time *
     *
     * @return the update time
     * @since 1.0.0
     */
    @Override
    public Date getUpdateTime() {
        return this.updateTime;
    }

}
