package com.yanger.starter.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理新增和更新的基础数据填充, 配合 BaseEntity 和 MyBatisPlusConfig 使用
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class TimeMetaHandler implements MetaObjectHandler {

    /**
     * 新增数据执行
     *
     * @param metaObject the meta object
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
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}
