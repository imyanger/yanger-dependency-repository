package com.fkhwl.starter.cache.support;

import com.alibaba.fastjson.JSON;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.function.Function;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @versio 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.09.23 09:35
 * @since 1.6.0
 */
public class FastjsonKeyConvertor implements Function<Object, Object>, RedisSerializer<Object> {

    /** INSTANCE */
    public static final FastjsonKeyConvertor INSTANCE = new FastjsonKeyConvertor();

    /**
     * Apply
     *
     * @param originalKey original key
     * @return the object
     * @since 1.6.0
     */
    @Override
    public Object apply(Object originalKey) {
        if (originalKey == null) {
            return null;
        }
        if (originalKey instanceof String) {
            return originalKey;
        }
        return JSON.toJSONString(originalKey);
    }

    /**
     * Serialize
     *
     * @param s s
     * @return the byte [ ]
     * @throws SerializationException serialization exception
     * @since 1.6.0
     */
    @Override
    public byte[] serialize(Object s) throws SerializationException {
        if (s == null) {
            return new byte[0];
        }
        if (s instanceof String) {
            return ((String) s).getBytes();
        }

        return JSON.toJSONBytes(s);
    }

    /**
     * Deserialize
     *
     * @param bytes bytes
     * @return the string
     * @throws SerializationException serialization exception
     * @since 1.6.0
     */
    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return JSON.toJSONString(bytes);
    }
}

