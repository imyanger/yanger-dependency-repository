package com.yanger.starter.mongo.conditions;

import com.yanger.starter.mongo.core.MongoQuery;
import com.yanger.starter.mongo.mapper.Model;

import java.io.Serializable;

/**
 * mongodb 搜索语句包装类
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuppressWarnings(value = {"serial", "checkstyle:MethodLimit"})
public abstract class Wrapper<M extends Model<M>> extends MongoQuery implements Serializable {

    /** ID */
    public static final String ID = Model.MONGO_ID;

    /** VERSION */
    public static final String VERSION = "_version";

    /**
     * 实体对象（子类实现）
     * @return 泛型 M
     */
    public abstract M getEntity();

}
