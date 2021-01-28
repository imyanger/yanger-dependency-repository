package com.fkhwl.starter.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fkhwl.starter.common.enums.DeleteEnum;

import java.io.*;
import java.util.Date;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @param <T> parameter
 * @param <M> parameter
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.22 13:58
 * @since 1.0.0
 */
public abstract class BaseExtendPO<T extends Serializable, M extends Model<M>> extends BasePO<T, M>
    implements LogicDelete, AuditTime {

    /** serialVersionUID */
    private static final long serialVersionUID = 7951121625400869460L;

    /** 逻辑删除标识: 逻辑已删除值(1); 逻辑未删除值(0) 默认为 0 */
    @TableLogic
    @TableField(value = DELETED)
    private DeleteEnum deleted;
    /** 创建时间 (公共字段) */
    @TableField(value = CREATE_TIME, fill = FieldFill.INSERT)
    private Date createTime;
    /** 最后更新时间 (公共字段) */
    @TableField(value = UPDATE_TIME, fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * Gets deleted *
     *
     * @return the deleted
     * @since 1.0.0
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
     * @since 1.0.0
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
     * Gets update time *
     *
     * @return the update time
     * @since 1.0.0
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
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    public M setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return (M) this;
    }
}
