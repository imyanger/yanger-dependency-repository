package com.yanger.general.tools;

import com.yanger.general.constant.Charsets;
import com.yanger.general.constant.StringPool;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.io.FileUtil;

/**
 * @Description hosts 文件操作工具
 * @Author yanger
 * @Date 2020/12/18 18:49
 */
public class HostUtils {

    /** SPLITTER */
    private static final String SPLITTER = " ";

    /**
     * 读取 hosts, 排除注释项, 将一行记录解析为 [ip + 空格 + domain}
     *
     * @return the set
     * @since 1.5.0
     */
    public static Set<String> read() {
        return FileUtil.readLines(getHostFile(), Charsets.UTF_8).stream()
            .filter(it -> !it.trim().matches("(^#.*)|(\\s*)"))
            .map(it -> it.replaceAll("#.*", "").trim().replaceAll("\\s+", SPLITTER))
            .collect(Collectors.toSet());
    }

    /**
     * Combine
     *
     * @param ip     ip
     * @param domain domain
     * @return the string
     * @since 1.5.0
     */
    @Contract(pure = true)
    public static @NotNull String combine(String ip, String domain) {
        if (StringUtils.isBlank(domain)) {
            throw new IllegalArgumentException("ERROR: domain must be specified");
        }
        return (ip + SPLITTER + domain).trim();
    }

    /**
     * Exists
     *
     * @param domain domain
     * @return the boolean
     * @since 1.5.0
     */
    public static boolean exists(String domain) {
        return exists(StringPool.EMPTY, domain);
    }

    /**
     * Exists
     *
     * @param ip     ip
     * @param domain domain
     * @return the boolean
     * @since 1.5.0
     */
    public static boolean exists(String ip, String domain) {
        return exists(read(), ip, domain);
    }

    /**
     * Exists
     *
     * @param records records
     * @param ip      ip
     * @param domain  domain
     * @return the boolean
     * @since 1.5.0
     */
    public static boolean exists(@NotNull Set<String> records, String ip, String domain) {
        String combine = combine(ip, domain);
        for (String record : records) {
            if (record.contains(combine)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get ips
     *
     * @param records records
     * @param domain  domain
     * @return the set
     * @since 1.5.0
     */
    public static @NotNull Set<String> getIps(@NotNull Set<String> records, String domain) {
        Set<String> ips = new HashSet<>(4);

        String combine = combine(StringPool.EMPTY, domain);
        for (String record : records) {
            if (record.contains(combine)) {
                ips.add(ip(record));
            }
        }

        return ips;
    }

    /**
     * Ip
     *
     * @param record record
     * @return the string
     * @since 1.5.0
     */
    public static String ip(@NotNull String record) {
        String[] split = check(record);
        return split[0];
    }

    /**
     * Doamin
     *
     * @param record record
     * @return the string
     * @since 1.5.0
     */
    public static String doamin(@NotNull String record) {
        String[] split = check(record);
        return split[1];
    }

    /**
     * Check
     *
     * @param record record
     * @return the string [ ]
     * @since 1.5.0
     */
    @NotNull
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    private static String[] check(@NotNull String record) {
        String[] split = record.split(SPLITTER);
        if (split.length != 2) {
            throw new IllegalArgumentException("ERROR： ip & domain must be specified");
        }
        return split;
    }

    /**
     * 获取 host 文件路径
     *
     * @return host file
     * @since 1.5.0
     */
    public static @NotNull String getHostFile() {
        String fileName;

        // 判断系统
        if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
            fileName = "/etc/hosts";
        } else {
            fileName = System.getenv("windir") + "\\system32\\drivers\\etc\\hosts";
        }
        return fileName;
    }

    /**
     * 根据输入 IP 和 Domain，删除 host 文件中的某个 host 配置
     *
     * @param ip     ip
     * @param domain domain
     * @return boolean boolean
     * @since 1.5.0
     */
    public static synchronized boolean deleteHost(String ip, String domain) {

        String record = combine(ip, domain);

        if (exists(ip, domain)) {
            Set<String> lines = read();
            lines.removeIf(s -> s.equals(record));
            FileUtil.writeLines(lines, getHostFile(), Charsets.UTF_8);
            return true;
        }
        return false;
    }

    /**
     * 根据输入 IP 和 Domain，更新 host 文件中的某个 host 配置
     *
     * @param ip     ip
     * @param domain domain
     * @return boolean boolean
     * @since 1.5.0
     */
    @Contract("null, _ -> fail")
    public static synchronized boolean updateHost(String ip, String domain) {
        if (StringUtils.isAnyBlank(ip, domain)) {
            throw new IllegalArgumentException("ERROR： ip & domain must be specified");
        }

        String record = combine(ip, domain);

        if (!exists(ip, domain)) {
            Set<String> lines = read();
            lines.add(record);
            FileUtil.writeLines(lines, getHostFile(), Charsets.UTF_8);
            return true;
        }
        return false;
    }


}
