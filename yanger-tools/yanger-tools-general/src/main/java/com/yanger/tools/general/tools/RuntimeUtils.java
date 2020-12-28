package com.yanger.tools.general.tools;

import com.yanger.tools.general.constant.CharPool;
import com.yanger.tools.general.constant.StringPool;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;

import lombok.experimental.UtilityClass;

/**
 * @Description 运行时工具类
 * @Author yanger
 * @Date 2020/12/22 11:03
 */
@UtilityClass
public class RuntimeUtils {

    /** CPU_NUM */
    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /** pId */
    private static volatile int pId = -1;

    /**
     * 获得当前进程的PID
     * 当失败时返回-1
     *
     * @return pid p id
     */
    public static int getPid() {
        if (pId > 0) {
            return pId;
        }
        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf(CharPool.AT);
        if (index > 0) {
            pId = NumberUtils.toInt(jvmName.substring(0, index), -1);
            return pId;
        }
        return -1;
    }

    /**
     * 返回应用启动到现在的时间
     *
     * @return {Duration}
     */
    public static Duration getUpTime() {
        long upTime = ManagementFactory.getRuntimeMXBean().getUptime();
        return Duration.ofMillis(upTime);
    }

    /**
     * 返回输入的JVM参数列表
     *
     * @return jvm参数 jvm arguments
     */
    @NotNull
    public static String getJvmArguments() {
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return StringUtils.join(vmArguments, StringPool.SPACE);
    }

    /**
     * 获取CPU核数
     *
     * @return cpu count
     */
    @Contract(pure = true)
    public static int getCpuNum() {
        return CPU_NUM;
    }

}
