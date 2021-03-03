package com.yanger.starter.mongo.listener;

import com.yanger.starter.mongo.annotation.AutoIncKey;
import com.yanger.starter.mongo.entity.Sequence;
import com.yanger.starter.mongo.exception.MongoException;
import com.yanger.starter.mongo.mapper.MongoPO;
import com.yanger.tools.web.tools.ClassUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ReflectionUtils;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 自增主键设置
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
public class AutoIncrementKeyEventListener extends AbstractMongoEventListener<MongoPO<?, ?>> {
    /** Mongo */
    private final MongoTemplate mongo;

    /**
     * Auto increment key event listener
     *
     * @param mongoTemplate mongo template
     */
    public AutoIncrementKeyEventListener(MongoTemplate mongoTemplate) {
        this.mongo = mongoTemplate;
    }

    /**
     * 对象转换成数据库对象的时候操作 id 字段实现 id 自增
     *
     * @param event event
     */
    @Override
    public void onBeforeConvert(@NotNull BeforeConvertEvent<MongoPO<?, ?>> event) {
        MongoPO<?, ?> source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);

            // 设置自增 id
            if (field.isAnnotationPresent(AutoIncKey.class) && field.get(source) == null) {
                // 获取实体父类中的第一个泛型类型
                Class<?> idClass = ClassUtils.getSuperClassT(event.getSource().getClass(), 0);
                if (!Long.class.isAssignableFrom(idClass)) {
                    throw new MongoException("非 Long 类型不能自增 id");
                }
                field.set(source, AutoIncrementKeyEventListener.this.getNextId(source.getClass().getSimpleName()));
            }
        });

    }

    /**
     * 利用 mongo 的 findAndModify 的原子性增加 id, 分布式环境下不适用.
     *
     * @param collectionName collection name
     * @return the next id
     */
    private Long getNextId(String collectionName) {
        Query query = new Query(Criteria.where(Sequence.COLLECTION_NAME).is(collectionName));
        Update update = new Update();
        update.inc(Sequence.SEQ_ID, 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        Sequence inc = this.mongo.findAndModify(query, update, options, Sequence.class);
        return Objects.requireNonNull(inc).getSeqId();
    }
}
