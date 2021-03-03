package com.yanger.starter.dubbo.filter;

import com.yanger.starter.dubbo.constant.TraceConstant;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.web.support.Trace;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.ListenableFilter;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @Description 跨进行参数传递
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class CrossJvmParameterPassingFilter extends ListenableFilter {

    /**
     * Cross jvm parameter passing filter
     */
    public CrossJvmParameterPassingFilter() {
        super.listener = new ContextListener();
    }

    /**
     * Invoke
     *
     * @param invoker    invoker
     * @param invocation invocation
     * @return result result
     * @throws RpcException rpc exception
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        boolean consumerSide = rpcContext.isConsumerSide();
        // 将 application context 数据写入到 rpc context
        if (consumerSide) {
            Map<String, String> consumerAttachments = RpcContext.getContext().getAttachments();
            consumerAttachments.put(TraceConstant.TRACE_ID, Trace.context().get());
            // 调用完成后不删除 context, 避免再次调用另一个服务时 context 无数据
            return invoker.invoke(invocation);
        }

        // 将 rpc context 数据写入到 application context
        Map<String, String> providerAttachments = RpcContext.getContext().getAttachments();
        Trace.context().set(providerAttachments.getOrDefault(TraceConstant.TRACE_ID, StringPool.EMPTY));
        try {
            return invoker.invoke(invocation);
        } finally {
            // provider 侧在调用完成后统一删除
            Trace.context().remove();
        }
    }

    static class ContextListener implements Listener {
        /**
         * On response
         *
         * @param appResponse app response
         * @param invoker     invoker
         * @param invocation  invocation
         */
        @Override
        public void onResponse(@NotNull Result appResponse, Invoker<?> invoker, Invocation invocation) {
            // pass attachments to result
            appResponse.addAttachments(RpcContext.getServerContext().getAttachments());
        }

        /**
         * On error
         *
         * @param t          t
         * @param invoker    invoker
         * @param invocation invocation
         */
        @Override
        public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {

        }
    }
}
