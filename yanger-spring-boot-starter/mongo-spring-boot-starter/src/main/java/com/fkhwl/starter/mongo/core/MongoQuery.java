package com.fkhwl.starter.mongo.core;

import com.fkhwl.starter.mongo.util.FieldConvertUtils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import lombok.Getter;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: ${description}</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:50
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public abstract class MongoQuery {
    /** Query */
    @Getter
    protected Query query;
    /** Update */
    @Getter
    protected Update update;
    /** Criteria */
    @Getter
    protected Criteria criteria;
    /** Field */
    @Getter
    protected Field field;

    /**
     * Mongo query
     */
    public MongoQuery() {
        this.criteria = new Criteria();
        this.query = Query.query(this.criteria);
        this.field = this.query.fields();
    }

    /**
     * Field.
     *
     * @param key the key
     */
    protected void field(String key) {
        this.field.include(key);
    }

    /**
     * And criteria.
     *
     * @param key the key
     * @return the criteria
     */
    protected Criteria and(String key) {
        key = FieldConvertUtils.convert(key);
        return this.criteria.and(key);
    }

}
