package com.fkhwl.starter.dubbo.check;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: RPC 请求检查接口 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.06.18 02:39
 * @since 1.5.0
 */
public interface RpcCheck {

    /**
     * Check
     *
     * @return the rpc context
     * @since 1.5.0
     */
    String check();
}
