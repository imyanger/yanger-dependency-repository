package com.yanger.starter.id.provider;

import lombok.Getter;
import lombok.Setter;

/**
 * 基于配置生成 machineId, 需要在部署的每一台机器上配置不同的机器号
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class PropertyMachineIdProvider implements MachineIdProvider {

    @Getter
    @Setter
    private long machineId;

}
