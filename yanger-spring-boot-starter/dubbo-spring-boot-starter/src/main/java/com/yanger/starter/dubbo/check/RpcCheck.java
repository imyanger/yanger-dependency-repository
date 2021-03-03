package com.yanger.starter.dubbo.check;

/**
 * @Description RPC 请求检查接口
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public interface RpcCheck {

    /**
     * Check
     *
     * @return the rpc context
     */
    String check();
}
