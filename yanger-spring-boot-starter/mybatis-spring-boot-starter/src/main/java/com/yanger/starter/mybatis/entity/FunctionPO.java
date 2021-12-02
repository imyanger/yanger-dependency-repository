package com.yanger.starter.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yanger.starter.mybatis.enums.DeleteEnum;
import com.yanger.starter.mybatis.enums.StatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 时间和逻辑删除的基础字段
 *     1. 新增记录时, 不需要以下公共字段, 将根据注解自动生成;
 *     2. 更新记录时, 也不需设置 updateTime, 会自动更新时间 {@link com.yanger.starter.mybatis.handler.TimeMetaHandler}
 *     注意: 子类不能使用 builder 模式! 子类不能使用 builder 模式! 子类不能使用 builder 模式!
 *     eg: public class User extends FunctionPO<Long, User> {}
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public abstract class FunctionPO<T extends Serializable, M extends Model<M>> extends BasePO<T, M>
        implements LogicDelete, AuditTime, DataStatus {

    private static final long serialVersionUID = 7951121625400869460L;

    /** 逻辑删除标识: 逻辑已删除值(1); 逻辑未删除值(0) 默认为 0 */
    @TableLogic
    @TableField(value = LogicDelete.DELETED)
    private DeleteEnum deleted = DeleteEnum.N;

    /** 创建时间 (公共字段) */
    @TableField(value = AuditTime.CREATE_TIME, fill = FieldFill.INSERT)
    private Date createTime;

    /** 最后更新时间 (公共字段) */
    @TableField(value = AuditTime.UPDATE_TIME, fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 状态字段: 正常(1)，禁用(0)，已删除(1) */
    @TableField(value = DataStatus.STATUS)
    private StatusEnum status = StatusEnum.NORMAL;

    /**
     * Gets deleted *
     *
     * @return the deleted
     */
    @Override
    public DeleteEnum getDeleted() {
        return this.deleted;
    }

    /**
     * Sets deleted *
     *
     * @param deleted deleted
     * @return the deleted
     */
    @SuppressWarnings("unchecked")
    public M setDeleted(DeleteEnum deleted) {
        this.deleted = deleted;
        return (M) this;
    }

    /**
     * Gets create time *
     *
     * @return the create time
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
     */
    @SuppressWarnings("unchecked")
    public M setCreateTime(Date createTime) {
        this.createTime = createTime;
        return (M) this;
    }

    /**
     * Gets update time *
     *
     * @return the update time
     */
    @Override
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * Sets update time *
     *
     * @param updateTime update time
     * @return the update time
     */
    @SuppressWarnings("unchecked")
    public M setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return (M) this;
    }

    /**
     * Gets status
     *
     * @return the status
     */
    @Override
    public StatusEnum getStatus() {
        return this.status;
    }

    /**
     * Sets deleted *
     *
     * @param status status
     * @return the status
     */
    @SuppressWarnings("unchecked")
    public M setStatus(StatusEnum status) {
        this.status = status;
        return (M) this;
    }

}
