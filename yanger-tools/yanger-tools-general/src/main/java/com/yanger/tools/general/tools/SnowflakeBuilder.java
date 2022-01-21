package com.yanger.tools.general.tools;

import cn.hutool.core.lang.Snowflake;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 雪花算法生成全局唯一 id
 * @Author yanger
 * @Date 2020/12/22 10:59
 */
@UtilityClass
public class SnowflakeBuilder {

    /**
     * build 构造
     * @return {@link long}
     * @Author yanger
     * @Date 2022/01/21 21:49
     */
    public static long builder() {
        return new Snowflake(getWorkId(), getDataCenterId()).nextId();
    }

    /**
     * build 构造
     * @param workerId 机器编号
     * @param regionId 区域编号
     * @return {@link long}
     * @Author yanger
     * @Date 2022/01/21 21:49
     */
    public static long builder(long workerId, long regionId) {
        return new Snowflake(workerId, regionId).nextId();
    }

    /**
     * 获取机器编号
     * @return {@link Long}
     * @Author yanger
     * @Date 2022/01/21 21:48
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
     * 获取区域编号
     * @param
     * @return {@link Long}
     * @Author yanger
     * @Date 2022/01/21 21:48
     */
    private static Long getDataCenterId() {
        int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int sums = 0;
        for (int i : ints) {
            sums += i;
        }
        return (long) (sums % 32);
    }

}
