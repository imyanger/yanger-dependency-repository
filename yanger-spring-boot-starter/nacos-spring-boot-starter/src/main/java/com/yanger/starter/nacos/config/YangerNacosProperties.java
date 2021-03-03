package com.yanger.starter.nacos.config;

import com.google.common.collect.Lists;

import com.alibaba.nacos.api.config.listener.Listener;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import lombok.Data;

import static com.alibaba.nacos.api.PropertyKeyConst.NAMESPACE;
import static com.alibaba.nacos.api.PropertyKeyConst.SERVER_ADDR;

/**
 * @Description 直接使用 Nacos API 的配置类
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = YangerNacosProperties.PREFIX)
public class YangerNacosProperties {

    /** PREFIX */
    public static final String PREFIX = "yanger.nacos";

    /** Config */
    // @Resource
    // public Config config;

    /** Discovery */
    // @Resource
    // public Discovery discovery;

    /** 监听器 */
    private List<Listener> listener = Lists.newArrayList();

    /** 是否自动向 nacos 创建配置 (默认为 false) */
    private Boolean enableAutoCreateConfig = false;

    /** 是否优先从 Nacos 中读取配置 (默认为 true) */
    private Boolean enableNacosConfig;

    /** 分组 */
    private String group;

    /** 命名空间 */
    private String namespace;

    /** 地址 */
    private String address;

    /** dataId */
    private String dataId;


    // @Data
    // @Validated
    // @Component
    // @ConfigurationProperties(prefix = Config.PREFIX)
    // public static class Config {
    //     /** PREFIX */
    //     public static final String PREFIX = "yanger.nacos.config";
    //
    //     /** 转换 nacos config 配置, 不需要配置, 这里 configAddr 自动被 spring 注入了 nacos 的配置 */
    //     private String serverAddr = ConfigDefaultValue.NACOS_SERVER;
    //     /** Namespace */
    //     private String namespace = App.FAKH_NAME_SPACE;
    //     /** Group */
    //     private String group = "DEFAULT_GROUP";
    //     /** Data id */
    //     private String dataId;
    // }


    // @Data
    // @Component
    // @ConfigurationProperties(prefix = Discovery.PREFIX)
    // public static class Discovery {
    //     /** PREFIX */
    //     public static final String PREFIX = "yanger.nacos.discovery";
    //     /** 服务发现地址 */
    //     private String serverAddr = ConfigDefaultValue.NACOS_SERVER;
    //     /** Namespace */
    //     private String namespace = App.FAKH_NAME_SPACE;
    //     /** Group */
    //     private String group = "DEFAULT_GROUP";
    // }
    public Properties assembleConfigServiceProperties() {
        Properties properties = new Properties();
        properties.put(SERVER_ADDR, Objects.toString(this.address, ""));
        properties.put(NAMESPACE, Objects.toString(this.namespace, ""));
        return properties;
    }

    /**
     * Assemble config service properties properties
     *
     * @return the properties

     */
    // public Properties assembleConfigServiceProperties() {
    //     Properties properties = new Properties();
    //     properties.put(SERVER_ADDR, Objects.toString(this.config.getServerAddr(), ""));
    //     properties.put(NAMESPACE, Objects.toString(this.config.getNamespace(), ""));
    //     return properties;
    // }

    /**
     * Assemble discovery service properties properties
     *
     * @return the properties

     */
    // public Properties assembleDiscoveryServiceProperties() {
    //     Properties properties = new Properties();
    //     properties.put(SERVER_ADDR, Objects.toString(this.discovery.getServerAddr(), ""));
    //     properties.put(NAMESPACE, Objects.toString(this.discovery.getNamespace(), ""));
    //     return properties;
    // }

}
