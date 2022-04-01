package com.yanger.starter.mongo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mongodb.client.result.DeleteResult;
import com.yanger.starter.mongo.conditions.AbstractWrapper;
import com.yanger.starter.mongo.conditions.Wrapper;
import com.yanger.starter.mongo.conditions.query.QueryWrapper;
import com.yanger.starter.mongo.core.Update;
import com.yanger.starter.mongo.datasource.MongoDataSource;
import com.yanger.starter.mongo.exception.MongoException;
import com.yanger.starter.mongo.factory.MongoProviderFactory;
import com.yanger.starter.mongo.util.Wrappers;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;
import java.util.List;

/**
 * ActiveRecord 模式 CRUD
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuperBuilder
@NoArgsConstructor
@SuppressWarnings("unchecked")
public abstract class Model<M extends Model<M>> implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    public static final String MONGO_ID = "_id";

    /**
     * Template mongo template
     * @param claz claz
     * @return the mongo template
     */
    private static MongoTemplate template(Class<?> claz) {
        return MongoDataSource.getDataSource(claz);
    }

    /**
     * Collection string
     * @param claz claz
     * @return the string
     */
    public static String collection(Class<?> claz) {
        return MongoProviderFactory.collectionName(claz);
    }

    /**
     * Init model.
     * @return the model
     */
    protected abstract Model<M> init();

    /**
     * 主键值
     * @return the serializable
     */
    public abstract Serializable getId();

    /**
     * 插入（字段选择插入）
     * @return the boolean
     */
    public M insert() {
        return this.insert(MongoProviderFactory.collectionName(this.getClass()));
    }

    /**
     * 插入指定的 collection
     * @param collectionName collection name
     * @return the m
     */
    public M insert(String collectionName) {
        template(this.getClass()).insert(this, collectionName);
        return (M) this;
    }

    /**
     * 插入 OR 更新
     * @param wrapper wrapper
     * @param update  update
     * @return the boolean
     */
    public M insertOrUpdate(@NotNull Wrapper<M> wrapper, Update update) {
        AbstractWrapper abstractWrapper = (AbstractWrapper) wrapper;
        template(this.getClass()).upsert(wrapper.getQuery(), abstractWrapper.getUpdate(), this.getClass());
        if (this.getId() != null) {
            this.updateById();
        } else {
            this.insert();
        }

        return (M) this;
    }

    /**
     * 更新（字段选择更新）
     * @return the boolean
     */
    public M updateById() {
        template(this.getClass()).update(this.getClass());
        return (M) this;
    }

    /**
     * 执行更新
     * @param wrapper wrapper
     * @return the boolean
     */
    public M update(Wrapper<M> wrapper) {
        return (M) this;
    }

    /**
     * Delete boolean. 最终还是解析实体, 获取 _id 来删除数据
     * @return the long
     */
    public Long delete() {
        DeleteResult result = template(this.getClass()).remove(this, MongoProviderFactory.collectionName(this.getClass()));
        return result.getDeletedCount();
    }

    /**
     * 删除记录
     * @param wrapper wrapper
     * @return the long 被删除的数量
     */
    public Long delete(@NotNull Wrapper<M> wrapper) {
        DeleteResult result = template(this.getClass()).remove(wrapper.getQuery(),
                                                               this.getClass(),
                                                               MongoProviderFactory.collectionName(this.getClass()));
        return result.getDeletedCount();
    }

    /**
     * 根据主键删除
     * @return the boolean
     */
    public Long deleteById() {
        return this.deleteById(this.getId());
    }

    /**
     * 根据 ID 删除
     * @param id 主键ID
     * @return the boolean
     */
    public Long deleteById(Serializable id) {
        checkId(id);

        QueryWrapper wrapper = Wrappers.query();
        wrapper.where(MONGO_ID, id);
        return this.delete(wrapper);
    }

    /**
     * 查询所有
     * @return the list
     */
    public List<M> selectAll() {
        return (List<M>) template(this.getClass()).findAll(this.getClass(), MongoProviderFactory.collectionName(this.getClass()));
    }

    /**
     * 根据主键查询
     * @return the t
     */
    public M selectById() {
        return this.selectById(this.getId());
    }

    /**
     * 根据 ID 查询
     * @param id 主键ID
     * @return the t
     */
    public M selectById(Serializable id) {
        checkId(id);

        QueryWrapper wrapper = Wrappers.query();
        wrapper.where(MONGO_ID, id);
        return (M) template(this.getClass()).findById(this.getId(), this.getClass(), MongoProviderFactory.collectionName(this.getClass()));
    }

    /**
     * Select list list
     * @return the list
     */
    public List<M> selectList() {
        QueryWrapper wrapper = Wrappers.query();
        return this.selectList(wrapper);
    }

    /**
     * 查询总记录数
     * @param wrapper wrapper
     * @return the list
     */
    public List<M> selectList(@NotNull Wrapper<M> wrapper) {
        return (List<M>) template(this.getClass()).find(wrapper.getQuery(),
                                                        this.getClass(),
                                                        MongoProviderFactory.collectionName(this.getClass()));
    }

    /**
     * Select one m
     * @return the m
     */
    public M selectOne() {
        Wrapper<M> wrapper = Wrappers.query();
        return this.selectOne(wrapper);
    }

    /**
     * 查询一条记录
     * @param wrapper wrapper
     * @return the t
     */
    public M selectOne(@NotNull Wrapper<M> wrapper) {
        return (M) template(this.getClass()).findOne(wrapper.getQuery(),
                                                     this.getClass(),
                                                     MongoProviderFactory.collectionName(this.getClass()));
    }

    /**
     * 翻页查询
     * @param <E>     parameter
     * @param page    翻页条件
     * @param wrapper wrapper   不含 limit 查询条件
     * @return the e
     */
    public <E extends IPage<M>> E selectPage(@NotNull E page, QueryWrapper<M> wrapper) {
        // 查询总数
        Long count = this.selectCount(wrapper);
        // 计算 page 数据
        page.setTotal(count);
        // 查询指定分页数据
        wrapper.limit(page.getSize(), page.getCurrent());
        page.setRecords(this.selectList(wrapper));
        return page;
    }

    /**
     * Select count long
     * @return the long
     */
    public Long selectCount() {
        QueryWrapper wrapper = Wrappers.query();
        return this.selectCount(wrapper);
    }

    /**
     * 查询总数
     * @param wrapper wrapper
     * @return the integer
     */
    public Long selectCount(@NotNull Wrapper<M> wrapper) {
        return template(this.getClass()).count(wrapper.getQuery(),
                                               this.getClass(),
                                               MongoProviderFactory.collectionName(this.getClass()));
    }

    /**
     * Check id boolean
     * @param id id
     * @return the boolean
     */
    @Contract("null -> fail")
    private static void checkId(Serializable id) {
        if (id == null) {
            throw new MongoException("id 不能为 null");
        }
    }

    /**
     * Exists boolean.
     * @param wrapper the wrapper
     * @return the boolean
     */
    public boolean exists(@NotNull Wrapper<M> wrapper) {
        return template(this.getClass()).exists(wrapper.getQuery(),
                                                this.getClass(),
                                                MongoProviderFactory.collectionName(this.getClass()));
    }

}
