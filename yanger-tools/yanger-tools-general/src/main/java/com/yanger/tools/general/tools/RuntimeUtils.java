package com.yanger.tools.general.tools;

import com.yanger.tools.general.constant.CharPool;
import com.yanger.tools.general.constant.StringPool;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;

/**
 * 运行时工具类
 * @Author yanger
 * @Date 2020/12/22 11:03
 */
@UtilityClass
public class RuntimeUtils {

    /** cpu 数量 */
    private static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /** pid */
    private static volatile int pid = -1;

    /**
     * 获得当前进程的PID
     * @return {@link int} 进程 id，当失败时返回-1
     * @Author yanger
     * @Date 2022/01/21 21:39
     */
    public static int getPid() {
        if (pid > 0) {
            return pid;
        }
        // something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        int index = jvmName.indexOf(CharPool.AT);
        if (index > 0) {
            pid = NumberUtils.toInt(jvmName.substring(0, index), -1);
            return pid;
        }
        return -1;
    }

    /**
     * 返回应用启动到现在的时间
     * @return {@link Duration}
     * @Author yanger
     * @Date 2022/01/21 21:40
     */
    public static Duration getUpTime() {
        long upTime = ManagementFactory.getRuntimeMXBean().getUptime();
        return Duration.ofMillis(upTime);
    }

    /**
     * 返回输入的JVM参数列表
     * @return {@link String}
     * @Author yanger
     * @Date 2022/01/21 21:40
     */
    @NotNull
    public static String getJvmArguments() {
        List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return StringUtils.join(vmArguments, StringPool.SPACE);
    }

    /**
     * 获取CPU核数
     * @return {@link int}
     * @Author yanger
     * @Date 2022/01/21 21:40
     */
    @Contract(pure = true)
    public static int getCpuNum() {
        return CPU_NUM;
    }

}
