package com.yanger.starter.dubbo.filter;

import com.yanger.tools.web.exception.BasicException;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.ListenableFilter;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericService;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

import cn.hutool.core.text.StrFormatter;

/**
 * @Description 重写 {@link org.apache.dubbo.rpc.filter.ExceptionFilter}, 不将 v5 框架异常包装为 RPC 异常
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
@Activate(group = CommonConstants.PROVIDER)
public class ConsumerExceptionFilter extends ListenableFilter {

    /**
     * Exception filter
     */
    public ConsumerExceptionFilter() {
        super.listener = new ExceptionListener();
    }

    /**
     * Invoke
     *
     * @param invoker    invoker
     * @param invocation invocation
     * @return the result
     * @throws RpcException rpc exception
     */
    @Override
    public Result invoke(@NotNull Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    static class ExceptionListener implements Listener {

        /** Logger */
        private Logger logger = LoggerFactory.getLogger(ExceptionListener.class);

        /** JAVA_STR */
        private static final String JAVA_STR = "java.";
        /** JAVAX_STR */
        private static final String JAVAX_STR = "javax.";

        /**
         * 重写异常拦截逻辑, 将部分异常包装为 BasicException 异常.
         *
         * @param appResponse app response
         * @param invoker     invoker
         * @param invocation  invocation
         */
        @Override
        @SuppressWarnings("PMD.UndefineMagicConstantRule")
        public void onResponse(@NotNull Result appResponse, Invoker<?> invoker, Invocation invocation) {
            if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
                try {
                    Throwable exception = appResponse.getException();

                    // 如果系统自定义异常, 直接抛出
                    if (exception instanceof BasicException) {
                        return;
                    }

                    // 如果是 checked 异常，全部包装为 BasicException
                    if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
                        appResponse.setException(new BasicException());
                        return;
                    }
                    // 在方法签名上有声明，直接抛出
                    try {
                        Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                        Class<?>[] exceptionClassses = method.getExceptionTypes();
                        for (Class<?> exceptionClass : exceptionClassses) {
                            if (exception.getClass().equals(exceptionClass)) {
                                return;
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        return;
                    }

                    // 未在方法签名上定义的异常，在服务器端打印 ERROR 日志
                    this.onError(exception, invoker, invocation);

                    // 异常类和接口类在同一 jar 包里，直接抛出
                    String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                    String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                    if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                        return;
                    }
                    // 是 JDK 自带的异常，直接抛出
                    String className = exception.getClass().getName();
                    if (className.startsWith(JAVA_STR) || className.startsWith(JAVAX_STR)) {
                        return;
                    }

                    // 是 Dubbo 本身的异常，直接抛出
                    if (exception instanceof RpcException) {
                        return;
                    }

                    // 其他的异常包装为 BasicException
                    appResponse.setException(new BasicException());
                } catch (Throwable e) {
                    String warnMessage = "执行 ConsumerExceptionFilter case: {}. service: {}, method: {}, exception: {} :{}";
                    this.logger.warn(StrFormatter.format(warnMessage,
                                                         RpcContext.getContext().getRemoteHost(),
                                                         invoker.getInterface().getName(),
                                                         invocation.getMethodName(),
                                                         e.getClass().getName(),
                                                         e.getMessage()),
                                     e);
                }
            }
        }

        /**
         * On error
         *
         * @param exception  exception
         * @param invoker    invoker
         * @param invocation invocation
         */
        @Override
        public void onError(Throwable exception, @NotNull Invoker<?> invoker, @NotNull Invocation invocation) {
            String message = "捕获未处理异常 {}. service: {}, method: {}, exception: {} :{}";
            this.logger.error(StrFormatter.format(message,
                                                  RpcContext.getContext().getRemoteHost(),
                                                  invoker.getInterface().getName(),
                                                  invocation.getMethodName(),
                                                  exception.getClass().getName(),
                                                  exception.getMessage()),
                              exception);
        }

        /**
         * Sets logger *
         *
         * @param logger logger
         */
        // For test purpose
        public void setLogger(Logger logger) {
            this.logger = logger;
        }
    }
}

