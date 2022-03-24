package com.yanger.starter.cache.support;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.function.Function;

/**

 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class FastjsonKeyConvertor implements Function<Object, Object>, RedisSerializer<Object> {

    /** INSTANCE */
    public static final FastjsonKeyConvertor INSTANCE = new FastjsonKeyConvertor();

    /**
     * Apply
     * @param originalKey original key
     * @return the object
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
     * @param s s
     * @return the byte [ ]
     * @throws SerializationException serialization exception
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
     * @param bytes bytes
     * @return the string
     * @throws SerializationException serialization exception
     */
    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return JSON.toJSONString(bytes);
    }

}

