package com.fkhwl.starter.common.base;

import java.util.Date;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 审计字段接口 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.22 13:53
 * @since 1.0.0
 */
public interface AuditTime {
    /** CREATE_TIME */
    String CREATE_TIME = "create_time";
    /** UPDATE_TIME */
    String UPDATE_TIME = "update_time";

    /**
     * Gets create time *
     *
     * @return the create time
     * @since 1.0.0
     */
    Date getCreateTime();

    /**
     * Gets update time *
     *
     * @return the update time
     * @since 1.0.0
     */
    Date getUpdateTime();

}
