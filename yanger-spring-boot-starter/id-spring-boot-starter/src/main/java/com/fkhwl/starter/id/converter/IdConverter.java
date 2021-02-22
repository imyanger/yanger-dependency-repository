package com.fkhwl.starter.id.converter;

import com.fkhwl.starter.id.entity.Id;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: ID 对象和长整型 id 互转</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:17
 * @since 1.5.0
 */
public interface IdConverter {

    /**
     * Convert
     *
     * @param id id
     * @return the long
     * @since 1.5.0
     */
    long convert(Id id);

    /**
     * Convert
     *
     * @param id id
     * @return the id
     * @since 1.5.0
     */
    Id convert(long id);

}
