package com.yanger.starter.id.service;

import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.enums.IdType;
import com.yanger.starter.id.enums.SyncType;
import com.yanger.starter.id.populater.AtomicIdPopulator;
import com.yanger.starter.id.populater.IdPopulator;
import com.yanger.starter.id.populater.LockIdPopulator;
import com.yanger.starter.id.populater.SyncIdPopulator;
import lombok.extern.slf4j.Slf4j;

/**
 * id 生成服务
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public class IdServiceImpl extends AbstractIdServiceImpl {

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
     * @param type type
     */
    public IdServiceImpl(String type) {
        super(type);
        this.initPopulator();
    }

    /**
     * Id service
     * @param type type
     */
    public IdServiceImpl(IdType type) {
        super(type);
        this.initPopulator();
    }

    /**
     * 通过 参数选择生成时间和序列号的算法 的同步类型
     */
    public void initPopulator() {
        String syncType = System.getProperty(ConfigKey.IdConfigKey.SYNC_TYPE);
        if (this.idPopulator == null) {
            if (SyncType.SYNCHRONIZED.name().equalsIgnoreCase(syncType)) {
                log.info("The SyncIdPopulator is used.");
                this.idPopulator = new SyncIdPopulator();
            } else if (SyncType.CAS.name().equalsIgnoreCase(syncType)) {
                log.info("The AtomicIdPopulator is used.");
                this.idPopulator = new AtomicIdPopulator();
            } else {
                log.info("The default LockIdPopulator is used.");
                this.idPopulator = new LockIdPopulator();
            }
        }
    }

    /**
     * Populate id
     * @param id id
     */
    @Override
    protected void populateId(Id id) {
        this.idPopulator.populateId(id, this.idMeta);
    }

    /**
     * Sets id populator
     * @param idPopulator id populator
     */
    public void setIdPopulator(IdPopulator idPopulator) {
        this.idPopulator = idPopulator;
    }

}
