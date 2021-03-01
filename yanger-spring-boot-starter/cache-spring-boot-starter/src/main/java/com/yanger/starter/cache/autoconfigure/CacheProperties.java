package com.yanger.starter.cache.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import io.lettuce.core.ReadFrom;
import lombok.Data;
import lombok.Getter;

/**
 * @Description jetcache 配置
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Data
@ConfigurationProperties(prefix = "jetcache")
public class CacheProperties {

    /** 统计间隔,0 表示不统计 */
    private Integer statIntervalMinutes;

    /** jetcache-anno 把 cacheName 作为远程缓存 key 前缀 */
    private boolean areaInCacheName;

    /** Hide packages  @Cached 和 @CreateCache 自动生成 name 的时候,为了不让 name 太长,hiddenPackages 指定的包名前缀被截掉 */
    private String hidePackages;

    /** Local 本都缓存配置 */
    private Map<String, Area> local;

    /** Remote 远程缓存配置 */
    private Map<String, Area> remote;

    @Data
    static class Area {

        /** 缓存类型：
         * tair、redis（配置：redis.lettuce） 为当前支持的远程缓存;
         * linkedhashmap、caffeine 为当前支持的本地缓存类型
         */
        private String type;

        /**
         * 每个缓存实例的最大元素的全局配置,仅 local 类型的缓存需要指定. 注意是每个缓存实例的限制,而不是全部,
         * 比如这里指定 100,然后用 @CreateCache 创建了两个缓存实例（并且注解上没有设置 localLimit 属性）,那么每个缓存实例的限制都是 100
         */
        private Integer limit;

        /**
         * key 转换器的全局配置,当前只有一个已经实现的 keyConvertor: fastjson.
         * 仅当使用 @CreateCache 且缓存类型为 LOCAL 时可以指定为 none,此时通过 equals 方法来识别 key. 方法缓存必须指定 keyConvertor
         */
        private String keyConvertor;

        /** Value encoder */
        private String valueEncoder;

        /** Value decoder */
        private String valueDecoder;

        /** 以毫秒为单位指定超时时间的全局配置 */
        private Long expireAfterWriteInMillis;

        /** 要 jetcache2.2 以上,以毫秒为单位,指定多长时间没有访问,就让缓存失效,当前只有本地缓存支持. 0 表示不使用这个功能. */
        private Long expireAfterAccessInMillis;

        /** Pool config */
        private Pool poolConfig;

        /** redis://user:password@example.com:6379 会通过数量创建不同的连接方式 */
        private List<String> uri;

        /** Host */
        private String host;

        /** Port */
        private Integer port;

        /** Password */
        private String password;

        /** Database */
        private Integer database = 0;

        /** Mode */
        private String mode;

        /**
         * 指定从哪里读取数据, 此设置用作此连接上的默认读取操作
         *
         * @see ReadFrom
         */
        private ReadFromEnum readFrom;

        /** 集群模式下存在: 最大重定向数 */
        private Integer maxRedirects = -1;
    }

    @Getter
    public enum ReadFromEnum {
        /** Master read from enum */
        master,

        /** Master preferred read from enum */
        masterPreferred,

        /** Slave read from enum */
        slave,

        /** Slave preferred read from enum */
        slavePreferred,

        /** Nearest read from enum */
        nearest
    }

    /**
     * 配置连接池, 需要 common-pool
     */
    @Data
    static class Pool {

        /** 空闲连接的最大数量, 使用负值表示空闲连接的数量不受限制. */
        private Integer maxIdle = 10;

        /** 池中要维护的最小空闲连接数的目标, 此设置只有在它和逐出运行之间的时间均为正值时才有效. */
        private Integer minIdle = 10;

        /** 池在给定时间可以分配的最大连接数, 使用负值表示无限制. */
        private Integer maxTotal = 20;

        /** 当池耗尽时, 连接分配在引发异常之前应阻塞的最长时间, 使用负值可无限期阻止. */
        private Duration maxWait = Duration.ofMillis(10000);

        /** 空闲对象逐出器线程的运行间隔时间, 为正值时, 空闲对象逐出线程启动, 否则不执行空闲对象逐出. */
        private Duration timeBetweenEvictionRuns;
    }

}
