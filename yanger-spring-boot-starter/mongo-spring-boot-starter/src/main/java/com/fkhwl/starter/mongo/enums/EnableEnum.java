package com.fkhwl.starter.mongo.enums;/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author yanghao
 * @version x.x.x
 * @email "mailto:yanghao@fkhwl.com"
 * @date 2021.03.02 16:20
 * @since x.x.x
 */

import com.yanger.starter.basic.enums.SerializeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description
 * @Author yanger
 * @Date 2021/3/2 16:20
 */
@Getter
@AllArgsConstructor
public enum EnableEnum implements SerializeEnum<Boolean> {
    /** On enable enum */
    ON(Boolean.TRUE, "可用状态"),
    /** Off enable enum */
    OFF(Boolean.FALSE, "不可用状态");

    /** 数据库存储的值 */
    private final Boolean value;
    /** 枚举描述 */
    private final String desc;
}

