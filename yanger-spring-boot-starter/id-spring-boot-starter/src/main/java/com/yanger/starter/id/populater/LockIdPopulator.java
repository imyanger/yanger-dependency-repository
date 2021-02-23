package com.yanger.starter.id.populater;

import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.entity.IdMeta;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 通过 ReentrantLock 生成时间和序列号
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class LockIdPopulator extends BasePopulator {

    private final Lock lock = new ReentrantLock();

    public LockIdPopulator() {
        super();
    }

    /**
     * Populate id
     *
     * @param id     id
     * @param idMeta id meta
     */
    @Override
    public void populateId(@NotNull Id id, IdMeta idMeta) {
        this.lock.lock();
        try {
            super.populateId(id, idMeta);
        } finally {
            this.lock.unlock();
        }
    }

}
