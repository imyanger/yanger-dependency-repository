package com.fkhwl.starter.id.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 分布式 id 生成服务发布方式</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:18
 * @since 1.5.0
 */
@Getter
@AllArgsConstructor
public enum DeployType {
    /** 内嵌: 直接使用 starter, 需要配置本地的机器 id */
    EMBED(0),
    /** REST 服务: 使用 RESTFul API 的方式发布服务 */
    REST(1),
    /** 中心服务: 使用 client SDK 调用 RPC 服务 */
    CENTER(2);

    /** Type */
    private final Integer type;
}
