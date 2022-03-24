package com.yanger.starter.id.provider;

/**
 * 基于配置生成 machineId, 需要在部署的每一台机器上配置不同的机器号
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class PropertyMachineIdsProvider implements MachineIdsProvider {

    /** Machine ids */
    private long[] machineIds;

    /** Current index */
    private int currentIndex;

    /**
     * Gets next machine id
     * @return the next machine id
     */
    @Override
    public long getNextMachineId() {
        return this.getMachineId();
    }

    /**
     * Gets machine id
     * @return the machine id
     */
    @Override
    public long getMachineId() {
        return this.machineIds[this.currentIndex++ % this.machineIds.length];
    }

    /**
     * Sets machine ids
     * @param machineIds machine ids
     */
    public void setMachineIds(long[] machineIds) {
        this.machineIds = machineIds;
    }

}
