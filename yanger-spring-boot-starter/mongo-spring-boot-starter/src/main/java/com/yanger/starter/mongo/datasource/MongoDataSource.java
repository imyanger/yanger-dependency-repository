package com.yanger.starter.mongo.datasource;

import com.yanger.starter.mongo.constant.MongoConstant;
import com.yanger.starter.mongo.core.MongoBean;
import com.yanger.starter.mongo.factory.MongoProviderFactory;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * mongodb 多数据源配置
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class MongoDataSource {

    /**
     * 获取默认数据源
     *
     * @return the mongo template
     */
    public static MongoTemplate getDataSource() {
        return getDataSource(MongoConstant.DEFAULT_DATASOURCE);
    }

    /**
     * 根据数据源名称获取数据源
     *
     * @param datasource the datasource
     * @return the mongo template
     */
    public static MongoTemplate getDataSource(String datasource) {
        return MongoProviderFactory.getConfigureWithMongoTemplate(datasource);
    }

    /**
     * 通过实体类获取数据源, 如果实体没有使用 @MongoCollection 标识, 将返回默认数据源
     *
     * @param claz the claz
     * @return the mongo template
     */
    public static MongoTemplate getDataSource(@NotNull Class<?> claz) {
        MongoBean mongoBean = MongoProviderFactory.getAnnotation2BeanMap(claz.getName());
        if (mongoBean == null) {
            return getDataSource();
        }
        return mongoBean.getMongoTemplate();
    }

    /**
     * Gets mongo transaction template *
     *
     * @param mongoTemplate mongo template
     * @return the mongo transaction manager
     */
    public static TransactionTemplate getMongoTransactionTemplate(MongoTemplate mongoTemplate) {
        return MongoProviderFactory.getMongoTransactionTemplate(mongoTemplate);
    }
}
