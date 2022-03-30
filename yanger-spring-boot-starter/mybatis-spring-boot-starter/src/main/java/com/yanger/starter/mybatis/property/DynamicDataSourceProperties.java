package com.yanger.starter.mybatis.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 动态多数据源配置：
 *  1. 开启 dynamicEnable，表示使用多数据源，需要设置 dynamic 多数据源信息，切需要其中一个（唯一）数据源 defaultEnable = true 来标识为默认数据源
 *  2. 开启了 dynamicReadWriteEnable，表示开启读写分离，需要设置 dynamicRead 和 dynamicWrite
 *  3. 同时开启 dynamicEnable 和 dynamicReadWriteEnable 时，多数据眼都会生效，若不 使用 @DS 注解指定，则默认按读写分离策略存储
 *  4. 开启 dynamicPackageEnable 则开启按包匹配数据源，需要设置 dynamic 数据源的 effectPackage 来指定数据源在哪些包下生效
 *  5. 同时开启 dynamicReadWriteEnable 和 dynamicPackageEnable 后，dynamicPackageEnable 具有更高优先级，即先按 effectPackage 来选择数据源
 *
 *  PS:
 *    建议非必要情况，dynamicReadWriteEnable 与 dynamicPackageEnable 不要同时使用
 * @Author yanger
 * @Date 2022/3/25/025 18:22
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DynamicDataSourceProperties {

    /** 开启动态多数据源 */
    private Boolean dynamicEnable = false;

    /** 多数据源 */
    private Map<String, DataSourceDetail> dynamic;

    /** 开启读写分离 */
    private Boolean dynamicReadWriteEnable = false;

    /** 动态读库 */
    private DataSourceDetail dynamicRead;

    /** 动态写库 */
    private DataSourceDetail dynamicWrite;

    /** 开启按包读取数据源 */
    private Boolean dynamicPackageEnable = false;

}
