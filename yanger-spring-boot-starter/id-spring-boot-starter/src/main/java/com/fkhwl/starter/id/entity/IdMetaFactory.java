package com.fkhwl.starter.id.entity;

import com.fkhwl.starter.id.enums.IdType;

import org.jetbrains.annotations.Nullable;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:17
 * @since 1.5.0
 */
public class IdMetaFactory {

    /** maxPeak */
    private static final IdMeta MAX_PEAK = new IdMeta((byte) 10, (byte) 20, (byte) 30, (byte) 2, (byte) 1, (byte) 1);

    /** minGranularity */
    private static final IdMeta MIN_GRANULARITY = new IdMeta((byte) 10, (byte) 10, (byte) 40, (byte) 2, (byte) 1, (byte) 1);

    /**
     * Gets id meta *
     *
     * @param idType idType
     * @return the id meta
     * @since 1.5.0
     */
    public static @Nullable IdMeta getIdMeta(IdType idType) {
        if (IdType.MAX_PEAK.equals(idType)) {
            return MAX_PEAK;
        } else if (IdType.MIN_GRANULARITY.equals(idType)) {
            return MIN_GRANULARITY;
        }
        return null;
    }
}
