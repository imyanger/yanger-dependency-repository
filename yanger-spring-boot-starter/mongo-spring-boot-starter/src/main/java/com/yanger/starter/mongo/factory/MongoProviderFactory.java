package com.yanger.starter.mongo.factory;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yanger.starter.mongo.core.MongoBean;
import com.yanger.starter.mongo.exception.MongoException;
import com.yanger.tools.web.tools.ObjectUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 管理 mongoTemplate
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
public class MongoProviderFactory {
    /** datasourceName <--> MongoTemplate */
    private static final Map<String, MongoTemplate> CONFIGURE_WITH_MONGO_TEMPLATES = new ConcurrentHashMap<>();
    /** MONGO_TRANSACTION_TEMPLATE */
    private static final Map<MongoTemplate, TransactionTemplate> MONGO_TRANSACTION_TEMPLATE = new ConcurrentHashMap<>();
    /** className <--> MongoBean */
    private static final Map<String, MongoBean> ANNOTATION_TO_BEAN_MAP = new ConcurrentHashMap<>();

    /**
     * Gets configure with mongo templates.
     *
     * @return the configure with mongo templates
     */
    @Contract(pure = true)
    public static Map<String, MongoTemplate> getConfigureWithMongoTemplates() {
        return CONFIGURE_WITH_MONGO_TEMPLATES;
    }

    /**
     * Gets mongo transaction manager *
     *
     * @return the mongo transaction manager
     */
    @Contract(pure = true)
    public static Map<MongoTemplate, TransactionTemplate> getMongoTransactionTemplate() {
        return MONGO_TRANSACTION_TEMPLATE;
    }

    /**
     * Gets entity withmongo templates.
     *
     * @return the entity withmongo templates
     */
    @Contract(pure = true)
    public static Map<String, MongoBean> getAnnotationToBeanMap() {
        return ANNOTATION_TO_BEAN_MAP;
    }

    /**
     * Add mongo template.
     *
     * @param datasourceName the datasource name
     * @param mongoTemplate  the mongo template
     */
    public static synchronized void addConfigureWithMongoTemplate(String datasourceName, MongoTemplate mongoTemplate) {
        Assert.isTrue(!CONFIGURE_WITH_MONGO_TEMPLATES.containsKey(datasourceName),
                      StringUtils.format("datasourceName = [{}] 已存在", datasourceName));
        CONFIGURE_WITH_MONGO_TEMPLATES.put(datasourceName, mongoTemplate);
        log.info("初始化 MongoTemplate: [{}] --> [{}]", datasourceName, mongoTemplate);
    }

    /**
     * Gets mongo template.
     *
     * @param datasourceName the datasource name
     * @return the mongo template
     */
    public static MongoTemplate getConfigureWithMongoTemplate(String datasourceName) {
        if (StringUtils.isNotBlank(datasourceName)) {
            return CONFIGURE_WITH_MONGO_TEMPLATES.get(datasourceName);
        } else {
            throw new MongoException("datasourceName is blank!");
        }
    }

    /**
     * Add mongo transaction manager *
     *
     * @param mongoTemplate           mongo template
     * @param mongoTransactionManager mongo transaction manager
     */
    public static synchronized void addMongoTransactionTemplate(MongoTemplate mongoTemplate, TransactionTemplate mongoTransactionManager) {
        Assert.isTrue(!MONGO_TRANSACTION_TEMPLATE.containsKey(mongoTemplate),
                      StringUtils.format("mongoTemplate = [{}] 已存在", mongoTemplate));
        MONGO_TRANSACTION_TEMPLATE.put(mongoTemplate, mongoTransactionManager);
        log.info("初始化 mongoTransactionManager: [{}] --> [{}]", mongoTemplate, mongoTransactionManager);
    }

    /**
     * Gets mongo transaction manager *
     *
     * @param mongoTemplate mongo template
     * @return the mongo template
     */
    public static TransactionTemplate getMongoTransactionTemplate(MongoTemplate mongoTemplate) {
        if (ObjectUtils.isNotNull(mongoTemplate)) {
            return MONGO_TRANSACTION_TEMPLATE.get(mongoTemplate);
        } else {
            throw new MongoException("mongoTemplate is blank!");
        }
    }

    /**
     * Add mongo template.
     *
     * @param className the class name
     * @param mongoBean the mongo bean
     */
    public static synchronized void addAnnotation2BeanMap(String className, MongoBean mongoBean) {
        Assert.isTrue(!ANNOTATION_TO_BEAN_MAP.containsKey(className), StringUtils.format("className = [{}] 已存在", className));
        log.debug("扫描到 Mongo Entity: [{}]", className);
        ANNOTATION_TO_BEAN_MAP.put(className, mongoBean);
    }

    /**
     * Collection name string.
     *
     * @param className the class name
     * @return the string
     */
    public static String collectionName(String className) {
        return getAnnotation2BeanMap(className).getCollectionName();
    }

    /**
     * Gets mongo template.
     *
     * @param className the class name
     * @return the mongo template
     */
    public static MongoBean getAnnotation2BeanMap(String className) {
        if (StringUtils.isNotBlank(className)) {
            return ANNOTATION_TO_BEAN_MAP.get(className);
        } else {
            throw new MongoException("className is blank!");
        }
    }

    /**
     * Collection name string.
     *
     * @param claz the claz
     * @return the string
     */
    public static String collectionName(@NotNull Class<?> claz) {
        return collectionName(claz.getName());
    }
}
