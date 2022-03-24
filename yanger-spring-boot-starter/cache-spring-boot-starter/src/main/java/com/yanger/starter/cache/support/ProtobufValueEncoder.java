package com.yanger.starter.cache.support;

import com.alicp.jetcache.support.AbstractValueEncoder;

/**

 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class ProtobufValueEncoder extends AbstractValueEncoder {

    /**
     * Jackson value encoder
     */
    public ProtobufValueEncoder() {
        this(Boolean.parseBoolean(System.getProperty("jetcache.useIdentityNumber", "true")));
    }

    /**
     * Jackson value encoder
     * @param useIdentityNumber use identity number
     */
    private ProtobufValueEncoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    /**
     * Apply byte [ ]
     * @param o o
     * @return the byte [ ]
     */
    @Override
    public byte[] apply(Object o) {
        return new byte[0];
    }

}
