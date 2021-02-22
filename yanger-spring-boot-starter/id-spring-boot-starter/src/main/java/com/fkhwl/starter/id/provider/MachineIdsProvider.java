package com.fkhwl.starter.id.provider;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 批量生成机器 id</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.24 16:24
 * @since 1.5.0
 */
public interface MachineIdsProvider extends MachineIdProvider {

    /**
     * Gets next machine id *
     *
     * @return the next machine id
     * @since 1.5.0
     */
    long getNextMachineId();

}
