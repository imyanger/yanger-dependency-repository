package com.fkhwl.starter.mongo.entity;

import com.fkhwl.starter.core.util.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.TypeInformation;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.07 19:05
 * @since 1.0.0
 */
public class CustomBasicMongoPersistentEntity<T> extends BasicMongoPersistentEntity<T> {
    /** Collection */
    private final String collection;

    /**
     * 自定义 collection name, 如果没有指定, 则使用驼峰转下划线命名
     *
     * @param typeInformation must not be {@literal null}.
     * @since 1.0.0
     */
    public CustomBasicMongoPersistentEntity(TypeInformation<T> typeInformation) {
        super(typeInformation);

        Class<?> rawType = typeInformation.getType();
        String fallback = StringUtils.humpToUnderline(rawType.getSimpleName());

        if (this.isAnnotationPresent(Document.class)) {
            Document document = this.getRequiredAnnotation(Document.class);
            this.collection = StringUtils.hasText(document.collection()) ? document.collection() : fallback;
        } else {
            this.collection = fallback;
        }
    }

    /**
     * Gets collection *
     *
     * @return the collection
     * @since 1.0.0
     */
    @NotNull
    @Override
    public String getCollection() {
        return this.collection;
    }
}
