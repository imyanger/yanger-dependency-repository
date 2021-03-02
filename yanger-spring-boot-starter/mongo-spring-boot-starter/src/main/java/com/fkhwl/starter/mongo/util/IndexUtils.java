package com.fkhwl.starter.mongo.util;

import com.google.common.collect.Lists;

import com.fkhwl.starter.mongo.factory.MongoProviderFactory;
import com.fkhwl.starter.mongo.index.CustomMongoPersistentEntityIndexResolver;
import com.yanger.starter.basic.context.EarlySpringContext;
import com.yanger.tools.web.tools.ObjectUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Arrays;
import java.util.List;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.05 21:11
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
@UtilityClass
public class IndexUtils {
    /** mongoMappingContext */
    private static MongoMappingContext mongoMappingContext;

    /**
     * 为指定的实体添加索引:
     * 1. 如果传 collectionName, 将使用 clazz 的 collectionName
     *
     * @param mongoTemplate  mongo template
     * @param clazz          clazz
     * @param collectionName collection name
     */
    public static void createIndexes(@NotNull MongoTemplate mongoTemplate, Class<?> clazz, String... collectionName) {

        List<String> collectionNames = Lists.newArrayList();
        if (ObjectUtils.isEmpty(collectionName)) {
            collectionNames.add(MongoProviderFactory.collectionName(clazz));
        } else {
            Arrays.stream(collectionName).filter(StringUtils::isNotBlank).forEach(collectionNames::add);
        }

        if (mongoMappingContext == null) {
            mongoMappingContext = EarlySpringContext.getInstance(MongoMappingContext.class);
        }

        IndexResolver resolver = new CustomMongoPersistentEntityIndexResolver(mongoMappingContext);

        try {
            collectionNames.forEach(collection -> {
                IndexOperations indexOps = mongoTemplate.indexOps(collection);
                resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);
            });
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
    }
}
