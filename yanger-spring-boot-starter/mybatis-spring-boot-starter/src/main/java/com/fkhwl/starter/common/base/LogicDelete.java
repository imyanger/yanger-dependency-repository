package com.fkhwl.starter.common.base;

import com.fkhwl.starter.common.enums.DeleteEnum;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 逻辑删除字段接口 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.22 13:53
 * @since 1.0.0
 */
public interface LogicDelete {
    /** DELETED */
    String DELETED = "deleted";

    /**
     * Gets deleted *
     *
     * @return the deleted
     * @since 1.0.0
     */
    DeleteEnum getDeleted();
}
