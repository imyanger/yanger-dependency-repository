package com.yanger.starter.id.populater;

import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.entity.IdMeta;
import com.yanger.starter.id.enums.IdType;
import com.yanger.starter.id.util.TimeUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * 基本的生成时间和序列号
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public abstract class BasePopulator implements IdPopulator, ResetPopulator {

    /** Sequence */
    protected long sequence = 0;

    /** Last timestamp */
    protected long lastTimestamp = -1;

    @Contract(pure = true)
    public BasePopulator() {
        super();
    }

    /**
     * 检查当前时间是否已经到了下一个时间单位:
     * 为 true 则将序列号清零;
     * 为 false 则对序列号进行累加, 如果累加后越界则需要等待下一秒再产生 id.
     *
     * @param id     id
     * @param idMeta id meta
     */
    @Override
    public void populateId(@NotNull Id id, IdMeta idMeta) {
        long timestamp = TimeUtils.genTime(IdType.parse(id.getIdType()));
        TimeUtils.validateTimestamp(this.lastTimestamp, timestamp);

        if (timestamp == this.lastTimestamp) {
            this.sequence++;
            this.sequence &= idMeta.getSeqBitsMask();
            if (this.sequence == 0) {
                timestamp = TimeUtils.tillNextTimeUnit(this.lastTimestamp, IdType.parse(id.getIdType()));
            }
        } else {
            this.lastTimestamp = timestamp;
            this.sequence = 0;
        }

        id.setSeq(this.sequence);
        id.setTime(timestamp);
    }

    @Override
    public void reset() {
        this.sequence = 0;
        this.lastTimestamp = -1;
    }

}
