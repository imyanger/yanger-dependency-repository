package com.yanger.starter.mongo.conditions;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.mongodb.WriteConcern;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.yanger.starter.mongo.core.IMongoQuery;
import com.yanger.starter.mongo.core.IMongoUpdate;
import com.yanger.starter.mongo.core.Update;
import com.yanger.starter.mongo.datasource.MongoDataSource;
import com.yanger.starter.mongo.enums.Between;
import com.yanger.starter.mongo.enums.MongoType;
import com.yanger.starter.mongo.enums.OP;
import com.yanger.starter.mongo.enums.Operator;
import com.yanger.starter.mongo.exception.MongoException;
import com.yanger.starter.mongo.factory.MongoProviderFactory;
import com.yanger.starter.mongo.mapper.Model;
import com.yanger.starter.mongo.util.FieldConvertUtils;
import com.yanger.starter.mongo.util.Wrappers;
import com.yanger.tools.web.exception.BasicException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuppressWarnings(value = {"serial", "unchecked", "checkstyle:MethodLimit"})
public abstract class AbstractWrapper<M extends Model<M>, R, Children extends AbstractWrapper<M, R, Children>> extends Wrapper<M> {

    /** 数据库表映射实体类 */
    private M entity;
    /** 实体类型(主要用于确定泛型以及取TableInfo缓存) */
    private Class<M> entityClass;
    /** 占位符 */
    @Getter
    protected final Children typedThis = (Children) this;
    /** prefix */
    @Getter
    @Setter
    protected String prefix = null;
    /** suffix */
    @Getter
    @Setter
    protected String suffix = null;
    /** Mongo template */
    @Getter
    @Setter
    protected MongoTemplate mongoTemplate;
    /** Map */
    @Getter
    @Setter
    protected Map<OP, List<Criteria>> map = Maps.newHashMap();

    /**
     * 子类返回一个自己的新对象  @return the children  @return the children  @return the children
     */
    protected abstract Children instance();

    /**
     * Gets entity *
     *
     * @return the entity
     */
    @Override
    public M getEntity() {
        return this.entity;
    }

    /**
     * Sets entity *
     *
     * @param entity entity
     * @return the entity
     */
    public Children setEntity(M entity) {
        this.entity = entity;
        return this.typedThis;
    }

    /**
     * Gets entity class *
     *
     * @return the entity class
     */
    protected Class<M> getEntityClass() {
        if (this.entityClass == null && this.entity != null) {
            this.entityClass = (Class<M>) this.entity.getClass();
        }
        return this.entityClass;
    }

    /**
     * Sets entity class *
     *
     * @param entityClass entity class
     * @return the entity class
     */
    public Children setEntityClass(Class<M> entityClass) {
        if (entityClass != null) {
            this.entityClass = entityClass;
        }
        return this.typedThis;
    }

    /**
     * Fields wrapper.
     *
     * @param fields the fields
     * @return the wrapper
     */
    public Children fields(String... fields) {
        Arrays.stream(fields).forEach(f -> {
            this.field(FieldConvertUtils.convert(f));
        });

        return this.typedThis;
    }

    /**
     * Where wrapper.
     *
     * @param property the property
     * @param values   the values
     * @return the wrapper
     */
    public Children where(String property, @NotNull Serializable... values) {
        property = FieldConvertUtils.convert(property);
        Criteria[] criterias = new Criteria[values.length];
        int index = 0;
        for (Serializable str : values) {
            criterias[index] = Criteria.where(property).is(str);
            index++;
        }
        this.criteria.andOperator(criterias);
        return this.typedThis;
    }

    /**
     * Limit wrapper.
     *
     * @param limit the limit
     * @param page  the page
     * @return the wrapper
     */
    public Children limit(long limit, long page) {
        this.limit(limit, page, 0);
        return this.typedThis;
    }

    /**
     * Limit wrapper.
     *
     * @param limit the limit
     * @param page  the page
     * @param skip  the skip
     * @return the wrapper
     */
    public Children limit(long limit, long page, long skip) {
        if (page < 1) {
            throw new MongoException("page is invalid ...");
        }
        this.query.limit((int) limit);
        this.query.skip(skip + ((page - 1) * limit));
        return this.typedThis;
    }

