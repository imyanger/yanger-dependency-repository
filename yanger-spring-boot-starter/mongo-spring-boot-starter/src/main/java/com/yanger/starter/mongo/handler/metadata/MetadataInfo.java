package com.yanger.starter.mongo.handler.metadata;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 数据库表反射信息
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Data
@Setter(AccessLevel.PACKAGE)
@Accessors(chain = true)
public class MetadataInfo implements Constants {

    /**
     * 实体类型
     */
    private Class<?> entityType;

    /**
     * 实体名称
     */
    private String name;


    /**
     * 表字段信息列表
     */
    private List<MetadataFieldInfo> fieldList;


    /**
     * 表字段是否启用了插入填充
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private boolean withInsertFill;

    /**
     * 表字段是否启用了更新填充
     */
    @Getter
    @Setter(AccessLevel.NONE)
    private boolean withUpdateFill;

    /**
     * Metadata info
     * @param entityType entity type
     */
    @Contract(pure = true)
    public MetadataInfo(Class<?> entityType) {
        this.entityType = entityType;
    }

    /**
     * Sets field list
     * @param fieldList field list
     */
    void setFieldList(@NotNull List<MetadataFieldInfo> fieldList) {
        this.fieldList = fieldList;
        fieldList.forEach(i -> {
            if (i.isWithInsertFill()) {
                this.withInsertFill = true;
            }
            if (i.isWithUpdateFill()) {
                this.withUpdateFill = true;
            }
        });
    }

}
