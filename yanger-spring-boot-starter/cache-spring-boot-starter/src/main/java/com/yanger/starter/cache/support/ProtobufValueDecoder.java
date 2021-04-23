package com.yanger.starter.cache.support;

import com.yanger.starter.basic.annotation.AutoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Slf4j
@AutoService(ProtobufValueDecoder.class)
public class ProtobufValueDecoder extends CacheAbstractValueDecoder {

    /** INSTANCE */
    public static final ProtobufValueDecoder INSTANCE = new ProtobufValueDecoder();

    /**
     * Protobuf value decoder
     */
    public ProtobufValueDecoder() {
        this(Boolean.parseBoolean(System.getProperty("jetcache.useIdentityNumber", "true")));
    }

    /**
     * Protobuf value decoder
     *
     * @param useIdentityNumber use identity number
     */
    private ProtobufValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    /**
     * Do apply object
     *
     * @param buffer buffer
     * @return the object
     * @throws Exception exception
     */
    public Object doApply(byte[] buffer) {
        return null;
    }

}
