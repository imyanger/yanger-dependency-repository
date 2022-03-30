package com.yanger.starter.mybatis.plugins;

import com.yanger.starter.mybatis.dynamic.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 读写分离数据源处理
 * @Author yanger
 * @Date 2021/1/28 18:41
 */
@Slf4j
@Intercepts(value = {
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class DynamicReadWriteInterceptor implements Interceptor {

    /**
     * Intercept object
     * @param invocation invocation
     * @return the object
     * @throws Throwable throwable
     */
    @Override
    public Object intercept(@NotNull Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (!synchronizationActive) {
            if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                DataSourceContextHolder.switchToRead();
            } else {
                DataSourceContextHolder.switchToWrite();
            }
        }
        return invocation.proceed();
    }

    /**
     * Plugin object
     * @param target target
     * @return the object
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

}
