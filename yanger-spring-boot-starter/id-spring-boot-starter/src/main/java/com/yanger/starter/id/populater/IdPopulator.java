package com.yanger.starter.id.populater;

import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.entity.IdMeta;
import org.jetbrains.annotations.NotNull;

/**
 * 根据时间和序列号填充 id
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface IdPopulator {

    /**
     * Populate id
     * @param id     id
     * @param idMeta id meta
     */
    void populateId(@NotNull Id id, IdMeta idMeta);

}
