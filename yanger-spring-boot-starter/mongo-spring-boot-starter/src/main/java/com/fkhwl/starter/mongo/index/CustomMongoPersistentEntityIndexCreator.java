package com.fkhwl.starter.mongo.index;

import com.fkhwl.starter.mongo.annotation.MongoColumn;

import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContextEvent;
import org.springframework.data.mongodb.core.index.IndexOperationsProvider;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexCreator;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 自定义索引创建策略, 用于处理 {@link MongoColumn} 组合注解 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.06 12:37
 * @since 1.0.0
 */
@Slf4j
public class CustomMongoPersistentEntityIndexCreator extends MongoPersistentEntityIndexCreator {


    /**
     * Consumer mongo persistent entity index creator
     *
     * @param mappingContext          mapping context
     * @param indexOperationsProvider index operations provider
     * @throws DataIntegrityViolationException 当重复创建索引时抛出
     * @since 1.0.0
     */
    public CustomMongoPersistentEntityIndexCreator(MongoMappingContext mappingContext,
                                                   IndexOperationsProvider indexOperationsProvider)
        throws DataIntegrityViolationException {

        super(mappingContext, indexOperationsProvider, new CustomMongoPersistentEntityIndexResolver(mappingContext));
    }

    /**
     * 重写 MappingContextEvent 事件处理器, 此方法只有在开启自动创建索引时才会被调用
     *
     * @param event event
     * @since 1.0.0
     */
    @Override
    public void onApplicationEvent(@NotNull MappingContextEvent<?, ?> event) {
        PersistentEntity<?, ?> entity = event.getPersistentEntity();

        // 获得 Mongo 实体类
        if (entity instanceof MongoPersistentEntity) {
            log.debug("已注册的 mongo entity: {}", entity.getName());
        }

        super.onApplicationEvent(event);
    }
}
