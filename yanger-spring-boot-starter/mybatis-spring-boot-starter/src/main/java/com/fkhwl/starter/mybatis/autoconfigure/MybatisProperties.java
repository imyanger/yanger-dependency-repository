package com.fkhwl.starter.mybatis.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 14:47
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties {
    /** PREFIX */
    public static final String PREFIX = "fkh.mybatis";

    /** sql 日志 */
    private boolean enableLog;
    /** 分页默认起始页 */
    private Long page;
    /** 分页默认大小 */
    private Long limit;
    /** 单页限制 默认不限制 */
    private Long singlePageLimit = -1L;
    /** 敏感数据加密 AES_KEY */
    private String sensitiveKey = "rFsHHirtsGuST7HtBzebLge1uVYCg2ZS";
    /** sql 检查插件 */
    private boolean enableIllegalSqlInterceptor = Boolean.FALSE;
    /** SQL执行分析插件, 拦截一些整表操作 */
    private boolean enableSqlExplainInterceptor = Boolean.FALSE;

}
