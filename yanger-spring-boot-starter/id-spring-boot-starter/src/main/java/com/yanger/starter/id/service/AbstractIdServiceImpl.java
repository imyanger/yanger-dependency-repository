package com.yanger.starter.id.service;

import com.yanger.starter.id.converter.IdConverter;
import com.yanger.starter.id.converter.IdConverterImpl;
import com.yanger.starter.id.entity.Id;
import com.yanger.starter.id.entity.IdMeta;
import com.yanger.starter.id.enums.DeployType;
import com.yanger.starter.id.enums.IdType;
import com.yanger.starter.id.factory.IdMetaFactory;
import com.yanger.starter.id.provider.MachineIdProvider;
import com.yanger.starter.id.util.TimeUtils;

import org.jetbrains.annotations.Contract;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

/**
 * abstract id 生成服务
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Slf4j
public abstract class AbstractIdServiceImpl implements IdService {

    /** 机器 id */
    protected long machineId = -1;

    /** id 生成方式 */
    protected long deployType = DeployType.EMBED.getType();

    /** id 类型 */
    protected long idTypeValue = IdType.MAX_PEAK.getVaule();

    /** 版本 */
    protected long version = 0;

    /** Id type */
    protected IdType idType;

    /** Id meta */
    protected IdMeta idMeta;

    /** Id converter */
    protected IdConverter idConverter;

    /** Machine id provider */
    protected MachineIdProvider machineIdProvider;

    @Contract(pure = true)
    public AbstractIdServiceImpl() {
        this.idType = IdType.MAX_PEAK;
    }

    /**
     * Abstract id service
     *
     * @param type type
     */
    public AbstractIdServiceImpl(String type) {
        this.idType = IdType.parse(type);
    }

    /**
     * Abstract id service
     *
     * @param type type
     */
    @Contract(pure = true)
    public AbstractIdServiceImpl(IdType type) {
        this.idType = type;
    }

    /**
     * Init
     */
    public void init() {
        this.machineId = this.machineIdProvider.getMachineId();

        if (this.machineId < 0) {
            log.error("The machine ID is not configured properly so that Vesta Service refuses to start.");

            throw new IllegalStateException(
                "The machine ID is not configured properly so that Vesta Service refuses to start.");

        }
        if (this.idMeta == null) {
            this.setIdMeta(IdMetaFactory.getIdMeta(this.idType));
            this.setIdTypeValue(this.idType.value());
        } else {
            if (this.idMeta.getTimeBits() == IdType.MAX_PEAK.getSize()) {
                this.setIdTypeValue(IdType.MAX_PEAK.value());
            } else if (this.idMeta.getTimeBits() == IdType.MIN_GRANULARITY.getSize()) {
                this.setIdTypeValue(IdType.MIN_GRANULARITY.value());
            } else {
                throw new RuntimeException("Init Error. The time bits in IdMeta should be set to 30 or 40!");
            }
        }
        this.setIdConverter(new IdConverterImpl(this.idMeta));
    }

    /**
     * Gen id
     *
     * @return the long
     */
    @Override
    public long genId() {
        Id id = new Id();

        id.setMachine(this.machineId);
        id.setDeployType(this.deployType);
        id.setIdType(this.idTypeValue);
        id.setVersion(this.version);

        this.populateId(id);

        long ret = this.idConverter.convert(id);

        // Use trace because it cause low performance
        if (log.isTraceEnabled()) {
            log.trace(String.format("Id: %s => %d", id, ret));
        }

        return ret;
    }

    /**
     * 由子类实现生成唯一 id 需要的时间和序列号
     *
     * @param id id
     */
    protected abstract void populateId(Id id);

    /**
     * Trans time
     *
     * @param time time
     * @return the date
     */
    @Override
    public Date transTime(long time) {
        if (this.idType == IdType.MAX_PEAK) {
            return new Date(time * 1000 + TimeUtils.EPOCH);
        } else if (this.idType == IdType.MIN_GRANULARITY) {
            return new Date(time + TimeUtils.EPOCH);
        }
        return null;
    }


    /**
     * Exp id
     *
     * @param id id
     * @return the id
     */
    @Override
    public Id expId(long id) {
        return this.idConverter.convert(id);
    }

    /**
     * Make id
     *
     * @param time time
     * @param seq  seq
     * @return the long
     */
    @Override
    public long makeId(long time, long seq) {
        return this.makeId(time, seq, this.machineId);
    }

    /**
     * Make id
     *
     * @param time    time
     * @param seq     seq
     * @param machine machine
     * @return the long
     */
    @Override
    public long makeId(long time, long seq, long machine) {
        return this.makeId(this.deployType, time, seq, machine);
    }

    /**
     * Make id
     *
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     */
    @Override
    public long makeId(long deployType, long time, long seq, long machine) {
        return this.makeId(this.idTypeValue, deployType, time, seq, machine);
    }

    /**
     * Make id
     *
     * @param idType     idType
     * @param deployType gen method
     * @param time       time
     * @param seq        seq
     * @param machine    machine
     * @return the long
     */
    @Override
    public long makeId(long idType, long deployType, long time, long seq, long machine) {
        return this.makeId(this.version, idType, deployType, time, seq, machine);
    }

    /**
     * Make id
     *
     * @param version   version
     * @param type      type
     * @param deployType gen method
     * @param time      time
     * @param seq       seq
     * @param machine   machine
     * @return the long
     */
    @Override
    public long makeId(long version, long type, long deployType, long time, long seq, long machine) {
        IdType idType = IdType.parse(type);

        Id id = new Id(machine, seq, time, deployType, type, version);
        IdConverter idConverter = new IdConverterImpl(idType);

        return idConverter.convert(id);
    }


    /**
     * Sets machine id *
     *
     * @param machineId machine id
     */
    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    /**
     * Sets gen method *
     *
     * @param deployType gen method
     */
    public void setDeployType(long deployType) {
        this.deployType = deployType;
    }

    /**
     * Sets type *
     *
     * @param idTypeValue type
     */
    public void setIdTypeValue(long idTypeValue) {
        this.idTypeValue = idTypeValue;
    }

    /**
     * Sets version *
     *
     * @param version version
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * Sets id converter *
     *
     * @param idConverter id converter
     */
    public void setIdConverter(IdConverter idConverter) {
        this.idConverter = idConverter;
    }

    /**
     * Sets id meta *
     *
     * @param idMeta id meta
     */
    public void setIdMeta(IdMeta idMeta) {
        this.idMeta = idMeta;
    }

    /**
     * Sets machine id provider *
     *
     * @param machineIdProvider machine id provider
     */
    public void setMachineIdProvider(MachineIdProvider machineIdProvider) {
        this.machineIdProvider = machineIdProvider;
    }

}
