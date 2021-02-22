package com.fkhwl.starter.cache.support;

import com.alicp.jetcache.support.AbstractValueEncoder;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: todo-dong4j : (2020年02月12日 11:43) [未完成] </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.11 22:24
 * @since 1.0.0
 */
public class ProtobufValueEncoder extends AbstractValueEncoder {

    /**
     * Jackson value encoder
     *
     * @since 1.0.0
     */
    public ProtobufValueEncoder() {
        this(Boolean.parseBoolean(System.getProperty("jetcache.useIdentityNumber", "true")));
    }

    /**
     * Jackson value encoder
     *
     * @param useIdentityNumber use identity number
     * @since 1.0.0
     */
    private ProtobufValueEncoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    /**
     * Apply byte [ ]
     *
     * @param o o
     * @return the byte [ ]
     * @since 1.0.0
     */
    @Override
    public byte[] apply(Object o) {
        return new byte[0];
    }
}
