package com.fkhwl.starter.id.provider;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 同 {@link PropertyMachineIdProvider}</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:24
 * @since 1.5.0
 */
public class PropertyMachineIdsProvider implements MachineIdsProvider {
    /** Machine ids */
    private long[] machineIds;
    /** Current index */
    private int currentIndex;

    /**
     * Gets next machine id *
     *
     * @return the next machine id
     * @since 1.5.0
     */
    @Override
    public long getNextMachineId() {
        return this.getMachineId();
    }

    /**
     * Gets machine id *
     *
     * @return the machine id
     * @since 1.5.0
     */
    @Override
    public long getMachineId() {
        return this.machineIds[this.currentIndex++ % this.machineIds.length];
    }

    /**
     * Sets machine ids *
     *
     * @param machineIds machine ids
     * @since 1.5.0
     */
    public void setMachineIds(long[] machineIds) {
        this.machineIds = machineIds;
    }
}
