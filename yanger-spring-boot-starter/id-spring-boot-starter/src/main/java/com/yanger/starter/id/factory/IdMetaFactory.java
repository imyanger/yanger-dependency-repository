package com.yanger.starter.id.factory;

import com.yanger.starter.id.entity.IdMeta;
import com.yanger.starter.id.enums.IdType;

import org.jetbrains.annotations.Nullable;

/**
 * ID 元数据工厂
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class IdMetaFactory {

    /** maxPeak */
    private static final IdMeta MAX_PEAK = new IdMeta((byte) 10, (byte) 20, (byte) 30, (byte) 2, (byte) 1, (byte) 1);

    /** minGranularity */
    private static final IdMeta MIN_GRANULARITY = new IdMeta((byte) 10, (byte) 10, (byte) 40, (byte) 2, (byte) 1, (byte) 1);

    public static @Nullable IdMeta getIdMeta(IdType idType) {
        if (IdType.MAX_PEAK.equals(idType)) {
            return MAX_PEAK;
        } else if (IdType.MIN_GRANULARITY.equals(idType)) {
            return MIN_GRANULARITY;
        }
        return null;
    }

}
