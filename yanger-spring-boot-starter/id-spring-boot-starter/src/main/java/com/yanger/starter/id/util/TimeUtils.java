package com.yanger.starter.id.util;

import com.yanger.starter.id.enums.IdType;
import com.yanger.tools.general.format.StringFormat;
import lombok.extern.slf4j.Slf4j;

/**
 * 时间工具类
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class TimeUtils {
    
    /** EPOCH */
    public static final long EPOCH = 1420041600000L;


    /**
     * 验证时间是否被调慢.
     * @param lastTimestamp last timestamp
     * @param timestamp     timestamp
     */
    public static void validateTimestamp(long lastTimestamp, long timestamp) {
        if (timestamp < lastTimestamp) {
            if (log.isErrorEnabled()) {
                log.error(StringFormat.format("Clock moved backwards.  Refusing to generate id for {} second/milisecond.",
                                              lastTimestamp - timestamp));
            }

            throw new IllegalStateException(
                StringFormat.format("Clock moved backwards.  Refusing to generate id for {} second/milisecond.",
                                    lastTimestamp - timestamp));
        }
    }

    /**
     * 使用自旋锁处理在一秒内生成的所有 id 都被使用的情况(必须等到下一秒再生成 id)
     * @param lastTimestamp last timestamp
     * @param idType        id type
     * @return the long
     */
    public static long tillNextTimeUnit(long lastTimestamp, IdType idType) {
        if (log.isInfoEnabled()) {
            log.info(StringFormat.format("Ids are used out during {}. Waiting till next second/milisencond.",
                                         lastTimestamp));
        }

        long timestamp = TimeUtils.genTime(idType);
        while (timestamp <= lastTimestamp) {
            timestamp = TimeUtils.genTime(idType);
        }

        if (log.isInfoEnabled()) {
            log.info(StringFormat.format("Next second/milisencond {} is up.", timestamp));
        }

        return timestamp;
    }

    /**
     * 通过 {@link IdType} 确定产生的时间单位, 并使用 {@link TimeUtils#EPOCH} 压缩时间.
     * @param idType id type
     * @return the long
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
