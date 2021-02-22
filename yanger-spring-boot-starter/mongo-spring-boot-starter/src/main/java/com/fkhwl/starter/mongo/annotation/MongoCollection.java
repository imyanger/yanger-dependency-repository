package com.fkhwl.starter.mongo.annotation;

import com.fkhwl.starter.mongo.enums.CollcetionType;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: ${description}</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 18:16
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@CompoundIndex
@Document
public @interface MongoCollection {

    /**
     * Value string.
     *
     * @return the string
     * @since 1.0.0
     */
    @AliasFor(value = "collection", annotation = Document.class)
    String value() default "";

    /**
     * Desc string.
     *
     * @return the string
     * @since 1.0.0
     */
    String desc() default "";

    /**
     * Datasource string.
     *
     * @return the string
     * @since 1.0.0
     */
    String datasource() default "";

    /**
     * 集合类型, 默认为普通类型, 默认会创建索引, SHARDING 类型不会自动创建索引
     *
     * @return the collcetion type
     * @since 1.0.0
     */
    CollcetionType type() default CollcetionType.ORDINARY;

    /**
     * JSON 格式的实际索引定义或解析为 JSON 字符串或 org.bson.Document 的模板表达式。
     * JSON 文档的键是要索引的字段, 值定义索引方向（1表示升序, -1表示降序）。
     * 如果在嵌套文档上保留为空, 则将对整个文档编制索引
     *
     * @return string 复合索引
     * @since 1.0.0
     */
    @AliasFor(value = "def", annotation = CompoundIndex.class)
    String def() default "";

    /**
     * 是否是唯一索引
     *
     * @return boolean boolean
     * @see <a href="https://docs.mongodb.org/manual/core/index-unique/"></a>
     * @since 1.0.0
     */
    @AliasFor(value = "unique", annotation = CompoundIndex.class)
    boolean unique() default false;

    /**
     * 如果设置为true, 则索引将跳过缺少索引字段的任何文档。
     *
     * @return boolean boolean
     * @see <a href="https://docs.mongodb.org/manual/core/index-sparse/"></a>
     * @since 1.0.0
     */
    @AliasFor(value = "sparse", annotation = CompoundIndex.class)
    boolean sparse() default false;

    /**
     * 索引名
     *
     * @return string string
     * @since 1.0.0
     */
    @AliasFor(value = "name", annotation = CompoundIndex.class)
    String name() default "";

    /**
     * 如果设置为true, 那么MongoDB将忽略给定的索引名, 而是生成一个新名称。
     *
     * @return boolean boolean
     * @since 1.0.0
     */
    @AliasFor(value = "useGeneratedName", annotation = CompoundIndex.class)
    boolean useGeneratedName() default false;

    /**
     * 如果为true, 则将在后台创建索引。
     *
     * @return boolean boolean
     * @see <a href="https://docs.mongodb.org/manual/core/indexes/#background-construction"></a>
     * @since 1.0.0
     */
    @AliasFor(value = "background", annotation = CompoundIndex.class)
    boolean background() default false;
}
