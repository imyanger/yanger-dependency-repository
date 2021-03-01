package com.yanger.starter.cache.support;

import com.alicp.jetcache.spi.ValueDecoderRegister;
import com.alicp.jetcache.support.AbstractValueDecoder;
import com.yanger.starter.basic.annotation.AutoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Slf4j
@AutoService(ValueDecoderRegister.class)
public class ProtobufValueDecoder extends CacheAbstractValueDecoder implements ValueDecoderRegister {

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
    @Override
    public Object doApply(byte[] buffer) {
        return null;
    }


    /**
     * Identity number int
     *
     * @return the int
     */
    @Override
    public int identityNumber() {
        return 0;
    }

    /**
     * Gets decoder *
     *
     * @return the decoder
     */
    @Override
    public AbstractValueDecoder getDecoder() {
        return INSTANCE;
    }

}
