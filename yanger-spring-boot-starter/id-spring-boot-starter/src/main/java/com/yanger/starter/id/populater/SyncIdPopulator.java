package com.yanger.starter.id.populater;

import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.entity.IdMeta;
import org.jetbrains.annotations.NotNull;

/**
 * 通过 synchronized 生成时间和序列号
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class SyncIdPopulator extends BasePopulator {

    public SyncIdPopulator() {
        super();
    }

    /**
     * Populate id
     * @param id     id
     * @param idMeta id meta
     */
    @Override
    public synchronized void populateId(@NotNull Id id, IdMeta idMeta) {
        super.populateId(id, idMeta);
    }

}
