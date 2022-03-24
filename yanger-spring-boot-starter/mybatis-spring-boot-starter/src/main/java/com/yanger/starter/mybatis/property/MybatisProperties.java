package com.yanger.starter.mybatis.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据库配置
 * @Author yanger
 * @Date 2021/1/28 15:35
 */
@Data
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties {

    /** PREFIX */
    public static final String PREFIX = "yanger.mybatis";

    /** sql 日志 */
    private boolean enableLog;

    /** 分页默认起始页 */
    private Long page;

    /** 分页默认大小 */
    private Long limit;

    /** 单页限制 默认不限制 */
    private Long singlePageLimit = -1L;

    /** 是否开启敏感数据加密 */
    private boolean enableSensitive;

    /** 敏感数据加密 AES_KEY */
    private String sensitiveKey = "dKE78Ndp*)^*)(3Hm30)2^(@:34h3M2K";

    /** sql 检查插件 */
    private boolean enableIllegalSqlInterceptor = Boolean.FALSE;

    /** SQL执行分析插件, 拦截一些整表操作 */
    private boolean enableSqlExplainInterceptor = Boolean.FALSE;

}
