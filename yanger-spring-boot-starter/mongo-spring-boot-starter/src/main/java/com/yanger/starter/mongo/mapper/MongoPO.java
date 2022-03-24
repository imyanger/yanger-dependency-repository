package com.yanger.starter.mongo.mapper;

import cn.hutool.core.lang.Snowflake;
import com.yanger.starter.basic.entity.IBaseEntity;
import com.yanger.starter.mongo.annotation.AutoIncKey;
import com.yanger.starter.mongo.annotation.MongoColumn;
import com.yanger.starter.mongo.enums.FieldFill;
import com.yanger.starter.mongo.listener.AutoIncrementKeyEventListener;
import com.yanger.tools.web.tools.RandomUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ActiveRecord 模式 CRUD
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public abstract class MongoPO<T extends Serializable, M extends Model<M>> extends Model<M> implements IBaseEntity<T> {

    /** Enable auto increment key */
    @Transient
    private boolean enableAutoIncrementKey;

    /** 是否已从环境变量中获取配置 */
    @Transient
    private final boolean flag = false;

    /** Snowflake */
    @Transient
    private final Snowflake snowflake = new Snowflake(RandomUtils.nextInt(30), RandomUtils.nextInt(30));

    /** serialVersionUID */
    private static final long serialVersionUID = 7078041398239557709L;

    /** DB_FIELD_CREATE_TIME */
    public static final String DB_FIELD_CREATE_TIME = "create_time";

    /** JAVA_FIELD_CREATE_TIME */
    public static final String JAVA_FIELD_CREATE_TIME = "createTime";

    /** DB_FIELD_UPDATE_TIME */
    public static final String DB_FIELD_UPDATE_TIME = "update_time";

    /** JAVA_FIELD_UPDATE_TIME */
    public static final String JAVA_FIELD_UPDATE_TIME = "updateTime";

    /** Id */
    @Id
    @AutoIncKey
    @MongoColumn(index = MONGO_ID)
    protected T id;

    /** Create at */
    @Getter
    @Setter
    @MongoColumn(name = DB_FIELD_CREATE_TIME, fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /** Update at */
    @Getter
    @Setter
    @MongoColumn(name = DB_FIELD_UPDATE_TIME, fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    /**
     * To string string
     * @return the string
     */
    @Override
    public String toString() {
        return "MongoPO(id="
               + this.getId()
               + ", createTime="
               + this.getCreateTime()
               + ", updateTime="
               + this.getUpdateTime()
               + ")";
    }

    /**
     * 根据 id 类型初始化, 如果启动 id 自增且是 Long 类型, 则将 id 重新赋值为 null, 交由 {@link AutoIncrementKeyEventListener} 进行 id 初始化
     * id 性能和分布式问题
     * @return the model
     */
    @Override
    protected Model<M> init() {
        return this;
    }

    /**
     * Pk val serializable
     * @return the serializable
     */
    @Override
    public T getId() {
        return this.id;
    }

    /**
     * 必须使用 @Builder 注解, 不然在使用 wrapper 转换时 id 会丢失
     * @param id id
     * @return the id
     */
    @SuppressWarnings("unchecked")
    public M setId(T id) {
        this.id = id;
        return (M) this;
    }

}