    /**
     * Nor wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children nor(String key, Object... value) {
        this.push(OP.NOR, Wrappers.genOrCriteria(key, value));
        return this.typedThis;
    }

    /**
     * Push *
     *
     * @param op           op
     * @param criteriaList criteria list
     */
    private void push(OP op, List<Criteria> criteriaList) {
        List<Criteria> criterias = this.map.computeIfAbsent(op, k -> new ArrayList<>());
        criterias.addAll(criteriaList);
    }


    /**
     * Nor wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children nor(String[] key, Object... value) {
        this.push(OP.NOR, Wrappers.genOrCriteria(key, value));
        return this.typedThis;
    }


    /**
     * Nor wrapper.
     *
     * @param key       the key
     * @param value     the value
     * @param operators the operators
     * @return the wrapper
     */
    public Children nor(String[] key, Object[] value, Operator[] operators) {
        this.push(OP.NOR, Wrappers.genOrCriteria(key, value, operators));
        return this.typedThis;
    }

    /**
     * And wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children and(String[] key, Object... value) {
        this.push(OP.AND, Wrappers.genOrCriteria(key, value));
        return this.typedThis;
    }

    /**
     * And wrapper.
     *
     * @param key       the key
     * @param value     the value
     * @param operators the operators
     * @return the wrapper
     */
    public Children and(String[] key, Object[] value, Operator[] operators) {
        this.push(OP.AND, Wrappers.genOrCriteria(key, value, operators));
        return this.typedThis;
    }

    /**
     * Gte wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children gte(String key, Object value) {
        this.and(key).gte(value);
        return this.typedThis;
    }

    /**
     * Gt wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children gt(String key, Object value) {
        this.and(key).gt(value);
        return this.typedThis;
    }

    /**
     * Lt wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children lt(String key, Object value) {
        this.and(key).lt(value);
        return this.typedThis;
    }

    /**
     * Lte wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children lte(String key, Object value) {
        this.and(key).lte(value);
        return this.typedThis;
    }

    /**
     * Gte or null wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children gteOrNull(String key, Object value) {
        return this.orNull(Criteria.where(key).gte(value), key);
    }

    /**
     * Gt or null wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children gtOrNull(String key, Object value) {
        return this.orNull(Criteria.where(key).gt(value), key);
    }

    /**
     * Lte or null wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children lteOrNull(String key, Object value) {
        return this.orNull(Criteria.where(key).lte(value), key);
    }

    /**
     * Lt or null wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children ltOrNull(String key, Object value) {
        return this.orNull(Criteria.where(key).lt(value), key);
    }

    /**
     * Or null wrapper
     *
     * @param criteria criteria
     * @param key      key
     * @return the wrapper
     */
    @Contract("_, _ -> this")
    private Children orNull(Criteria criteria, String key) {
        Criteria c = new Criteria();
        List<Criteria> criterias = Lists.newArrayListWithExpectedSize(2);
        criterias.add(criteria);
        criterias.add(Criteria.where(key).is(null));
        c.orOperator(criterias.toArray(new Criteria[0]));
        this.push(OP.AND, Collections.singletonList(c));
        return this.typedThis;
    }

    /**
     * In wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children in(String key, Object... value) {
        this.and(key).in(value);
        return this.typedThis;
    }

    /**
     * Nin wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children nin(String key, Object... value) {
        this.and(key).nin(value);
        return this.typedThis;
    }

    /**
     * Size wrapper.
     *
     * @param key  the key
     * @param size the size
     * @return the wrapper
     */
    public Children size(String key, int size) {
        this.and(key).size(size);
        return this.typedThis;
    }

    /**
     * Exists wrapper.
     *
     * @param key  the key
     * @param flag the flag
     * @return the wrapper
     */
    public Children exists(String key, boolean flag) {
        this.and(key).exists(flag);
        return this.typedThis;
    }

    /**
     * Or wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children or(String[] key, Object... value) {
        this.push(OP.OR, Wrappers.genOrCriteria(key, value));
        return this.typedThis;
    }

    /**
     * Or wrapper.
     *
     * @param key       the key
     * @param value     the value
     * @param operators the operators
     * @return the wrapper
     */
    public Children or(String[] key, Object[] value, Operator[] operators) {
        this.push(OP.OR, Wrappers.genOrCriteria(key, value, operators));
        return this.typedThis;
    }

    /**
     * My self wrapper.
     *
     * @param myself the myself
     * @return the wrapper
     */
    public Children mySelf(@NotNull IMongoQuery myself) {
        myself.query(this.criteria);
        return this.typedThis;
    }

