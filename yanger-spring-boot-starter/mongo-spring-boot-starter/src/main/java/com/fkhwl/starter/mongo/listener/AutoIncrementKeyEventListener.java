package com.fkhwl.starter.mongo.listener;

import com.fkhwl.starter.core.util.ClassUtils;
import com.fkhwl.starter.mongo.annotation.AutoIncKey;
import com.fkhwl.starter.mongo.entity.Sequence;
import com.fkhwl.starter.mongo.exception.MongoException;
import com.fkhwl.starter.mongo.mapper.MongoPO;

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
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 自增主键设置 </p>
 * todo-dong4j : (2020年04月04日 18:19) [未支持分布式自增主键]
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 14:27
 * @since 1.0.0
 */
@Slf4j
public class AutoIncrementKeyEventListener extends AbstractMongoEventListener<MongoPO<?, ?>> {
    /** Mongo */
    private final MongoTemplate mongo;

    /**
     * Auto increment key event listener
     *
     * @param mongoTemplate mongo template
     * @since 1.0.0
     */
    public AutoIncrementKeyEventListener(MongoTemplate mongoTemplate) {
        this.mongo = mongoTemplate;
    }

    /**
     * 对象转换成数据库对象的时候操作 id 字段实现 id 自增
     *
     * @param event event
     * @since 1.0.0
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
     * @since 1.0.0
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
