package com.yanger.starter.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yanger.starter.mybatis.enums.StatusEnum;

import java.io.Serializable;

/**
 * 数据状态基础字段
 *     注意: 子类不能使用 builder 模式! 子类不能使用 builder 模式! 子类不能使用 builder 模式!
 *     eg: public class User extends BaseStatusPO<Long, User> {}
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public abstract class BaseStatusPO<T extends Serializable, M extends Model<M>> extends BasePO<T, M> implements DataStatus {

    private static final long serialVersionUID = 7951121625400869460L;

    /** 状态字段: 正常(1)，禁用(0)，已删除(-1) */
    @TableField(value = DataStatus.STATUS)
    private StatusEnum status = StatusEnum.NORMAL;

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

}
