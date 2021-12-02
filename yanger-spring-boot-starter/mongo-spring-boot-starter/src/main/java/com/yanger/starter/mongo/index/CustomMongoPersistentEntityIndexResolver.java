package com.yanger.starter.mongo.index;

import com.yanger.starter.mongo.annotation.MongoCollection;
import com.yanger.starter.mongo.annotation.MongoColumn;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.util.TypeInformation;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 处理 {@link MongoCollection} 的 复合索引和 {@link MongoColumn} 字段索引
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class CustomMongoPersistentEntityIndexResolver extends MongoPersistentEntityIndexResolver {
    /** Mapping context */
    private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

    /**
     * Create new {@link MongoPersistentEntityIndexResolver}.
     *
     * @param mappingContext must not be {@literal null}.
     */
    @Contract("null -> fail")
    public CustomMongoPersistentEntityIndexResolver(MappingContext<? extends MongoPersistentEntity<?>,
        MongoPersistentProperty> mappingContext) {
        super(mappingContext);
        this.mappingContext = mappingContext;
    }


    /**
     * 返回 mongo 实体的 IndexDefinitionHolder 迭代器, 用于创建索引
     *
     * @param typeInformation type information
     * @return the iterable
     */
    @NotNull
    @Override
    public Iterable<? extends IndexDefinitionHolder> resolveIndexFor(@NotNull TypeInformation<?> typeInformation) {
        return this.resolveIndexForEntity(this.mappingContext.getRequiredPersistentEntity(typeInformation));
    }

    /**
     * 父类默认扫描 {@link Indexed}, 当只使用 {@link MongoColumn} 且没有配置 index 字段时, 会导致生成一个空的 IndexDefinitionHolder 从而导致 NPE,
     * 因此这里对最终的 index 列表做一次排空处理.
     *
     * @param root root
     * @return the list
     */
    @Override
    @NotNull
    public List<IndexDefinitionHolder> resolveIndexForEntity(MongoPersistentEntity<?> root) {
        List<IndexDefinitionHolder> indexInformation = super.resolveIndexForEntity(root);
        return indexInformation.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 复合索引处理, 因为使用了 {@link MongoCollection} 复合注解, 会解析 {@link CompoundIndexes} 的 def, 如果为空将会抛出异常, 这里通过覆写原有逻辑,
     * 实现使用 {@link MongoCollection} 复合注解时未定义 def 时不创建索引.
     *
     * @param dotPath            dot path
     * @param fallbackCollection fallback collection
     * @param entity             entity
     * @return the list
     */
    @NotNull
    @Override
    protected List<IndexDefinitionHolder> createCompoundIndexDefinitions(@NotNull String dotPath,
                                                                         @NotNull String fallbackCollection,
                                                                         @NotNull MongoPersistentEntity<?> entity) {

        List<IndexDefinitionHolder> indexDefinitions = new ArrayList<>();
        CompoundIndexes indexes = entity.findAnnotation(CompoundIndexes.class);

        if (indexes != null) {
            indexDefinitions = Arrays.stream(indexes.value())
                .map(index -> this.createCompoundIndexDefinition(dotPath, fallbackCollection, index, entity))
                .collect(Collectors.toList());
        }

        CompoundIndex index = entity.findAnnotation(CompoundIndex.class);

        // 使用 MongoCollection 只有配置了 def 属性才创建索引
        if (index != null && StringUtils.isNotBlank(index.def())) {
            indexDefinitions.add(this.createCompoundIndexDefinition(dotPath, fallbackCollection, index, entity));
        }

        return indexDefinitions;
    }

    /**
     * 处理字段索引
     *
     * @param dotPath            dot path
     * @param collection         collection
     * @param persistentProperty persistent property
     * @return the index definition holder
     */
    @Override
    @Nullable
    protected IndexDefinitionHolder createIndexDefinition(@NotNull String dotPath,
                                                          @NotNull String collection,
                                                          @NotNull MongoPersistentProperty persistentProperty) {

        Indexed index = persistentProperty.findAnnotation(Indexed.class);

        // 使用 MongoColumn 时, 只有配置了 index 属性才会创建索引
        if (index == null || StringUtils.isBlank(index.name())) {
            return null;
        }

        return super.createIndexDefinition(dotPath, collection, persistentProperty);
    }


    /**
     * 处理 HashIndexed 索引
     *
     * @param dotPath            dot path
     * @param collection         collection
     * @param persistentProperty persistent property
     * @return the index definition holder
     */
    @Override
    @Nullable
    protected IndexDefinitionHolder createHashedIndexDefinition(@NotNull String dotPath,
                                                                @NotNull String collection,
                                                                MongoPersistentProperty persistentProperty) {

        return super.createHashedIndexDefinition(dotPath, collection, persistentProperty);
    }


    /**
     * 处理 GeoSpatialIndexed 索引
     *
     * @param dotPath            dot path
     * @param collection         collection
     * @param persistentProperty persistent property
     * @return the index definition holder
     */
    @Override
    @Nullable
    protected IndexDefinitionHolder createGeoSpatialIndexDefinition(@NotNull String dotPath,
                                                                    @NotNull String collection,
                                                                    @NotNull MongoPersistentProperty persistentProperty) {

        GeoSpatialIndexed index = persistentProperty.findAnnotation(GeoSpatialIndexed.class);

        if (index == null || StringUtils.isBlank(index.name())) {
            return null;
        }
        return super.createGeoSpatialIndexDefinition(dotPath, collection, persistentProperty);
    }

}
