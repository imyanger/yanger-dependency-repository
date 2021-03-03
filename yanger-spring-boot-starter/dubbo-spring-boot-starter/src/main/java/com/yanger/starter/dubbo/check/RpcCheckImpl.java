package com.yanger.starter.dubbo.check;

import com.yanger.starter.basic.util.ConfigKit;

import org.apache.dubbo.rpc.RpcContext;

/**
 * @Description RPC 请求检查接口
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public class RpcCheckImpl implements RpcCheck {

    /**
     * Check
     *
     * @return the rpc context
     */
    @Override
    public String check() {
        return ConfigKit.getAppName() + ":" + RpcContext.getContext().getLocalAddressString();
    }
}
