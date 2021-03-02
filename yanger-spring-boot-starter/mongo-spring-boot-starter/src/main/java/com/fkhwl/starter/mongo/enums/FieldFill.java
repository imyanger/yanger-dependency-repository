package com.fkhwl.starter.mongo.enums;/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author yanghao
 * @version x.x.x
 * @email "mailto:yanghao@fkhwl.com"
 * @date 2021.03.02 11:53
 * @since x.x.x
 */

/**
 * @Description
 * @Author yanger
 * @Date 2021/3/2 11:53
 */
public enum FieldFill {
    /**
     * 默认不处理
     */
    DEFAULT,
    /**
     * 插入时填充字段
     */
    INSERT,
    /**
     * 更新时填充字段
     */
    UPDATE,
    /**
     * 插入和更新时填充字段
     */
    INSERT_UPDATE
}
