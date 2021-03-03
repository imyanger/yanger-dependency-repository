package com.yanger.starter.mongo.convert;

import com.yanger.starter.mongo.annotation.MongoCollection;
import com.yanger.starter.mongo.entity.CustomBasicMongoPersistentEntity;
import com.yanger.starter.mongo.enums.CollcetionType;
import com.yanger.starter.mongo.enums.FieldConvert;
import com.yanger.starter.mongo.index.CustomMongoPersistentEntityIndexCreator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.CachingMongoPersistentProperty;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.util.Optional;

/**
 * @Description 字段名转换配置项类
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class CustomMongoMappingContext extends MongoMappingContext {

    /** DEFAULT_NAMING_STRATEGY */
    private static final FieldNamingStrategy DEFAULT_NAMING_STRATEGY = new SnakeCaseFieldNamingStrategy();

    /** fieldNamingStrategy */
    private static FieldNamingStrategy fieldNamingStrategy = DEFAULT_NAMING_STRATEGY;

    /** OFF 关闭（默认）; UNDERSCORE 下划线 */
    private static FieldConvert fieldConvert = FieldConvert.OFF;

    /**
     * Gets field convert.
     *
     * @return the field convert
     */
    @Contract(pure = true)
    public static FieldConvert getFieldConvert() {
        return fieldConvert;
    }

    /**
     * Sets field convert.
     *
     * @param fieldConvert the field convert
     */
    public static void setFieldConvert(FieldConvert fieldConvert) {
        CustomMongoMappingContext.fieldConvert = fieldConvert;
    }

    /**
     * 重写添加 mongod 实体逻辑:
     * 如果实体被 @MongoCollection 标识, 且 type = Sharding 则返回 null, 在对实体遍历创建索引时将被忽略.
     * 用于解决在分表情况下会生成一张无用的表.
     *
     * @param type type
     * @return the optional
     * @see CustomMongoPersistentEntityIndexCreator#CustomMongoPersistentEntityIndexCreator CustomMongoPersistentEntityIndexCreator
     *     #CustomMongoPersistentEntityIndexCreator
     */
    @NotNull
    @Override
    protected Optional<BasicMongoPersistentEntity<?>> addPersistentEntity(@NotNull Class<?> type) {
        MongoCollection mongoCollection = AnnotationUtils.findAnnotation(type, MongoCollection.class);
        if (mongoCollection == null || mongoCollection.type() == CollcetionType.SHARDING) {
            return Optional.empty();
        }

        return this.addPersistentEntity(ClassTypeInformation.from(type));
    }

    /**
     * 修改没有配置 @MongoCollection value 属性时的 collection name 生成规则
     *
     * @param <T>             parameter
     * @param typeInformation type information
     * @return the basic mongo persistent entity
     */
    @NotNull
    @Override
    protected <T> BasicMongoPersistentEntity<T> createPersistentEntity(@NotNull TypeInformation<T> typeInformation) {
        return new CustomBasicMongoPersistentEntity<>(typeInformation);
    }

    /**
     * 设置字段转换处理器 (大小写, 驼峰, 下划线等)
     *
     * @param fieldNamingStrategy field naming strategy
     */
    @Override
    public void setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        CustomMongoMappingContext.fieldNamingStrategy = fieldNamingStrategy == null ? DEFAULT_NAMING_STRATEGY : fieldNamingStrategy;
        if (fieldNamingStrategy instanceof SnakeCaseFieldNamingStrategy) {
            fieldConvert = FieldConvert.UNDERSCORE;
        }
    }

    /**
     * Create persistent property mongo persistent property.
     *
     * @param property         the property
     * @param owner            the owner
     * @param simpleTypeHolder the simple type holder
     * @return the mongo persistent property
     */
    @NotNull
    @Override
    public MongoPersistentProperty createPersistentProperty(@NotNull Property property,
                                                            @NotNull BasicMongoPersistentEntity<?> owner,
                                                            @NotNull SimpleTypeHolder simpleTypeHolder) {
        return new CachingMongoPersistentProperty(property, owner, simpleTypeHolder, fieldNamingStrategy);
    }
}
