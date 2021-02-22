package com.fkhwl.starter.mongo.listener;

import com.fkhwl.starter.core.util.ClassUtils;
import com.fkhwl.starter.core.util.RandomUtils;
import com.fkhwl.starter.core.util.StringUtils;
import com.fkhwl.starter.mongo.exception.MongoException;
import com.fkhwl.starter.mongo.mapper.MongoPO;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;

import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 自动生成 id </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 14:27
 * @since 1.0.0
 */
@Slf4j
public class AutoCreateKeyEventListener extends AbstractMongoEventListener<MongoPO<?, ?>> {
    /** Is enable auto increment key */
    private final boolean isEnableAutoIncrementKey;
    /** Snowflake */
    private final Snowflake snowflake = new Snowflake(RandomUtils.nextInt(30), RandomUtils.nextInt(30));

    /**
     * Auto create key event listener
     *
     * @param isEnableAutoIncrementKey is enable auto increment key
     * @since 1.0.0
     */
    public AutoCreateKeyEventListener(boolean isEnableAutoIncrementKey) {
        this.isEnableAutoIncrementKey = isEnableAutoIncrementKey;
    }

    /**
     * 对象转换成数据库对象的时候操作 id 字段实现 id 自增
     *
     * @param event event
     * @see MongoPO
     * @since 1.0.0
     */
    @Override
    @SuppressWarnings("checkstyle:EmptyBlock")
    public void onBeforeConvert(@NotNull BeforeConvertEvent<MongoPO<?, ?>> event) {

        MongoPO<?, ?> source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(Id.class) && field.get(source) == null) {
                // 获取实体父类中的第一个泛型类型
                Class<?> idClass = ClassUtils.getSuperClassT(event.getSource().getClass(), 0);
                if (Long.class.isAssignableFrom(idClass)) {
                    if (!this.isEnableAutoIncrementKey) {
                        field.set(source, this.snowflake.nextId());
                    }
                } else if (ObjectId.class.isAssignableFrom(idClass)) {
                    log.trace("ObjectId 类型由 mongo 自动生成");
                } else if (String.class.isAssignableFrom(idClass)) {
                    field.set(source, StringUtils.getUid());
                } else {
                    throw new MongoException("不支持的 id 类型: {}", idClass);
                }
            }
        });

    }

}
