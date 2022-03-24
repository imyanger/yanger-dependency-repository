package com.yanger.starter.mongo.entity;

import com.yanger.tools.general.tools.StringTools;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.StringUtils;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class CustomBasicMongoPersistentEntity<T> extends BasicMongoPersistentEntity<T> {

    /** Collection */
    private final String collection;

    /**
     * 自定义 collection name, 如果没有指定, 则使用驼峰转下划线命名
     * @param typeInformation must not be {@literal null}.
     */
    public CustomBasicMongoPersistentEntity(TypeInformation<T> typeInformation) {
        super(typeInformation);

        Class<?> rawType = typeInformation.getType();
        String fallback = StringTools.camelCaseToUnderline(rawType.getSimpleName());

        if (this.isAnnotationPresent(Document.class)) {
            Document document = this.getRequiredAnnotation(Document.class);
            this.collection = StringUtils.hasText(document.collection()) ? document.collection() : fallback;
        } else {
            this.collection = fallback;
        }
    }

    /**
     * Gets collection *
     * @return the collection
     */
    @NotNull
    @Override
    public String getCollection() {
        return this.collection;
    }
}
