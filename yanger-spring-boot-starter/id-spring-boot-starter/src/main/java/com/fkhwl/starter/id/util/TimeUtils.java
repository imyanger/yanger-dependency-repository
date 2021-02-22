package com.fkhwl.starter.id.util;

import com.fkhwl.starter.id.enums.IdType;
import com.yanger.tools.general.format.StringFormatter;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:18
 * @since 1.5.0
 */
@Slf4j
public class TimeUtils {
    /** EPOCH */
    public static final long EPOCH = 1420041600000L;


    /**
     * 验证时间是否被调慢.
     *
     * @param lastTimestamp last timestamp
     * @param timestamp     timestamp
     * @since 1.5.0
     */
    public static void validateTimestamp(long lastTimestamp, long timestamp) {
        if (timestamp < lastTimestamp) {
            if (log.isErrorEnabled()) {
                log.error(StringFormatter.format("Clock moved backwards.  Refusing to generate id for {} second/milisecond.",
                                              lastTimestamp - timestamp));
            }

            throw new IllegalStateException(
                StringFormatter.format("Clock moved backwards.  Refusing to generate id for {} second/milisecond.",
                                    lastTimestamp - timestamp));
        }
    }

    /**
     * 使用自旋锁处理在一秒内生成的所有 id 都被使用的情况(必须等到下一秒再生成 id)
     *
     * @param lastTimestamp last timestamp
     * @param idType        id type
     * @return the long
     * @since 1.5.0
     */
    public static long tillNextTimeUnit(long lastTimestamp, IdType idType) {
        if (log.isInfoEnabled()) {
            log.info(StringFormatter.format("Ids are used out during {}. Waiting till next second/milisencond.",
                                         lastTimestamp));
        }

        long timestamp = TimeUtils.genTime(idType);
        while (timestamp <= lastTimestamp) {
            timestamp = TimeUtils.genTime(idType);
        }

        if (log.isInfoEnabled()) {
            log.info(StringFormatter.format("Next second/milisencond {} is up.", timestamp));
        }

        return timestamp;
    }

    /**
     * 通过 {@link IdType} 确定产生的时间单位, 并使用 {@link TimeUtils#EPOCH} 压缩时间.
     *
     * @param idType id type
     * @return the long
     * @since 1.5.0
     */
    public static long genTime(IdType idType) {
        if (idType == IdType.MAX_PEAK) {
            return (System.currentTimeMillis() - TimeUtils.EPOCH) / 1000;
        } else if (idType == IdType.MIN_GRANULARITY) {
            return (System.currentTimeMillis() - TimeUtils.EPOCH);
        }

        return (System.currentTimeMillis() - TimeUtils.EPOCH) / 1000;
    }

}
