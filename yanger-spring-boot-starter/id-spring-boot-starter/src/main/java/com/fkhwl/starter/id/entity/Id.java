package com.fkhwl.starter.id.entity;

import org.jetbrains.annotations.Contract;

import java.io.*;

import lombok.Data;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 15:28
 * @since 1.5.0
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

    /**
     * Id
     *
     * @since 1.5.0
     */
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
     * @since 1.5.0
     */
    @Contract(pure = true)
    public Id(long machine,
              long seq,
              long time,
              long deployType,
              long idType,
              long version) {
        super();
        this.machine = machine;
        this.seq = seq;
        this.time = time;
        this.deployType = deployType;
        this.idType = idType;
        this.version = version;
    }

}
