package com.yanger.starter.cache.support;

import com.alicp.jetcache.anno.SerialPolicy;

import org.jetbrains.annotations.Contract;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.function.Function;

/**
 * @Description
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class ProtobufValueSerialPolicy implements SerialPolicy, RedisSerializer<Object> {

    /** JACKSON */
    public static final String PROTOBUF = "PROTOBUF";

    /** Protobuf value decoder */
    private final ProtobufValueDecoder protobufValueDecoder;

    /** Protobuf value encoder */
    private final ProtobufValueEncoder protobufValueEncoder;

    /**
     * Jackson value serial policy
     *
     * @param protobufValueDecoder protobuf value decoder
     * @param protobufValueEncoder protobuf value encoder
     */
    @Contract(pure = true)
    public ProtobufValueSerialPolicy(ProtobufValueDecoder protobufValueDecoder,
                                     ProtobufValueEncoder protobufValueEncoder) {
        this.protobufValueDecoder = protobufValueDecoder;
        this.protobufValueEncoder = protobufValueEncoder;

    }

    /**
     * Encoder function
     *
     * @return the function
     */
    @Override
    public Function<Object, byte[]> encoder() {
        return this.protobufValueEncoder;
    }

    /**
     * Decoder function
     *
     * @return the function
     */
    @Override
    public Function<byte[], Object> decoder() {
        return this.protobufValueDecoder;
    }

    /**
     * Serialize byte [ ]
     *
     * @param o o
     * @return the byte [ ]
     * @throws SerializationException serialization exception
     */
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return this.protobufValueEncoder.apply(o);
    }

    /**
     * Deserialize object
     *
     * @param bytes bytes
     * @return the object
     * @throws SerializationException serialization exception
     */
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return this.protobufValueDecoder.doApply(bytes);
    }

}
