package com.yanger.starter.id.entity;

import org.jetbrains.annotations.Contract;

/**
 * ID 元数据
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class IdMeta {

    /** Machine bits */
    private byte machineBits;

    /** Seq bits */
    private byte seqBits;

    /** Time bits */
    private byte timeBits;

    /** Gen method bits */
    private byte deployTypeBits;

    /** Type bits */
    private byte idTypeBits;

    /** Version bits */
    private byte versionBits;

    /**
     * Id meta
     *
     * @param machineBits    machine bits
     * @param seqBits        seq bits
     * @param timeBits       time bits
     * @param deployTypeBits gen method bits
     * @param idTypeBits     type bits
     * @param versionBits    version bits
     */
    @Contract(pure = true)
    public IdMeta(byte machineBits, byte seqBits, byte timeBits, byte deployTypeBits, byte idTypeBits, byte versionBits) {
        super();
        this.machineBits = machineBits;
        this.seqBits = seqBits;
        this.timeBits = timeBits;
        this.deployTypeBits = deployTypeBits;
        this.idTypeBits = idTypeBits;
        this.versionBits = versionBits;
    }

    /**
     * Gets machine bits *
     *
     * @return the machine bits
     */
    public byte getMachineBits() {
        return this.machineBits;
    }

    /**
     * Sets machine bits *
     *
     * @param machineBits machine bits
     */
    public void setMachineBits(byte machineBits) {
        this.machineBits = machineBits;
    }

    /**
     * Gets machine bits mask *
     *
     * @return the machine bits mask
     */
    public long getMachineBitsMask() {
        return ~(-1L << this.machineBits);
    }

    /**
     * Gets seq bits *
     *
     * @return the seq bits
     */
    public byte getSeqBits() {
        return this.seqBits;
    }

    /**
     * Sets seq bits
     *
     * @param seqBits seq bits
     */
    public void setSeqBits(byte seqBits) {
        this.seqBits = seqBits;
    }

    /**
     * Gets seq bits start pos *
     *
     * @return the seq bits start pos
     */
    public long getSeqBitsStartPos() {
        return this.machineBits;
    }

    /**
     * Gets seq bits mask
     *
     * @return the seq bits mask
     */
    public long getSeqBitsMask() {
        return ~(-1L << this.seqBits);
    }

    /**
     * Gets time bits
     *
     * @return the time bits
     */
    public byte getTimeBits() {
        return this.timeBits;
    }

    /**
     * Sets time bits *
     *
     * @param timeBits time bits
     */
    public void setTimeBits(byte timeBits) {
        this.timeBits = timeBits;
    }

    /**
     * Gets time bits start pos *
     *
     * @return the time bits start pos
     */
    public long getTimeBitsStartPos() {
        return this.machineBits + this.seqBits;
    }

    /**
     * Gets time bits mask *
     *
     * @return the time bits mask
     */
    public long getTimeBitsMask() {
        return ~(-1L << this.timeBits);
    }

    /**
     * Gets gen method bits *
     *
     * @return the gen method bits
     */
    public byte getDeployTypeBits() {
        return this.deployTypeBits;
    }

    /**
     * Sets gen method bits *
     *
     * @param deployTypeBits gen method bits
     */
    public void setDeployTypeBits(byte deployTypeBits) {
        this.deployTypeBits = deployTypeBits;
    }

    /**
     * Gets gen method bits start pos *
     *
     * @return the gen method bits start pos
     */
    public long getGenMethodBitsStartPos() {
        return this.machineBits + this.seqBits + this.timeBits;
    }

    /**
     * Gets gen method bits mask *
     *
     * @return the gen method bits mask
     */
    public long getGenMethodBitsMask() {
        return ~(-1L << this.deployTypeBits);
    }

    /**
     * Gets type bits *
     *
     * @return the type bits
     */
    public byte getIdTypeBits() {
        return this.idTypeBits;
    }

    /**
     * Sets type bits *
     *
     * @param idTypeBits type bits
     */
    public void setIdTypeBits(byte idTypeBits) {
        this.idTypeBits = idTypeBits;
    }

    /**
     * Gets type bits start pos *
     *
     * @return the type bits start pos
     */
    public long getTypeBitsStartPos() {
        return this.machineBits + this.seqBits + this.timeBits + this.deployTypeBits;
    }

    /**
     * Gets type bits mask *
     *
     * @return the type bits mask
     */
    public long getTypeBitsMask() {
        return ~(-1L << this.idTypeBits);
    }

    /**
     * Gets version bits *
     *
     * @return the version bits
     */
    public byte getVersionBits() {
        return this.versionBits;
    }

    /**
     * Sets version bits *
     *
     * @param versionBits version bits
     */
    public void setVersionBits(byte versionBits) {
        this.versionBits = versionBits;
    }

    /**
     * Gets version bits start pos
     *
     * @return the version bits start pos
     */
    public long getVersionBitsStartPos() {
        return this.machineBits + this.seqBits + this.timeBits + this.deployTypeBits + this.idTypeBits;
    }

    /**
     * Gets version bits mask *
     *
     * @return the version bits mask
     */
    public long getVersionBitsMask() {
        return ~(-1L << this.versionBits);
    }

}
