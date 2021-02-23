package com.yanger.starter.id.config;

import com.yanger.starter.id.enums.DeployType;
import com.yanger.starter.id.enums.IdType;
import com.yanger.starter.id.enums.ProviderType;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.concurrent.ThreadLocalRandom;

import lombok.Data;

/**
 * @Description id 配置项
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Data
@Validated
@ConfigurationProperties(prefix = IdProperties.PREFIX)
public class IdProperties {

    /** PREFIX */
    public static final String PREFIX = "yanger.id";

    /** Type */
    public ProviderType providerType = ProviderType.SNOW_FLAKE;

    /** Id type */
    public IdType idType = IdType.MAX_PEAK;

    /** Deploy type */
    public DeployType deployType = DeployType.EMBED;

    /** type = PROPERTY 需要的参数 Machine id */
    public Long machineId = ThreadLocalRandom.current().nextLong(1L, 1024L);

    /** Version */
    public Long version;

    /** type = IP_CONFIGURABLE 需要的参数 ips */
    public String ips;

    /** type = DB 需要的参数 Machine id */
    public String dbUrl;

    /** Db name */
    public String dbName;

    /** Db user */
    public String dbUser;

    /** Db password */
    public String dbPassword;

}
