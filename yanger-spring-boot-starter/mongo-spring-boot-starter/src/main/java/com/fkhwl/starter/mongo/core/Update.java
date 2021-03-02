package com.fkhwl.starter.mongo.core;

import com.fkhwl.starter.mongo.util.FieldConvertUtils;

import org.jetbrains.annotations.Contract;

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
public class Update {

    /**
     * The Update.
     */
    private final org.springframework.data.mongodb.core.query.Update update;

    /**
     * Instantiates a new Update.
     *
     * @param update the update
     */
    @Contract(pure = true)
    public Update(org.springframework.data.mongodb.core.query.Update update) {
        this.update = update;
    }

    /**
     * Set org . springframework . data . mongodb . core . query . update.
     *
     * @param key   the key
     * @param value the value
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update set(String key, Object value) {
        key = FieldConvertUtils.convert(key);
        return this.update.set(key, value);
    }

    /**
     * Sets on insert.
     *
     * @param key   the key
     * @param value the value
     * @return the on insert
     */
    public org.springframework.data.mongodb.core.query.Update setOnInsert(String key, Object value) {
        key = FieldConvertUtils.convert(key);
        return this.update.setOnInsert(key, value);
    }

    /**
     * Unset org . springframework . data . mongodb . core . query . update.
     *
     * @param key the key
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update unset(String key) {
        key = FieldConvertUtils.convert(key);
        return this.update.unset(key);
    }

    /**
     * Inc org . springframework . data . mongodb . core . query . update.
     *
     * @param key the key
     * @param inc the inc
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update inc(String key, Number inc) {
        key = FieldConvertUtils.convert(key);
        return this.update.inc(key, inc);
    }

    /**
     * Push org . springframework . data . mongodb . core . query . update.
     *
     * @param key   the key
     * @param value the value
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update push(String key, Object value) {
        key = FieldConvertUtils.convert(key);
        return this.update.push(key, value);
    }

    /**
     * Push all org . springframework . data . mongodb . core . query . update.
     *
     * @param key    the key
     * @param values the values
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update pushAll(String key, Object[] values) {
        key = FieldConvertUtils.convert(key);
        org.springframework.data.mongodb.core.query.Update.PushOperatorBuilder builder = this.update.push(key);
        return builder.each(values);
    }

    /**
     * Add to set org . springframework . data . mongodb . core . query . update.
     *
     * @param key   the key
     * @param value the value
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update addToSet(String key, Object value) {
        key = FieldConvertUtils.convert(key);
        return this.update.addToSet(key, value);
    }

    /**
     * Pop org . springframework . data . mongodb . core . query . update.
     *
     * @param key the key
     * @param pos the pos
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update pop(String key,
                                                                  org.springframework.data.mongodb.core.query.Update.Position pos) {
        key = FieldConvertUtils.convert(key);
        return this.update.pop(key, pos);
    }

    /**
     * Pull org . springframework . data . mongodb . core . query . update.
     *
     * @param key   the key
     * @param value the value
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update pull(String key, Object value) {
        key = FieldConvertUtils.convert(key);
        return this.update.pull(key, value);
    }

    /**
     * Pull all org . springframework . data . mongodb . core . query . update.
     *
     * @param key    the key
     * @param values the values
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update pullAll(String key, Object[] values) {
        key = FieldConvertUtils.convert(key);
        return this.update.pullAll(key, values);
    }

    /**
     * Rename org . springframework . data . mongodb . core . query . update.
     *
     * @param oldName the old name
     * @param newName the new name
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update rename(String oldName, String newName) {
        oldName = FieldConvertUtils.convert(oldName);
        newName = FieldConvertUtils.convert(newName);
        return this.update.rename(oldName, newName);
    }

    /**
     * Current date org . springframework . data . mongodb . core . query . update.
     *
     * @param key the key
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update currentDate(String key) {
        key = FieldConvertUtils.convert(key);
        return this.update.currentDate(key);
    }

    /**
     * Current timestamp org . springframework . data . mongodb . core . query . update.
     *
     * @param key the key
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update currentTimestamp(String key) {
        key = FieldConvertUtils.convert(key);
        return this.update.currentTimestamp(key);
    }

    /**
     * Multiply org . springframework . data . mongodb . core . query . update.
     *
     * @param key        the key
     * @param multiplier the multiplier
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update multiply(String key, Number multiplier) {
        key = FieldConvertUtils.convert(key);
        return this.update.multiply(key, multiplier);
    }

    /**
     * Max org . springframework . data . mongodb . core . query . update.
     *
     * @param key   the key
     * @param value the value
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update max(String key, Object value) {
        key = FieldConvertUtils.convert(key);
        return this.update.max(key, value);
    }

    /**
     * Min org . springframework . data . mongodb . core . query . update.
     *
     * @param key   the key
     * @param value the value
     * @return the org . springframework . data . mongodb . core . query . update
     */
    public org.springframework.data.mongodb.core.query.Update min(String key, Object value) {
        key = FieldConvertUtils.convert(key);
        return this.update.min(key, value);
    }
}
