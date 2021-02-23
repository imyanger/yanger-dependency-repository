package com.yanger.starter.id.service;

import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.enums.IdType;
import com.yanger.starter.id.populater.AtomicIdPopulator;
import com.yanger.starter.id.populater.IdPopulator;
import com.yanger.starter.id.populater.LockIdPopulator;
import com.yanger.starter.id.populater.SyncIdPopulator;
import com.yanger.starter.id.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description id 生成服务
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class IdServiceImpl extends AbstractIdServiceImpl {

    /** SYNC_LOCK_IMPL_KEY */
    private static final String SYNC_LOCK_IMPL_KEY = "id.sync.lock.impl.key";

    /** ATOMIC_IMPL_KEY */
    private static final String ATOMIC_IMPL_KEY = "id.atomic.impl.key";

    /** Id populator */
    protected IdPopulator idPopulator;

    /**
     * Id service
     */
    public IdServiceImpl() {
        super();
        this.initPopulator();
    }

    /**
     * Id service
     *
     * @param type type
     */
    public IdServiceImpl(String type) {
        super(type);
        this.initPopulator();
    }

    /**
     * Id service
     *
     * @param type type
     */
    public IdServiceImpl(IdType type) {
        super(type);
        this.initPopulator();
    }

    /**
     * 通过 JVM 参数选择生成时间和序列号的算法
     */
    public void initPopulator() {
        if (this.idPopulator != null) {
            log.info("The " + this.idPopulator.getClass().getCanonicalName() + " is used.");
        } else if (CommonUtils.isPropKeyOn(SYNC_LOCK_IMPL_KEY)) {
            log.info("The SyncIdPopulator is used.");
            this.idPopulator = new SyncIdPopulator();
        } else if (CommonUtils.isPropKeyOn(ATOMIC_IMPL_KEY)) {
            log.info("The AtomicIdPopulator is used.");
            this.idPopulator = new AtomicIdPopulator();
        } else {
            log.info("The default LockIdPopulator is used.");
            this.idPopulator = new LockIdPopulator();
        }
    }

    /**
     * Populate id
     *
     * @param id id
     */
    @Override
    protected void populateId(Id id) {
        this.idPopulator.populateId(id, this.idMeta);
    }

    /**
     * Sets id populator *
     *
     * @param idPopulator id populator
     */
    public void setIdPopulator(IdPopulator idPopulator) {
        this.idPopulator = idPopulator;
    }

}
