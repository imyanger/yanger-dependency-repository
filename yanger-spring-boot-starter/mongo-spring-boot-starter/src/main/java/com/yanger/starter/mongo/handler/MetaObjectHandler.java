package com.yanger.starter.mongo.handler;

import com.yanger.starter.mongo.enums.FieldFill;
import com.yanger.starter.mongo.handler.metadata.MetadataFieldInfo;
import com.yanger.starter.mongo.handler.metadata.MetadataInfo;
import com.yanger.starter.mongo.handler.metadata.MetadataInfoHelper;
import com.yanger.starter.mongo.handler.metadata.StrictFill;
import com.yanger.starter.mongo.reflection.MetaObject;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 元对象字段填充控制器抽象类, 实现公共字段自动写入
 *     所有入参的 MetaObject 必定是 entity 或其子类的 MetaObject
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public interface MetaObjectHandler {

    /**
     * 是否开启了插入填充
     *
     * @return the boolean
     */
    default boolean openInsertFill() {
        return true;
    }

    /**
     * 兼容填充主键判断开关
     * 如果启用开关:当主键值为空且主键生成策略为NONE或INPUT会进入新增填充
     * 这开关主要是用来兼容旧版本的用户使用插入填充来进行主键填充的开关
     * 暂时不确定什么时候会移出此开关,请尽快使用新的Id生成策略来生成Id
     *
     * @return 是否启用 boolean
     */
    default boolean compatibleFillId() {
        return false;
    }

    /**
     * 是否开启了更新填充
     *
     * @return the boolean
     */
    default boolean openUpdateFill() {
        return true;
    }

    /**
     * 插入元对象字段填充 (用于插入时对公共字段的填充)
     *
     * @param metaObject 元对象
     */
    void insertFill(MetaObject metaObject);

    /**
     * 更新元对象字段填充 (用于更新时对公共字段的填充)
     *
     * @param metaObject 元对象
     */
    void updateFill(MetaObject metaObject);

    /**
     * insert 时填充,只会填充 fill 被标识为 INSERT 与 INSERT_UPDATE 的字段
     *
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value
     * @param metaObject meta object parameter
     * @return the insert field val by name
     */
    default MetaObjectHandler setInsertFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject) {
        return this.setFieldValByName(fieldName, fieldVal, metaObject, FieldFill.INSERT);
    }

    /**
     * Common method to set value for java bean.
     *
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value
     * @param metaObject meta object parameter
     * @param fieldFill  填充策略枚举
     * @return the field val by name
     */
    default MetaObjectHandler setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject, FieldFill fieldFill) {
        if (Objects.nonNull(fieldVal)
            && this.isFill(fieldName, fieldVal, metaObject, fieldFill)) {
            metaObject.setValue(fieldName, fieldVal);
        }
        return this;
    }

    /**
     * 填充判断
     * <li> 如果是主键,不填充 </li>
     * <li> 根据字段名找不到字段,不填充 </li>
     * <li> 字段类型与填充值类型不匹配,不填充 </li>
     * <li> 字段类型需在TableField注解里配置fill: @TableField(value="test_type", fill = FieldFill.INSERT), 没有配置或者不匹配时不填充 </li>
     * v_3.1.0以后的版本(不包括3.1.0), 子类的值也可以自动填充, Timestamp的值也可以填入到java.util.Date类型里面
     *
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value
     * @param metaObject meta object parameter
     * @param fieldFill  填充策略枚举
     * @return 是否进行填充 boolean
     */
    default boolean isFill(String fieldName, Object fieldVal, MetaObject metaObject, FieldFill fieldFill) {
        Optional<MetadataFieldInfo> first = this.findMetadataInfo(metaObject).getFieldList().stream()
            //v_3.1.1+ 设置子类的值也可以通过
            .filter(e -> e.getProperty().equals(fieldName) && e.getPropertyType().isAssignableFrom(fieldVal.getClass()))
            .findFirst();
        if (first.isPresent()) {
            FieldFill fill = first.get().getFieldFill();
            return fill == fieldFill || FieldFill.INSERT_UPDATE == fill;
        }
        return false;
    }

    /**
     * 获取 MetadataInfo 缓存
     *
     * @param metaObject meta object parameter
     * @return TableInfo metadata info
     */
    default MetadataInfo findMetadataInfo(@NotNull MetaObject metaObject) {
        return MetadataInfoHelper.getTableInfo(metaObject.getOriginalObject().getClass());
    }

    /**
     * update 时填充,只会填充 fill 被标识为 UPDATE 与 INSERT_UPDATE 的字段
     *
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value
     * @param metaObject meta object parameter
     * @return the update field val by name
     */
    default MetaObjectHandler setUpdateFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject) {
        return this.setFieldValByName(fieldName, fieldVal, metaObject, FieldFill.UPDATE);
    }

    /**
     * Strict insert fill meta object handler
     *
     * @param <T>        parameter
     * @param metaObject metaObject meta object parameter
     * @param fieldName  field name
     * @param fieldType  field type
     * @param fieldVal   field val
     * @return the meta object handler
     */
    default <T> MetaObjectHandler strictInsertFill(MetaObject metaObject,
                                                   String fieldName,
                                                   Class<T> fieldType,
                                                   Object fieldVal) {
        return this.strictInsertFill(this.findMetadataInfo(metaObject),
                                     metaObject,
                                     Collections.singletonList(StrictFill.of(fieldName, fieldType, fieldVal)));
    }

    /**
     * Strict insert fill meta object handler
     *
     * @param metadataInfo metadata info
     * @param metaObject   metaObject meta object parameter
     * @param strictFills  strict fills
     * @return the meta object handler
     */
    default MetaObjectHandler strictInsertFill(MetadataInfo metadataInfo, MetaObject metaObject, List<StrictFill> strictFills) {
        return this.strictFill(true, metadataInfo, metaObject, strictFills);
    }

    /**
     * 严格填充,只针对非主键的字段,只有该表注解了fill 并且 字段名和字段属性 能匹配到才会进行填充(null 值不填充)
     *
     * @param insertFill   是否验证在 insert 时填充
     * @param metadataInfo cache 缓存
     * @param metaObject   metaObject meta object parameter
     * @param strictFills  填充信息
     * @return this meta object handler
     */
    default MetaObjectHandler strictFill(boolean insertFill,
                                         MetadataInfo metadataInfo,
                                         MetaObject metaObject,
                                         List<StrictFill> strictFills) {
        boolean enableInsertFill = insertFill && metadataInfo.isWithInsertFill();
        boolean enableUpdateFill = !insertFill && metadataInfo.isWithUpdateFill();
        if (enableInsertFill || enableUpdateFill) {
            strictFills.forEach(i -> {
                String fieldName = i.getFieldName();
                metadataInfo.getFieldList().stream()
                    .filter(j -> j.getProperty().equals(fieldName)
                                 && i.getFieldType().equals(j.getPropertyType())
                                 && ((insertFill && j.isWithInsertFill()) || (!insertFill && j.isWithUpdateFill()))).findFirst()
                    .ifPresent(j -> this.strictFillStrategy(metaObject, fieldName, i.getFieldVal()));
            });
        }
        return this;
    }

    /**
     * 严格模式填充策略,默认有值不覆盖,如果提供的值为null也不填充
     *
     * @param metaObject metaObject meta object parameter
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value of Supplier
     * @return the meta object handler
     */
    default MetaObjectHandler strictFillStrategy(@NotNull MetaObject metaObject, String fieldName, Supplier<Object> fieldVal) {
        if (metaObject.getValue(fieldName) == null) {
            Object obj = fieldVal.get();
            if (Objects.nonNull(obj)) {
                metaObject.setValue(fieldName, obj);
            }
        }
        return this;
    }

    /**
     * Strict insert fill meta object handler
     *
     * @param <T>        parameter
     * @param metaObject metaObject meta object parameter
     * @param fieldName  field name
     * @param fieldType  field type
     * @param fieldVal   field val
     * @return the meta object handler
     */
    default <T> MetaObjectHandler strictInsertFill(MetaObject metaObject,
                                                   String fieldName,
                                                   Class<T> fieldType,
                                                   Supplier<T> fieldVal) {
        return this.strictInsertFill(this.findMetadataInfo(metaObject),
                                     metaObject,
                                     Collections.singletonList(StrictFill.of(fieldName, fieldType, fieldVal)));
    }

    /**
     * Strict update fill meta object handler
     *
     * @param <T>        parameter
     * @param metaObject metaObject meta object parameter
     * @param fieldName  field name
     * @param fieldType  field type
     * @param fieldVal   field val
     * @return the meta object handler
     */
    default <T> MetaObjectHandler strictUpdateFill(MetaObject metaObject,
                                                   String fieldName,
                                                   Class<T> fieldType,
                                                   Supplier<T> fieldVal) {
        return this.strictUpdateFill(this.findMetadataInfo(metaObject),
                                     metaObject,
                                     Collections.singletonList(StrictFill.of(fieldName, fieldType, fieldVal)));
    }

    /**
     * Strict update fill meta object handler
     *
     * @param metadataInfo metadata info
     * @param metaObject   metaObject meta object parameter
     * @param strictFills  strict fills
     * @return the meta object handler
     */
    default MetaObjectHandler strictUpdateFill(MetadataInfo metadataInfo, MetaObject metaObject, List<StrictFill> strictFills) {
        return this.strictFill(false, metadataInfo, metaObject, strictFills);
    }

    /**
     * Strict update fill meta object handler
     *
     * @param <T>        parameter
     * @param metaObject metaObject meta object parameter
     * @param fieldName  field name
     * @param fieldType  field type
     * @param fieldVal   field val
     * @return the meta object handler
     */
    default <T> MetaObjectHandler strictUpdateFill(MetaObject metaObject,
                                                   String fieldName,
                                                   Class<T> fieldType,
                                                   Object fieldVal) {
        return this.strictUpdateFill(this.findMetadataInfo(metaObject),
                                     metaObject,
                                     Collections.singletonList(StrictFill.of(fieldName, fieldType, fieldVal)));
    }

    /**
     * 填充策略,默认有值不覆盖,如果提供的值为null也不填充
     *
     * @param metaObject metaObject meta object parameter
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value of Supplier
     * @return the meta object handler
     */
    default MetaObjectHandler fillStrategy(MetaObject metaObject, String fieldName, Object fieldVal) {
        if (this.getFieldValByName(fieldName, metaObject) == null) {
            this.setFieldValByName(fieldName, fieldVal, metaObject);
        }
        return this;
    }

    /**
     * get value from java bean by propertyName
     *
     * @param fieldName  java bean property name
     * @param metaObject parameter wrapper
     * @return 字段值 field val by name
     */
    default Object getFieldValByName(String fieldName, @NotNull MetaObject metaObject) {
        return metaObject.hasGetter(fieldName) ? metaObject.getValue(fieldName) : null;
    }

    /**
     * 通用填充
     *
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value
     * @param metaObject meta object parameter
     * @return the field val by name
     */
    default MetaObjectHandler setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject) {
        if (Objects.nonNull(fieldVal) && metaObject.hasSetter(fieldName)) {
            metaObject.setValue(fieldName, fieldVal);
        }
        return this;
    }

}
