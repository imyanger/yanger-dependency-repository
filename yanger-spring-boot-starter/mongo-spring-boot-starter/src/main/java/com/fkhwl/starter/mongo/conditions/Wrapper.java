package com.fkhwl.starter.mongo.conditions;

import com.fkhwl.starter.mongo.core.MongoQuery;
import com.fkhwl.starter.mongo.mapper.Model;

import java.io.*;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: mongodb 搜索语句包装类</p>
 *
 * @param <M> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:41
 * @since 1.0.0
 */
@SuppressWarnings(value = {"serial", "checkstyle:MethodLimit"})
public abstract class Wrapper<M extends Model<M>> extends MongoQuery implements Serializable {
    /** ID */
    public static final String ID = Model.MONGO_ID;
    /** VERSION */
    public static final String VERSION = "_version";

    /**
     * 实体对象（子类实现）
     *
     * @return 泛型 M
     * @since 1.0.0
     */
    public abstract M getEntity();

}
