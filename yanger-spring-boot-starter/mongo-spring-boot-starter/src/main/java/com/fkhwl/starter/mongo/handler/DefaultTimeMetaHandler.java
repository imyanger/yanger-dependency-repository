package com.fkhwl.starter.mongo.handler;

import com.fkhwl.starter.core.metadata.MetaObjectHandler;
import com.fkhwl.starter.core.reflection.MetaObject;
import com.fkhwl.starter.mongo.mapper.MongoPO;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 默认的审计字段填充 (createTime 和 updateTime) </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.26 20:16
 * @since 1.0.0
 */
@Slf4j
public class DefaultTimeMetaHandler implements MetaObjectHandler {

    /**
     * 新增数据执行
     *
     * @param metaObject meta object
     * @since 1.0.0
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
     * @since 1.0.0
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName(MongoPO.JAVA_FIELD_UPDATE_TIME, LocalDateTime.now(), metaObject);
    }
}
