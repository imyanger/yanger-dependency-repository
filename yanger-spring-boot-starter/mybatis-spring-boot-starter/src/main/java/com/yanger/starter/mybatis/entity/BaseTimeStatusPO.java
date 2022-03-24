package com.yanger.starter.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yanger.starter.mybatis.enums.StatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 逻辑删除基础对象
 *     1. 新增记录时, 不需要以下公共字段, 将根据注解自动生成;
 *     2. 更新记录时, 也不需设置 updateTime, 会自动更新时间 {@link com.yanger.starter.mybatis.handler.TimeMetaHandler}
 *     注意: 子类不能使用 builder 模式! 子类不能使用 builder 模式! 子类不能使用 builder 模式!
 *     eg: public class User extends BaseTimeStatusPO<Long, User> {}
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public abstract class BaseTimeStatusPO<T extends Serializable, M extends Model<M>> extends BasePO<T, M> implements DataStatus, AuditTime {

    private static final long serialVersionUID = 7951121625400869460L;

    /** 状态字段: 正常(1)，禁用(0)，已删除(-1) */
    @TableField(value = DataStatus.STATUS)
    private StatusEnum status = StatusEnum.NORMAL;

    /** 创建时间 (公共字段) */
    @TableField(value = AuditTime.CREATE_TIME, fill = FieldFill.INSERT)
    private Date createTime;

    /** 最后更新时间 (公共字段) */
    @TableField(value = AuditTime.UPDATE_TIME, fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * Gets status
     * @return the status
     */
    @Override
    public StatusEnum getStatus() {
        return this.status;
    }

    /**
     * Sets deleted
     * @param status status
     * @return the status
     */
    @SuppressWarnings("unchecked")
    public M setStatus(StatusEnum status) {
        this.status = status;
        return (M) this;
    }

    /**
     * Gets create time
     * @return the create time
     */
    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * Sets create time
     * @param createTime create time
     * @return the create time
     */
    @SuppressWarnings("unchecked")
    public M setCreateTime(Date createTime) {
        this.createTime = createTime;
        return (M) this;
    }

    /**
     * Gets update time
     * @return the update time
     */
    @Override
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * Sets update time
     * @param updateTime update time
     * @return the update time
     */
    @SuppressWarnings("unchecked")
    public M setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return (M) this;
    }

}
