package com.fkhwl.starter.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fkhwl.starter.common.enums.DeleteEnum;

import java.io.*;

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
public abstract class BaseWithLogicPO<T extends Serializable, M extends Model<M>> extends BasePO<T, M> implements LogicDelete {

    /** serialVersionUID */
    private static final long serialVersionUID = 7951121625400869460L;

    /** 逻辑删除标识: 逻辑已删除值(1); 逻辑未删除值(0) 默认为 0 */
    @TableLogic
    @TableField(value = DELETED)
    private DeleteEnum deleted;

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

}
