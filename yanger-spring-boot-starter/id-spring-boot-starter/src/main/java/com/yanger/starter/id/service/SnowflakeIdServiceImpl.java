package com.yanger.starter.id.service;

import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.enums.DeployType;
import com.yanger.starter.id.enums.IdType;
import com.yanger.starter.id.exception.IdWorkerException;
import com.yanger.tools.general.format.StringFormat;

import java.util.Date;

import cn.hutool.core.date.SystemClock;

/**
 * Twitter的Snowflake 算法<br>
 * 分布式系统中, 有一些需要使用全局唯一ID的场景, 有些时候我们希望能使用一种简单一些的ID, 并且希望ID能够按照时间有序生成.
 *
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 *
 * <pre>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * <p>
 * <p>
 * 第一位为未使用(符号位表示正数), 接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 * 参考：http://www.cnblogs.com/relucent/p/4955340.html
 *
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class SnowflakeIdServiceImpl implements IdService {

    /** 设置一个时间初始值 2^41 - 1, 差不多可以用 69 年 */
    private final long twepoch;

    /** 机器ID  2 进制 5 位, 32位 减掉 1 位共 31 个 */
    private final long machineId;

    /** 机房 ID 2 进制 5 位, 32 位减掉 1 位共 31 个 */
    private final long dataCenterId;

    /** 代表一毫秒内生成的多个 id 的最新序号, 12 位 4096 -1 = 4095 个 */
    private long sequence = 0L;

    /** 5 位的机器 id */
    private final long machineIdBits = 5L;

    /** 5 位的机房 id */
    private final long dataCenterIdBits = 5L;

    /** 最大支持机器节点数 0~31, 一共 32 个, 最大支持数据中心节点数 0~31, 一共 32 个 */
    @SuppressWarnings(value = {"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private final long maxMachineId = -1L ^ (-1L << this.machineIdBits);

    /** 最大支持数据中心节点数 0~31, 一共 32 个, 最大支持数据中心节点数 0~31, 一共 32 个 */
    @SuppressWarnings(value = {"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private final long maxDataCenterId = -1L ^ (-1L << this.dataCenterIdBits);

    /** 序列号12位, 每毫秒内产生的id数 2 的 12 次方 */
    private final long sequenceBits = 12L;

    /** Sequence shift */
    private final long sequenceShift = 0L;

    /** 机器节点左移12位 */
    private final long machineIdShift = this.sequenceBits;

    /** 数据中心节点左移17位 */
    private final long dataCenterIdShift = this.sequenceBits + this.machineIdBits;

    /** 时间毫秒数左移22位 */
    private final long timestampLeftShift = this.sequenceBits + this.machineIdBits + this.dataCenterIdBits;

    /** 序列号最大值: 4095 */
    @SuppressWarnings(value = {"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private final long sequenceMask = -1L ^ (-1L << this.sequenceBits);

    /** Last timestamp */
    private long lastTimestamp = -1L;

    /** Use system clock */
    private final boolean useSystemClock;

    /**
     * 构造
     *
     * @param machineId    终端ID
     * @param dataCenterId 数据中心ID
     */
    public SnowflakeIdServiceImpl(long machineId, long dataCenterId) {
        this(machineId, dataCenterId, false);
    }

    /**
     * 构造
     *
     * @param machineId        终端ID
     * @param dataCenterId     数据中心ID
     * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
     */
    public SnowflakeIdServiceImpl(long machineId, long dataCenterId, boolean isUseSystemClock) {
        this(null, machineId, dataCenterId, isUseSystemClock);
    }

    /**
     * Snowflake provider
     *
     * @param epochDate        初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId, 慎用
     * @param machineId        工作机器节点id
     * @param dataCenterId     数据中心id
     * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
     */
    public SnowflakeIdServiceImpl(Date epochDate, long machineId, long dataCenterId, boolean isUseSystemClock) {
        if (null != epochDate) {
            this.twepoch = epochDate.getTime();
        } else {
            this.twepoch = 1288834974657L;
        }
        // 检查机房 id 和机器 id 是否超过 31 不能小于 0
        if (machineId > this.maxMachineId || machineId < 0) {
            throw new IllegalArgumentException(StringFormat.format("worker Id can't be greater than {} or less than 0",
                                                                      this.maxMachineId));
        }
        if (dataCenterId > this.maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(StringFormat.format("datacenter Id can't be greater than {} or less than 0",
                                                                      this.maxDataCenterId));
        }
        this.machineId = machineId;
        this.dataCenterId = dataCenterId;
        this.useSystemClock = isUseSystemClock;
    }

    /**
     * 根据 Snowflake 的 ID, 获取机器 id
     *
     * @param id snowflake 算法生成的 id
     * @return 所属机器的id worker id
     */
    public long getMachineId(long id) {
        return id >> this.machineIdShift & ~(-1L << this.machineIdBits);
    }

    /**
     * 根据 Snowflake 的 ID, 获取数据中心 id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心 data center id
     */
    public long getDataCenterId(long id) {
        return id >> this.dataCenterIdShift & ~(-1L << this.dataCenterIdBits);
    }

    /**
     * Get sequence
     *
     * @param id id
     * @return the long
     */
    public long getSequence(long id) {
        return id >> this.sequenceShift & ~(-1L << this.sequenceBits);
    }

    /**
     * 根据 Snowflake 的 ID, 获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间 generate date time
     */
    public long getGenerateDateTime(long id) {
        return (id >> this.timestampLeftShift & ~(-1L << 41L)) + this.twepoch;
    }

    /**
     * 下一个 ID
     *
     * @return ID long
     */
    public synchronized long nextId() {
        // 这儿就是获取当前时间戳, 单位是毫秒
        long timestamp = this.genTime();
        if (timestamp < this.lastTimestamp) {
            // 如果服务器时间有问题(时钟后退) 报错.
            throw new IllegalStateException(StringFormat.format("Clock moved backwards. Refusing to generate id for {}ms",
                                                                   this.lastTimestamp - timestamp));
        }
        // 在同一个毫秒内, 又发送了一个请求生成一个id, 这个时候把 seqence 序号给递增 1, 最多就是 4096
        if (this.lastTimestamp == timestamp) {
            // 一个毫秒内最多只能有 4096 个数字, 无论你传递多少进来, 这个位运算保证始终就是在 4096 这个范围内, 避免 sequence 超过了 4096 这个范围
            this.sequence = (this.sequence + 1) & this.sequenceMask;
            // 当某一毫秒的时间, 产生的 id 数超过 4095, 系统会进入等待, 直到下一毫秒, 系统继续产生ID
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0L;
        }

        // 这儿记录一下最近一次生成 id 的时间戳, 单位是毫秒
        this.lastTimestamp = timestamp;

        // 这儿就是最核心的二进制位运算操作, 生成一个 64bit 的 id
        // 先将当前时间戳左移, 放到 41 bit 那儿; 将机房 id 左移放到 5 bit 那儿; 将机器 id 左移放到 5 bit 那儿; 将序号放最后12 bit
        // 最后拼接起来成一个 64 bit 的二进制数字, 转换成 10 进制就是个 long型
        return ((timestamp - this.twepoch) << this.timestampLeftShift)
               | (this.dataCenterId << this.dataCenterIdShift)
               | (this.machineId << this.machineIdShift)
               | this.sequence;
    }

    /**
     * 下一个ID（字符串形式）
     *
     * @return ID 字符串形式
     */
    public String nextIdStr() {
        return Long.toString(this.nextId());
    }

    /**
     * 循环等待下一个时间
     *
     * @param lastTimestamp 上次记录的时间
     * @return 下一个时间 long
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.genTime();
        while (timestamp <= lastTimestamp) {
            timestamp = this.genTime();
        }
        return timestamp;
    }

    /**
     * 生成时间戳
     *
     * @return 时间戳 long
     */
    private long genTime() {
        return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
    }

    /**
     * 生成分布式 id
     *
     * @return the long
     */
    @Override
    public long genId() {
        return this.nextId();
    }

    /**
     * 反解分布式 id
     *
     * @param id id
     * @return the id
     */
    @Override
    public Id expId(long id) {
        return new Id(this.getMachineId(id), this.getSequence(id), this.getGenerateDateTime(id),
                      DeployType.EMBED.getType(), IdType.MAX_PEAK.getVaule(), 0);
    }

    /**
     * 生成某一时段的 id
     *
     * @param time time
     * @param seq  seq
     * @return the long
     */
    @Override
    public long makeId(long time, long seq) {
        throw new IdWorkerException("暂不支持的操作");
    }

    /**
     * Make id
     *
     * @param time    time
     * @param seq     seq
     * @param machine machine
     * @return the long
     */
    @Override
    public long makeId(long time, long seq, long machine) {
        throw new IdWorkerException("暂不支持的操作");
    }

    /**
     * Make id
     *
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     */
    @Override
    public long makeId(long deployType, long time, long seq, long machine) {
        throw new IdWorkerException("暂不支持的操作");
    }

    /**
     * Make id
     *
     * @param idType     type
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     */
    @Override
    public long makeId(long idType, long deployType, long time, long seq, long machine) {
        throw new IdWorkerException("暂不支持的操作");
    }

    /**
     * Make id
     *
     * @param version    version
     * @param idType     type
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     */
    @Override
    public long makeId(long version, long idType, long deployType, long time, long seq, long machine) {
        throw new IdWorkerException("暂不支持的操作");
    }

    /**
     * 将整型时间戳转换为时间
     *
     * @param time time
     * @return the date
     */
    @Override
    public Date transTime(long time) {
        return new Date(time);
    }
}
