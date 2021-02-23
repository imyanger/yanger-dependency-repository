package com.yanger.starter.id.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 分布式 id 生成服务发布方式
 * @Author yanger
 * @Date 2021/1/28 19:08
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