    /**
     * Fuzzy wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children fuzzy(String key, Object value) {
        String re = String.valueOf(value);
        re = re.replace("(", "\\(").replace(")", "\\)");
        this.and(key).regex(re, "i");
        return this.typedThis;
    }

    /**
     * And wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children and(String key, Object... value) {
        this.push(OP.AND, Wrappers.genOrCriteria(key, value));
        return this.typedThis;
    }

    /**
     * Ne wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children ne(String key, Object value) {
        this.and(key).ne(value);
        return this.typedThis;
    }

    /**
     * Or wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children or(String key, Object... value) {
        this.push(OP.OR, Wrappers.genOrCriteria(key, value));
        return this.typedThis;
    }

    /**
     * Skip wrapper.
     *
     * @param limit the limit
     * @param skip  the skip
     * @return the wrapper
     */
    public Children skip(int limit, int skip) {
        if (skip < 0) {
            throw new BasicException("skip is invalid ...");
        }
        this.query.limit(limit);
        this.query.skip(skip);
        return this.typedThis;
    }

    /**
     * Desc wrapper.
     *
     * @param properties the properties
     * @return the wrapper
     */
    public Children desc(String... properties) {
        properties = FieldConvertUtils.convert(properties);
        this.query.with(Sort.by(Sort.Direction.DESC, properties));
        return this.typedThis;
    }

    /**
     * Asc wrapper.
     *
     * @param properties the properties
     * @return the wrapper
     */
    public Children asc(String... properties) {
        properties = FieldConvertUtils.convert(properties);
        this.query.with(Sort.by(Sort.Direction.ASC, properties));
        return this.typedThis;
    }

    /**
     * Type wrapper.
     *
     * @param key  the key
     * @param type the type
     * @return the wrapper
     */
    public Children type(String key, int type) {
        this.and(key).type(type);
        return this.typedThis;
    }

    /**
     * Between wrapper.
     *
     * @param key     the key
     * @param begin   the begin
     * @param end     the end
     * @param between the between
     * @return the wrapper
     */
    public Children between(String key, Object begin, Object end, @NotNull Between between) {
        between.between(this.and(key), begin, end);
        return this.typedThis;
    }

    /**
     * Safe mongo.
     * WriteConcern.NONE:没有异常抛出
     * WriteConcern.NORMAL:仅抛出网络错误异常,没有服务器错误异常
     * WriteConcern.SAFE:抛出网络错误异常、服务器错误异常; 并等待服务器完成写操作.
     * WriteConcern.MAJORITY: 抛出网络错误异常、服务器错误异常; 并等待一个主服务器完成写操作.
     * WriteConcern.FSYNC_SAFE: 抛出网络错误异常、服务器错误异常; 写操作等待服务器将数据刷新到磁盘.
     * WriteConcern.JOURNAL_SAFE:抛出网络错误异常、服务器错误异常; 写操作等待服务器提交到磁盘的日志文件.
     * WriteConcern.REPLICAS_SAFE:抛出网络错误异常、服务器错误异常; 等待至少2台服务器完成写操作.
     *
     * @return the mongo
     */
    public Children safe() {
        this.mongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        return this.typedThis;
    }

    /**
     * Prefix mongo.
     *
     * @param prefix the prefix
     * @return the mongo
     */
    public Children prefix(String prefix) {
        this.prefix = prefix;
        return this.typedThis;
    }

    /**
     * Suffix mongo.
     *
     * @param suffix the suffix
     * @return the mongo
     */
    public Children suffix(String suffix) {
        this.suffix = suffix;
        return this.typedThis;
    }

    /**
     * Build criteria mongo.
     *
     * @param criteria the criteria
     * @return the mongo
     */
    public Children buildCriteria(Criteria criteria) {
        this.criteria = criteria;
        return this.typedThis;
    }

    /**
     * 地理坐标查询
     *
     * @param lat the lat
     * @param lng the lng
     * @param cls the cls
     * @return the list
     */
    public List<M> geoFind(Double lat, Double lng, Class<M> cls) {
        NearQuery nearQuery = NearQuery.near(lng, lat, Metrics.KILOMETERS);
        nearQuery.query(this.query);
        nearQuery.spherical(true);
        nearQuery.limit(this.query.getSkip() + this.query.getLimit());
        GeoResults<M> geoResults = this.getMongoTemplate(cls).geoNear(nearQuery, cls, this.collectionName(cls));
        List<GeoResult<M>> results = geoResults.getContent();
        List<M> list = Lists.newArrayListWithExpectedSize(results.size());
        for (GeoResult<M> g : results) {
            list.add(g.getContent());
        }
        return list;
    }

