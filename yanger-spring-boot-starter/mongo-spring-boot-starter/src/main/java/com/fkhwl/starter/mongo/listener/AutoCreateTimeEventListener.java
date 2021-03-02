package com.fkhwl.starter.mongo.listener;

import com.fkhwl.starter.mongo.handler.MetaObjectHandler;
import com.fkhwl.starter.mongo.mapper.MongoPO;
import com.fkhwl.starter.mongo.reflection.DefaultMetaObject;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 自动生成 createTime, updateTime </p>
 * 只有设置了 {@link com.fkhwl.starter.mongo.autoconfigure.sync.MongoProperties#isEnableAutoCreateTime()}} 才会生效
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 14:27
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
public class AutoCreateTimeEventListener extends AbstractMongoEventListener<MongoPO<?, ?>> {
    /** Meta object handler */
    private final MetaObjectHandler metaObjectHandler;

    /**
     * Auto create time event listener
     *
     * @param metaObjectHandler meta object handler
     */
    public AutoCreateTimeEventListener(MetaObjectHandler metaObjectHandler) {
        this.metaObjectHandler = metaObjectHandler;
    }

    /**
     * 对象转换成数据库对象的时候自动生成 createTime 和 updateTime
     * 注意: BeforeConvertEvent 时间应该使用实体字段来操作, 而 BeforeSaveEvent 事件操作 操作 Document 才生效.
     * 只有在业务端配置了填充策略才会自动填充指定字段
     *
     * @param event event
     * @see MongoPO
     */
    @Override
    public void onBeforeConvert(@NotNull BeforeConvertEvent<MongoPO<?, ?>> event) {
        if (this.metaObjectHandler != null) {
            MongoPO<?, ?> source = event.getSource();
            this.metaObjectHandler.insertFill(DefaultMetaObject.forObject(source));
            this.metaObjectHandler.updateFill(DefaultMetaObject.forObject(source));
        }

    }

}
