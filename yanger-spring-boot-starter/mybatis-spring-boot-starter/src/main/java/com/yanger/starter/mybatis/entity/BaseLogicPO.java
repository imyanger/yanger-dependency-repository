package com.yanger.starter.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yanger.starter.mybatis.enums.DeleteEnum;

import java.io.Serializable;

/**
 * 逻辑删除基础对象
 *     1. 新增记录时, 不需要以下公共字段, 将根据注解自动生成;
 *     2. 更新记录时, 也不需设置 updateTime, 会自动更新时间 {@link com.yanger.starter.mybatis.handler.TimeMetaHandler}
 *     注意: 子类不能使用 builder 模式! 子类不能使用 builder 模式! 子类不能使用 builder 模式!
 *     eg: public class User extends BaseLogicPO<Long, User> {}
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public abstract class BaseLogicPO<T extends Serializable, M extends Model<M>> extends BasePO<T, M> implements LogicDelete {

    private static final long serialVersionUID = 7951121625400869460L;

    /** 逻辑删除标识: 逻辑未删除值(0)，逻辑已删除值(1)，默认为 0 */
    @TableLogic
    @TableField(value = LogicDelete.DELETED)
    private DeleteEnum deleted = DeleteEnum.N;

    /**
     * Gets deleted
     * @return the deleted
     */
    @Override
    public DeleteEnum getDeleted() {
        return this.deleted;
    }

    /**
     * Sets deleted
     * @param deleted deleted
     * @return the deleted
     */
    @SuppressWarnings("unchecked")
    public M setDeleted(DeleteEnum deleted) {
        this.deleted = deleted;
        return (M) this;
    }

}
