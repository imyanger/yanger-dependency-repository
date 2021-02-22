package com.fkhwl.starter.id.service;

import com.fkhwl.starter.id.entity.Id;
import com.fkhwl.starter.id.enums.IdType;
import com.fkhwl.starter.id.populater.AtomicIdPopulator;
import com.fkhwl.starter.id.populater.IdPopulator;
import com.fkhwl.starter.id.populater.LockIdPopulator;
import com.fkhwl.starter.id.populater.SyncIdPopulator;
import com.fkhwl.starter.id.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:24
 * @since 1.5.0
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
     *
     * @since 1.5.0
     */
    public IdServiceImpl() {
        super();

        this.initPopulator();
    }

    /**
     * Id service
     *
     * @param type type
     * @since 1.5.0
     */
    public IdServiceImpl(String type) {
        super(type);

        this.initPopulator();
    }

    /**
     * Id service
     *
     * @param type type
     * @since 1.5.0
     */
    public IdServiceImpl(IdType type) {
        super(type);

        this.initPopulator();
    }

    /**
     * 通过 JVM 参数选择生成时间和序列号的算法
     *
     * @since 1.5.0
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
     * @since 1.5.0
     */
    @Override
    protected void populateId(Id id) {
        this.idPopulator.populateId(id, this.idMeta);
    }

    /**
     * Sets id populator *
     *
     * @param idPopulator id populator
     * @since 1.5.0
     */
    public void setIdPopulator(IdPopulator idPopulator) {
        this.idPopulator = idPopulator;
    }
}
