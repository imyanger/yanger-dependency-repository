package com.yanger.starter.mongo.handler;

import com.yanger.starter.mongo.mapper.MongoPO;
import com.yanger.starter.mongo.reflection.MetaObject;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 默认的审计字段填充 (createTime 和 updateTime)
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
public class DefaultTimeMetaHandler implements MetaObjectHandler {

    /**
     * 新增数据执行
     *
     * @param metaObject meta object
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName(MongoPO.JAVA_FIELD_CREATE_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(MongoPO.JAVA_FIELD_UPDATE_TIME, LocalDateTime.now(), metaObject);
    }

    /**
     * 更新数据执行
     *
     * @param metaObject meta object
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName(MongoPO.JAVA_FIELD_UPDATE_TIME, LocalDateTime.now(), metaObject);
    }
}
