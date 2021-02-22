package com.fkhwl.starter.id.entity;

import org.jetbrains.annotations.Contract;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:25
 * @since 1.5.0
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
     * @since 1.5.0
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
     * @since 1.5.0
     */
    public byte getMachineBits() {
        return this.machineBits;
    }

    /**
     * Sets machine bits *
     *
     * @param machineBits machine bits
     * @since 1.5.0
     */
    public void setMachineBits(byte machineBits) {
        this.machineBits = machineBits;
    }

    /**
     * Gets machine bits mask *
     *
     * @return the machine bits mask
     * @since 1.5.0
     */
    public long getMachineBitsMask() {
        return ~(-1L << this.machineBits);
    }

    /**
     * Gets seq bits *
     *
     * @return the seq bits
     * @since 1.5.0
     */
    public byte getSeqBits() {
        return this.seqBits;
    }

    /**
     * Sets seq bits *
     *
     * @param seqBits seq bits
     * @since 1.5.0
     */
    public void setSeqBits(byte seqBits) {
        this.seqBits = seqBits;
    }

    /**
     * Gets seq bits start pos *
     *
     * @return the seq bits start pos
     * @since 1.5.0
     */
    public long getSeqBitsStartPos() {
        return this.machineBits;
    }

    /**
     * Gets seq bits mask *
     *
     * @return the seq bits mask
     * @since 1.5.0
     */
    public long getSeqBitsMask() {
        return ~(-1L << this.seqBits);
    }

    /**
     * Gets time bits *
     *
     * @return the time bits
     * @since 1.5.0
     */
    public byte getTimeBits() {
        return this.timeBits;
    }

    /**
     * Sets time bits *
     *
     * @param timeBits time bits
     * @since 1.5.0
     */
    public void setTimeBits(byte timeBits) {
        this.timeBits = timeBits;
    }

    /**
     * Gets time bits start pos *
     *
     * @return the time bits start pos
     * @since 1.5.0
     */
    public long getTimeBitsStartPos() {
        return this.machineBits + this.seqBits;
    }

    /**
     * Gets time bits mask *
     *
     * @return the time bits mask
     * @since 1.5.0
     */
    public long getTimeBitsMask() {
        return ~(-1L << this.timeBits);
    }

    /**
     * Gets gen method bits *
     *
     * @return the gen method bits
     * @since 1.5.0
     */
    public byte getDeployTypeBits() {
        return this.deployTypeBits;
    }

    /**
     * Sets gen method bits *
     *
     * @param deployTypeBits gen method bits
     * @since 1.5.0
     */
    public void setDeployTypeBits(byte deployTypeBits) {
        this.deployTypeBits = deployTypeBits;
    }

    /**
     * Gets gen method bits start pos *
     *
     * @return the gen method bits start pos
     * @since 1.5.0
     */
    public long getGenMethodBitsStartPos() {
        return this.machineBits + this.seqBits + this.timeBits;
    }

    /**
     * Gets gen method bits mask *
     *
     * @return the gen method bits mask
     * @since 1.5.0
     */
    public long getGenMethodBitsMask() {
        return ~(-1L << this.deployTypeBits);
    }

    /**
     * Gets type bits *
     *
     * @return the type bits
     * @since 1.5.0
     */
    public byte getIdTypeBits() {
        return this.idTypeBits;
    }

    /**
     * Sets type bits *
     *
     * @param idTypeBits type bits
     * @since 1.5.0
     */
    public void setIdTypeBits(byte idTypeBits) {
        this.idTypeBits = idTypeBits;
    }

    /**
     * Gets type bits start pos *
     *
     * @return the type bits start pos
     * @since 1.5.0
     */
    public long getTypeBitsStartPos() {
        return this.machineBits + this.seqBits + this.timeBits + this.deployTypeBits;
    }

    /**
     * Gets type bits mask *
     *
     * @return the type bits mask
     * @since 1.5.0
     */
    public long getTypeBitsMask() {
        return ~(-1L << this.idTypeBits);
    }

    /**
     * Gets version bits *
     *
     * @return the version bits
     * @since 1.5.0
     */
    public byte getVersionBits() {
        return this.versionBits;
    }

    /**
     * Sets version bits *
     *
     * @param versionBits version bits
     * @since 1.5.0
     */
    public void setVersionBits(byte versionBits) {
        this.versionBits = versionBits;
    }

    /**
     * Gets version bits start pos *
     *
     * @return the version bits start pos
     * @since 1.5.0
     */
    public long getVersionBitsStartPos() {
        return this.machineBits + this.seqBits + this.timeBits + this.deployTypeBits + this.idTypeBits;
    }

    /**
     * Gets version bits mask *
     *
     * @return the version bits mask
     * @since 1.5.0
     */
    public long getVersionBitsMask() {
        return ~(-1L << this.versionBits);
    }
}
