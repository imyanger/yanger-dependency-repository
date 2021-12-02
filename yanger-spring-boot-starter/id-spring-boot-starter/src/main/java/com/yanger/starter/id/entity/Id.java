package com.yanger.starter.id.entity;

import org.jetbrains.annotations.Contract;

import java.io.*;

import lombok.Data;

/**
 * ID 对象
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Data
public class Id implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 6870931236218221183L;

    /** Machine */
    private long machine;

    /** Seq */
    private long seq;

    /** Time */
    private long time;

    /** Deploy type */
    private long deployType;

    /** Type */
    private long idType;

    /** Version */
    private long version;

    @Contract(pure = true)
    public Id() {
    }

    /**
     * Id
     *
     * @param machine    machine
     * @param seq        seq
     * @param time       time
     * @param deployType gen method
     * @param idType     type
     * @param version    version
     */
    @Contract(pure = true)
    public Id(long machine, long seq, long time, long deployType, long idType, long version) {
        super();
        this.machine = machine;
        this.seq = seq;
        this.time = time;
        this.deployType = deployType;
        this.idType = idType;
        this.version = version;
    }

}