    /**
     * 获取数据源
     * 1. Mongo.build(mongoTemplate).xxx    --> 手动设置数据源(用于操作没有被 @MongoCollection 标识的实体)
     * 2. Mongo.build().xxx --> 通过实体注解获取数据源
     *
     * @param clazz the clazz           实体类
     * @return the mongo template       mongo 操作模板类
     */
    private MongoTemplate getMongoTemplate(Class<M> clazz) {
        if (this.mongoTemplate != null) {
            return this.mongoTemplate;
        }
        this.mongoTemplate = MongoDataSource.getDataSource(clazz);

        if (this.mongoTemplate == null) {
            this.mongoTemplate = MongoDataSource.getDataSource();
        }

        return this.mongoTemplate;
    }

    /**
     * Load t.
     *
     * @param id    the id
     * @param clazz the clazz
     * @return the t
     */
    public M load(String id, Class<M> clazz) {
        this.eq(ID, id);
        this.init();
        return this.getMongoTemplate(clazz).findOne(this.query, clazz, this.collectionName(clazz));
    }

    /**
     * Eq wrapper.
     *
     * @param key   the key
     * @param value the value
     * @return the wrapper
     */
    public Children eq(String key, Object value) {
        this.and(key).is(value);
        return this.typedThis;
    }

    /**
     * Init.
     */
    private void init() {
        Set<OP> ops = this.map.keySet();
        for (OP op : ops) {
            List<Criteria> criterias = this.map.get(op);
            if (CollectionUtils.isNotEmpty(criterias)) {
                op.op(this.criteria, criterias.toArray(new Criteria[0]));
            }
        }
    }

    /**
     * One t.
     *
     * @param clazz the clazz
     * @return the t
     */
    public M one(Class<M> clazz) {
        this.init();
        return this.getMongoTemplate(clazz).findOne(this.query, clazz, this.collectionName(clazz));
    }

    /**
     * One t
     *
     * @return the t
     */
    public M one() {
        return this.getMongoTemplate(this.getEntityClass()).findOne(this.query,
                                                                    this.getEntityClass(),
                                                                    this.collectionName(this.getEntityClass()));
    }

    /**
     * Save.
     *
     * @param obj the obj
     */
    public void save(M obj) {
        this.insert(obj);
    }

    /**
     * Insert.
     *
     * @param obj the obj
     */
    @SuppressWarnings("unchecked")
    private void insert(@NotNull M obj) {
        this.getMongoTemplate((Class<M>) obj.getClass()).insert(obj, this.collectionName(obj));
    }

    /**
     * Collection name string
     *
     * @param obj obj
     * @return the string
     */
    @NotNull
    private String collectionName(@NotNull M obj) {
        Class<?> clazz = obj.getClass();

        return this.collectionName(clazz);
    }

    /**
     * 通过实体注解获取 collectionName, 并且拼装前后缀
     * 1. 被 @MongoCollection 标识的实体使用 value
     * 2. 没有被 @MongoCollection 标识的实体使用 class simple name
     *
     * @param clazz the clazz
     * @return the string
     */
    @NotNull
    public String collectionName(Class<?> clazz) {
        String collectionName = MongoProviderFactory.collectionName(clazz);

        if (StringUtils.isBlank(collectionName)) {
            collectionName = clazz.getSimpleName();
        }
        StringBuilder cn = new StringBuilder();
        if (StringUtils.isNotBlank(this.prefix)) {
            cn.append(this.prefix);
        }
        cn.append(collectionName);
        if (StringUtils.isNotBlank(this.suffix)) {
            cn.append(this.suffix);
        }
        return cn.toString();
    }

    /**
     * Insert mongo type.
     *
     * @param update the update
     * @param obj    the obj
     * @return the mongo type
     */
    @SuppressWarnings("unchecked")
    public MongoType insert(IMongoUpdate update, @NotNull M obj) {
        if (this.count((Class<M>) obj.getClass()) > 0) {
            if (update != null) {
                this.updateFirst(update, (Class<M>) obj.getClass());
            }
            return MongoType.UPDATE;
        } else {
            this.insert(obj);
            return MongoType.INSERT;
        }
    }

