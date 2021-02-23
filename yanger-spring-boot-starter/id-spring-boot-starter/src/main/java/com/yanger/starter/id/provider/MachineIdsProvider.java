package com.yanger.starter.id.provider;

/**
 * @Description 批量生成机器 id
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface MachineIdsProvider extends MachineIdProvider {

    long getNextMachineId();

}
