package com.fkhwl.starter.mongo.handler.metadata;

import com.fkhwl.starter.mongo.enums.FieldFill;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: object 字段反射信息</p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.04.12 12:51
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Getter
@ToString
@EqualsAndHashCode
public class MetadataFieldInfo implements Constants {

    /**
     * 属性
     */
    private final Field field;
    /**
     * 别名
     */
    private final String name;
    /**
     * 属性名
     */
    private final String property;
    /**
     * 属性类型
     */
    private final Class<?> propertyType;

    /**
     * 字段填充策略
     */
    private FieldFill fieldFill = FieldFill.DEFAULT;
    /**
     * 字段是否启用了插入填充
     */
    private boolean withInsertFill;
    /**
     * 字段是否启用了更新填充
     */
    private boolean withUpdateFill;

    /**
     * 全新的 存在 TableField 注解时使用的构造函数
     *
     * @param metadataInfo  metadata info
     * @param field         field
     * @param metadataField table field
     */
    public MetadataFieldInfo(MetadataInfo metadataInfo, @NotNull Field field, @NotNull MetadataField metadataField) {
        field.setAccessible(true);
        this.field = field;
        this.property = field.getName();
        this.propertyType = field.getType();
        this.fieldFill = metadataField.fill();
        this.withInsertFill = this.fieldFill == FieldFill.INSERT || this.fieldFill == FieldFill.INSERT_UPDATE;
        this.withUpdateFill = this.fieldFill == FieldFill.UPDATE || this.fieldFill == FieldFill.INSERT_UPDATE;
        this.name = metadataField.value();
    }

    /**
     * 不存在 TableField 注解时, 使用的构造函数
     *
     * @param metadataInfo metadata info
     * @param field        field
     */
    public MetadataFieldInfo(MetadataInfo metadataInfo, @NotNull Field field) {
        field.setAccessible(true);
        this.field = field;
        this.property = field.getName();
        this.propertyType = field.getType();
        this.name = field.getName();
    }
}
