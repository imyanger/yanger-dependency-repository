package com.fkhwl.starter.mongo.annotation;

import com.fkhwl.starter.mongo.enums.FieldFill;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Field
@Indexed
public @interface MongoColumn {

    /**
     * Name string
     *
     * @return the string
     */
    @AliasFor(value = "value", annotation = Field.class)
    String name() default "";

    /**
     * Desc string.
     *
     * @return the string
     */
    String desc() default "";

    /**
     * 字段自动填充策略
     * todo-dong4j : (2020年04月08日 13:40) [暂未处理]
     *
     * @return the field fill
     */
    FieldFill fill() default FieldFill.DEFAULT;

    /**
     * Target type field type
     *
     * @return the field type
     */
    @AliasFor(value = "targetType", annotation = Field.class)
    FieldType targetType() default FieldType.IMPLICIT;

    /**
     * Required boolean.
     *
     * @return the boolean
     */
    boolean required() default true;

    /**
     * Default value string.
     *
     * @return the string
     */
    String defaultValue() default "";

    /**
     * If set to true reject all documents that contain a duplicate value for the indexed field.
     *
     * @return boolean boolean
     * @see <a href="https://docs.mongodb.org/manual/core/index-unique/"></a>
     */
    boolean unique() default false;

    /**
     * Direction index direction
     *
     * @return the index direction
     */
    IndexDirection direction() default IndexDirection.ASCENDING;

    /**
     * If set to true index will skip over any document that is missing the indexed field.
     *
     * @return boolean boolean
     * @see <a href="https://docs.mongodb.org/manual/core/index-sparse/"></a>
     */
    boolean sparse() default false;

    /**
     * Index name either as plain value or as {@link org.springframework.expression.spel.standard.SpelExpression template expression}
     * <br />
     * The name will only be applied as is when defined on root level. For usage on nested or embedded structures the
     * provided name will be prefixed with the path leading to the entity. <br />
     * <br />
     * The structure below
     *
     * <pre>
     * <code>
     * &#64;Document
     * class Root {
     *   Hybrid hybrid;
     *   Nested nested;
     * }
     *
     * &#64;Document
     * class Hybrid {
     *   &#64;Indexed(name="index") String h1;
     *   &#64;Indexed(name="#{&#64;myBean.indexName}") String h2;
     * }
     *
     * class Nested {
     *   &#64;Indexed(name="index") String n1;
     * }
     * </code>
     * </pre>
     * <p>
     * resolves in the following index structures
     *
     * <pre>
     * <code>
     * db.root.createIndex( { hybrid.h1: 1 } , { name: "hybrid.index" } )
     * db.root.createIndex( { nested.n1: 1 } , { name: "nested.index" } )
     * db.hybrid.createIndex( { h1: 1} , { name: "index" } )
     * db.hybrid.createIndex( { h2: 1} , { name: the value myBean.getIndexName() returned } )
     * </code>
     * </pre>
     *
     * @return string string
     */
    @AliasFor(value = "name", annotation = Indexed.class)
    String index() default "";

    /**
     * If set to {@literal true} then MongoDB will ignore the given index name and instead generate a new name. Defaults
     * to {@literal false}.
     *
     * @return boolean boolean
     */
    boolean useGeneratedName() default false;

    /**
     * If {@literal true} the index will be created in the background.
     *
     * @return boolean boolean
     * @see <a href="https://docs.mongodb.org/manual/core/indexes/#background-construction"></a>
     */
    boolean background() default false;

    /**
     * Configures the number of seconds after which the collection should expire. Defaults to -1 for no expiry.
     *
     * @return int int
     * @see <a href="https://docs.mongodb.org/manual/tutorial/expire-data/"></a>
     */
    int expireAfterSeconds() default -1;

    /**
     * Alternative for {@link #expireAfterSeconds()} to configure the timeout after which the document should expire.
     * Defaults to an empty {@link String} for no expiry. Accepts numeric values followed by their unit of measure:
     * <ul>
     * <li><b>d</b>: Days</li>
     * <li><b>h</b>: Hours</li>
     * <li><b>m</b>: Minutes</li>
     * <li><b>s</b>: Seconds</li>
     * <li>Alternatively: A Spring {@literal template expression}. The expression can result in a
     * {@link java.time.Duration} or a valid expiration {@link String} according to the already mentioned
     * conventions.</li>
     * </ul>
     * Supports ISO-8601 style.
     *
     * <pre class="code">
     *
     * &#0064;Indexed(expireAfter = "10s") String expireAfterTenSeconds;
     *
     * &#0064;Indexed(expireAfter = "1d") String expireAfterOneDay;
     *
     * &#0064;Indexed(expireAfter = "P2D") String expireAfterTwoDays;
     *
     * &#0064;Indexed(expireAfter = "#{&#0064;mySpringBean.timeout}") String expireAfterTimeoutObtainedFromSpringBean;
     * </pre>
     *
     * @return empty by default.
     */
    String expireAfter() default "";
}