    /**
     * Count long.
     *
     * @param clazz the clazz
     * @return the long
     */
    public long count(Class<M> clazz) {
        this.init();
        return this.getMongoTemplate(clazz).count(this.query, this.collectionName(clazz));
    }

    /**
     * Update first write result.
     *
     * @param update the update
     * @param clazz  the clazz
     * @return the write result
     */
    public UpdateResult updateFirst(IMongoUpdate update, Class<M> clazz) {
        org.springframework.data.mongodb.core.query.Update mongoUpdate = this.getUpdate(update);
        return this.getMongoTemplate(clazz).updateFirst(this.query, mongoUpdate, this.collectionName(clazz));
    }

    /**
     * Gets update *
     *
     * @param update update
     * @return the update
     */
    public org.springframework.data.mongodb.core.query.Update getUpdate(@NotNull IMongoUpdate update) {
        org.springframework.data.mongodb.core.query.Update mongoUpdate =
            org.springframework.data.mongodb.core.query.Update.update(VERSION, System.currentTimeMillis());
        update.update(new Update(mongoUpdate));
        this.init();
        return mongoUpdate;
    }

    /**
     * Remove write result.
     *
     * @param cls the cls
     * @return the write result
     */
    public DeleteResult remove(Class<M> cls) {
        this.init();
        return this.getMongoTemplate(cls).remove(this.query, this.collectionName(cls));
    }

    /**
     * List list.
     *
     * @param clazz the clazz
     * @return the list
     */
    public List<M> list(Class<M> clazz) {
        this.init();
        return this.getMongoTemplate(clazz).find(this.query, clazz, this.collectionName(clazz));
    }

    /**
     * Insert batch.
     *
     * @param objs  the objs
     * @param clazz the clazz
     */
    public void insertBatch(List<M> objs, Class<M> clazz) {
        this.getMongoTemplate(clazz).insert(objs, this.collectionName(clazz));
    }

    /**
     * All list.
     *
     * @param clazz the clazz
     * @return the list
     */
    public List<M> all(Class<M> clazz) {
        return this.getMongoTemplate(clazz).findAll(clazz, this.collectionName(clazz));
    }

    /**
     * Upsert write result.
     *
     * @param update the update
     * @param clazz  the clazz
     * @return the write result
     */
    public UpdateResult upsert(IMongoUpdate update, Class<M> clazz) {
        org.springframework.data.mongodb.core.query.Update mongoUpdate = this.getUpdate(update);
        return this.getMongoTemplate(clazz).upsert(this.query, mongoUpdate, this.collectionName(clazz));
    }

    /**
     * Update multi write result.
     *
     * @param update the update
     * @param clazz  the clazz
     * @return the write result
     */
    public UpdateResult updateMulti(IMongoUpdate update, Class<M> clazz) {
        org.springframework.data.mongodb.core.query.Update mongoUpdate = this.getUpdate(update);
        return this.getMongoTemplate(clazz).updateMulti(this.query, mongoUpdate, this.collectionName(clazz));
    }

    /**
     * Find and modify.
     *
     * @param update the update
     * @param clazz  the clazz
     */
    public void findAndModify(IMongoUpdate update, Class<M> clazz) {
        org.springframework.data.mongodb.core.query.Update mongoUpdate = this.getUpdate(update);
        this.getMongoTemplate(clazz).findAndModify(this.query, mongoUpdate, clazz, this.collectionName(clazz));
    }

    /**
     * Find and update t.
     *
     * @param clazz  the clazz
     * @param update the update
     * @return the t
     */
    public M findAndUpdate(Class<M> clazz, IMongoUpdate update) {
        org.springframework.data.mongodb.core.query.Update mongoUpdate = this.getUpdate(update);
        return this.getMongoTemplate(clazz).findAndModify(this.query, mongoUpdate, clazz, this.collectionName(clazz));
    }

    /**
     * Find and update t.
     *
     * @param clazz     the clazz
     * @param update    the update
     * @param upsert    the upsert
     * @param returnNew the return new
     * @return the t
     */
    public M findAndUpdate(Class<M> clazz, IMongoUpdate update, boolean upsert, boolean returnNew) {
        org.springframework.data.mongodb.core.query.Update mongoUpdate = this.getUpdate(update);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(upsert).returnNew(returnNew);
        return this.getMongoTemplate(clazz).findAndModify(this.query, mongoUpdate, options, clazz, this.collectionName(clazz));
    }

}
