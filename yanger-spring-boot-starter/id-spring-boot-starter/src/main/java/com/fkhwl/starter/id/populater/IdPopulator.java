package com.fkhwl.starter.id.populater;

import com.fkhwl.starter.id.entity.Id;
import com.fkhwl.starter.id.entity.IdMeta;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 根据时间和序列号填充 id </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:19
 * @since 1.5.0
 */
public interface IdPopulator {

    /**
     * Populate id
     *
     * @param id     id
     * @param idMeta id meta
     * @since 1.5.0
     */
    void populateId(@NotNull Id id, IdMeta idMeta);

}
