package com.yanger.starter.mongo.index;

import com.yanger.starter.mongo.annotation.MongoColumn;

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
 * @Description 自定义索引创建策略, 用于处理 {@link MongoColumn} 组合注解
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
public class CustomMongoPersistentEntityIndexCreator extends MongoPersistentEntityIndexCreator {


    /**
     * Consumer mongo persistent entity index creator
     *
     * @param mappingContext          mapping context
     * @param indexOperationsProvider index operations provider
     * @throws DataIntegrityViolationException 当重复创建索引时抛出
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
