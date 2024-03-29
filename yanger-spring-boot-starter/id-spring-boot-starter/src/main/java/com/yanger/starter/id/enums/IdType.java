package com.yanger.starter.id.enums;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * id 类型
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Getter
public enum IdType {

    /**
     * 最大峰值类型: 采用秒级有序, 秒级时间占用 30 位, 序列号占用 20 位:
     * +--------+-----+------+---------+---------+-------+--------+
     * | 字段   | 版本 | 类型 | 生成方式 | 秒级时间 | 序列号 | 机器 id |
     * +-------+------+-----+---------+---------+-------+--------+
     * | 位 数  |  63 | 62  |  60-61  |  30-59  | 10-29 |   0-9  |
     * +-------+-----+-----+---------+---------+-------+--------+
     */
    MAX_PEAK(0, "max-peak", 30),

    /**
     * 最小粒度类型: 采用毫秒级有序, 毫秒级时间占用 40 位, 序列号占用 10 位:
     * +--------+-----+------+---------+---------+-------+--------+
     * | 字段   | 版本 | 类型 | 生成方式 | 秒级时间 | 序列号 | 机器 id |
     * +-------+------+-----+---------+---------+-------+--------+
     * | 位 数  |  63 | 62  |  60-61  |  20-59  | 10-19 |   0-9  |
     * +-------+-----+-----+---------+---------+-------+--------+
     */
    MIN_GRANULARITY(1, "min-granularity", 40);

    /** Vaule */
    private final Integer vaule;

    /** Name */
    private final String name;

    /** Size */
    private final Integer size;

    @Contract(pure = true)
    IdType(Integer vaule, String name, Integer size) {
        this.vaule = vaule;
        this.name = name;
        this.size = size;
    }

    @Contract(pure = true)
    public long value() {
        if (this == IdType.MIN_GRANULARITY) {
            return 1;
        }
        return 0;
    }

    @Contract(pure = true)
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Parse
     * @param name name
     * @return the id type
     */
    public static @Nullable IdType parse(String name) {
        if (MIN_GRANULARITY.getName().equals(name)) {
            return MIN_GRANULARITY;
        } else if (MAX_PEAK.getName().equals(name)) {
            return MAX_PEAK;
        }
        return null;
    }

    /**
     * Parse
     * @param type type
     * @return the id type
     */
    @Contract(pure = true)
    public static @Nullable IdType parse(long type) {
        if (type == 1) {
            return MIN_GRANULARITY;
        } else if (type == 0) {
            return MAX_PEAK;
        }
        return null;
    }

}
