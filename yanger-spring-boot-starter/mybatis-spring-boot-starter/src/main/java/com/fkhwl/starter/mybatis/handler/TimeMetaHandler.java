package com.fkhwl.starter.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 处理新增和更新的基础数据填充,配合 BaseEntity 和 MyBatisPlusConfig 使用 </p>
 * {@link com.fkhwl.starter.common.base.BasePO}
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.26 20:16
 * @since 1.0.0
 */
@Slf4j
public class TimeMetaHandler implements MetaObjectHandler {
    /**
     * 新增数据执行
     *
     * @param metaObject the meta object
     * @since 1.0.0
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    /**
     * 更新数据执行
     *
     * @param metaObject the meta object
     * @since 1.0.0
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}
