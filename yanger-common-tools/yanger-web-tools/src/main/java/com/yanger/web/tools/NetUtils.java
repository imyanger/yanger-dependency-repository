package com.yanger.web.tools;

import com.yanger.general.constant.StringPool;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.*;

import javax.servlet.http.HttpServletRequest;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description NET工具类
 * @Author yanger
 * @Date 2020/12/21 10:30
 */
@Slf4j
@UtilityClass
public class NetUtils extends INetUtils {

    /** LOCAL_HOST */
    public static final String LOCAL_HOST = INetUtils.LOCALHOST_VALUE;

    /** MIN_PORT_NUMBER */
    public static final int MIN_PORT_NUMBER = INetUtils.MIN_PORT;

    /** MAX_PORT_NUMBER */
    public static final int MAX_PORT_NUMBER = INetUtils.MAX_PORT;

    /**
     * Gets local ip addr.
     *
     * @return the local ip addr
     * @deprecated use {@link NetUtils#getLocalHost()}
     */
    @Nullable
    @Deprecated
    public static String getLocalIpAddr() {
        return getLocalHost();
    }

    /**
     * 获取 服务器 hostname
     *
     * @return hostname host name
     * @deprecated use {@link INetUtils#getLocalAddress()}
     */
    @Deprecated
    public static String getHostName() {
        return getLocalAddress().getHostName();
    }

    /**
     * 尝试端口时候被占用
     *
     * @param port 端口号
     * @return boolean 没有被占用: true, 被占用: false
     * @deprecated use {@link NetUtils#available(int)}
     */
    @Deprecated
    public static boolean tryPort(int port) {
        return available(port);
    }

    /**
     * 检查本机 TCP/UDP 端口是否可用, 可用返回 true, 否则返回 false
     *
     * @param port port
     * @return the boolean
     */
    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }
        try (ServerSocket ss = new ServerSocket(port);
             DatagramSocket ds = new DatagramSocket(port)) {
            ss.setReuseAddress(true);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    /**
     * 获取请求方 ip
     *
     * @param request HttpServletRequest
     * @return ip string
     */
    @SuppressWarnings("PMD.UndefineMagicConstantRule")
    public static String ip(@NotNull HttpServletRequest request) {
        String ipAddress;
        ipAddress = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCAL_HOST.equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException ignored) {
                }
                ipAddress = inetAddress != null ? inetAddress.getHostAddress() : null;
            }
        }
        // 对于通过多个代理的情况,第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(StringPool.COMMA) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(StringPool.COMMA));
            }
        }
        if (StringUtils.isEmpty(ipAddress)) {
            return "NONE";
        }
        return ipAddress;
    }

}
