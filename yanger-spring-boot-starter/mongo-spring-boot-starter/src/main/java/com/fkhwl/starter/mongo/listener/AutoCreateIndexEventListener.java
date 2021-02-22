package com.fkhwl.starter.mongo.listener;

import com.google.common.collect.Sets;

import com.fkhwl.starter.core.util.StringUtils;
import com.fkhwl.starter.mongo.datasource.MongoDataSource;
import com.fkhwl.starter.mongo.mapper.MongoPO;
import com.fkhwl.starter.mongo.util.IndexUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.util.Set;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 自动生成索引, 解决直接使用 collection name 时不创建索引的问题  </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 14:27
 * @since 1.0.0
 */
@Slf4j
public class AutoCreateIndexEventListener extends AbstractMongoEventListener<MongoPO<?, ?>> {
    /** 缓存已处理过的 collection */
    private final Set<String> initIndexedCollectionNames = Sets.newHashSetWithExpectedSize(8);

    /**
     * 判断 DB 是否存在指定的 collection name, 不存在则创建索引
     *
     * @param event event
     * @see MongoPO
     * @since 1.0.0
     */
    @Override
    public void onBeforeSave(@NotNull BeforeSaveEvent<MongoPO<?, ?>> event) {
        MongoPO<?, ?> source = event.getSource();

        // 具体操作时传入的 collection name
        String collectionName = event.getCollectionName();
        if (StringUtils.isBlank(collectionName)) {
            // 获取原始的 collection name
            collectionName = MongoPO.collection(source.getClass());
            if (StringUtils.isBlank(collectionName)) {
                collectionName = StringUtils.humpToUnderline(source.getClass().getSimpleName());
            }
        }

        // 已经处理过则退出
        if (!this.initIndexedCollectionNames.contains(collectionName)) {
            // fixme-dong4j : (2020.04.12 08:01) [在分表的情况下, 获取的有可能是老的数据源, 导致在原来的 db 中创建了新表,
            //  且不会在新的 db 中再次创建索引]
            // 暂时想到的解决方法是使用 AOP.
            MongoTemplate mongoTemplate = MongoDataSource.getDataSource(source.getClass());

            boolean exists = mongoTemplate.collectionExists(collectionName);
            if (!exists) {
                // 为指定的 collectionName 创建索引
                IndexUtils.createIndexes(mongoTemplate, source.getClass(), collectionName);
            }

            this.initIndexedCollectionNames.add(collectionName);
        }
    }

}
