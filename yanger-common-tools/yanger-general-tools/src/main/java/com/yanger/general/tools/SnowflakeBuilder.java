package com.yanger.general.tools;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.*;

import cn.hutool.core.lang.Snowflake;
import lombok.experimental.UtilityClass;

/**
 * @Description 雪花算法生成全局唯一 id
 * @Author yanger
 * @Date 2020/12/22 10:59
 */
@UtilityClass
public class SnowflakeBuilder {

    /**
     * 活动机器编号
     *
     * @return work id
     */
    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = org.apache.commons.lang3.StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums += b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            // 如果获取失败, 则使用随机数备用
            return RandomUtils.nextLong(0, 31);
        }
    }

    /**
     * 区域编号
     *
     * @return data center id
     */
    private static Long getDataCenterId() {
        int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int sums = 0;
        for (int i : ints) {
            sums += i;
        }
        return (long) (sums % 32);

    }

    /**
     * Builder long
     *
     * @return the long
     */
    public static long builder() {
        return new Snowflake(getWorkId(), getDataCenterId()).nextId();
    }

    /**
     * Builder long
     *
     * @param workerId worker id
     * @param regionId region id
     * @return the long
     */
    public static long builder(long workerId, long regionId) {
        return new Snowflake(workerId, regionId).nextId();
    }

}
