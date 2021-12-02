package com.yanger.starter.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.yanger.starter.basic.entity.IBaseEntity;

import java.io.*;

import lombok.Builder;

/**
 * 包含 id 的基础对象
 *     1. 新增记录时, 不需要以下公共字段, 将根据注解自动生成;
 *     2. 更新记录时, 也不需设置 updateTime, 会自动更新时间 {@link com.yanger.starter.mybatis.handler.TimeMetaHandler}
 *     注意: 子类不能使用 builder 模式! 子类不能使用 builder 模式! 子类不能使用 builder 模式!
 *     eg: public class User extends BasePO<Long, User> {}
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public abstract class BasePO<T extends Serializable, M extends Model<M>> extends Model<M> implements IBaseEntity<T> {

    private static final long serialVersionUID = -3685429878576720045L;

    /** Id */
    @TableId(value = ID)
    private T id;

    /**
     * Active Record 模式需要满足 2 个要求:
     * 1. 重写 pkVal();
     * 2. 存在 XxxDao;
     *
     * @return the serializable
     */
    @Override
    public T pkVal() {
        return this.getId();
    }

    /**
     * Pk val serializable
     *
     * @return the serializable
     */
    @Override
    public T getId() {
        return this.id;
    }

    /**
     * 必须使用 @Builder 注解, 不然在使用 wrapper 转换时 id 会丢失
     *
     * @param id id
     * @return the id
     */
    @Builder
    @SuppressWarnings("unchecked")
    public M setId(T id) {
        this.id = id;
        return (M) this;
    }

}
