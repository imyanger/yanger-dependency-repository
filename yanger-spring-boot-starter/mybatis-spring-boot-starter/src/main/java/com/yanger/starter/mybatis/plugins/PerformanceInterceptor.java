package com.yanger.starter.mybatis.plugins;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import com.yanger.starter.mybatis.utils.SqlUtils;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 用于输出每条 SQL 语句及其执行时间
 * @Author yanger
 * @Date 2021/1/28 18:41
 */
@Slf4j
@Intercepts(value = {
    @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
    @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
    @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class PerformanceInterceptor implements Interceptor {

    /** LOGGER */
    private static final Log LOGGER = LogFactory.getLog(PerformanceInterceptor.class);
    
    /** DRUID_POOLED_PREPARED_STATEMENT */
    private static final String DRUID_POOLED_PREPARED_STATEMENT = "com.alibaba.druid.pool.DRUID_POOLED_PREPARED_STATEMENT";
    
    /** T4C_PREPARED_STATEMENT */
    private static final String T4C_PREPARED_STATEMENT = "oracle.jdbc.driver.T4C_PREPARED_STATEMENT";
    
    /** ORACLE_PREPARED_STATEMENT_WRAPPER */
    private static final String ORACLE_PREPARED_STATEMENT_WRAPPER = "oracle.jdbc.driver.ORACLE_PREPARED_STATEMENT_WRAPPER";
    
    /** DELEGATE */
    private static final String DELEGATE = "delegate";
    
    /**
     * SQL 执行最大时长,超过自动停止运行,有助于发现问题.
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    private long maxTime = 0;
    
    /**
     * SQL 是否格式化
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    private boolean format = false;
    
    /**
     * 是否写入日志文件
     * <p>true 写入日志文件,不阻断程序执行! </p>
     * <p>超过设定的最大执行时长异常提示! </p>
     */
    @Setter
    @Getter
    @Accessors(chain = true)
    private boolean writeInLog = false;
    
    /** Oracle get original sql method */
    private Method oracleGetOriginalSqlMethod;
    
    /** Druid get sql method */
    private Method druidGetSqlMethod;

    /**
     * Intercept object
     *
     * @param invocation invocation
     * @return the object
     * @throws Throwable throwable
     */
    @Override
    @SuppressWarnings("checkstyle:NestedIfDepth")
    public Object intercept(@NotNull Invocation invocation) throws Throwable {
        Statement statement;
        Object firstArg = invocation.getArgs()[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }
        MetaObject stmtMetaObj = SystemMetaObject.forObject(statement);
        try {
            statement = (Statement) stmtMetaObj.getValue("stmt.statement");
        } catch (Exception e) {
            // do nothing
        }
        if (stmtMetaObj.hasGetter(DELEGATE)) {
            //Hikari
            try {
                statement = (Statement) stmtMetaObj.getValue(DELEGATE);
            } catch (Exception ignored) {

            }
        }

        String originalSql = null;
        String stmtClassName = statement.getClass().getName();
        if (DRUID_POOLED_PREPARED_STATEMENT.equals(stmtClassName)) {
            try {
                if (this.druidGetSqlMethod == null) {
                    Class<?> clazz = Class.forName(DRUID_POOLED_PREPARED_STATEMENT);
                    this.druidGetSqlMethod = clazz.getMethod("getSql");
                }
                Object stmtSql = this.druidGetSqlMethod.invoke(statement);
                if (stmtSql instanceof String) {
                    originalSql = (String) stmtSql;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else if (T4C_PREPARED_STATEMENT.equals(stmtClassName)
                   || ORACLE_PREPARED_STATEMENT_WRAPPER.equals(stmtClassName)) {
            try {
                if (this.oracleGetOriginalSqlMethod != null) {
                    Object stmtSql = this.oracleGetOriginalSqlMethod.invoke(statement);
                    if (stmtSql instanceof String) {
                        originalSql = (String) stmtSql;
                    }
                } else {
                    Class<?> clazz = Class.forName(stmtClassName);
                    this.oracleGetOriginalSqlMethod = this.getMethodRegular(clazz, "getOriginalSql");
                    if (this.oracleGetOriginalSqlMethod != null) {
                        // ORACLE_PREPARED_STATEMENT_WRAPPER is not a public class, need set this.
                        this.oracleGetOriginalSqlMethod.setAccessible(true);
                        if (null != this.oracleGetOriginalSqlMethod) {
                            Object stmtSql = this.oracleGetOriginalSqlMethod.invoke(statement);
                            if (stmtSql instanceof String) {
                                originalSql = (String) stmtSql;
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        if (originalSql == null) {
            originalSql = statement.toString();
        }
        originalSql = originalSql.replaceAll("[\\s]+", StringPool.SPACE);
        int index = this.indexOfSqlStart(originalSql);
        if (index > 0) {
            originalSql = originalSql.substring(index);
        }

        // 计算执行 SQL 耗时
        long start = SystemClock.now();
        Object result = invocation.proceed();
        long timing = SystemClock.now() - start;

        // 格式化 SQL 打印执行结果
        return this.format(invocation, originalSql, result, timing);
    }

    /**
     * Format object
     *
     * @param invocation  invocation
     * @param originalSql original sql
     * @param result      result
     * @param timing      timing
     * @return the object
     */
    @Contract("_, _, _, _ -> param3")
    private Object format(@NotNull Invocation invocation, String originalSql, Object result, long timing) {
        Object target = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(target);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        StringBuilder formatSql = new StringBuilder()
            .append(" Time: ").append(timing)
            .append(" ms - ID: ").append(ms.getId())
            .append(StringPool.NEWLINE).append("Execute SQL: ")
            .append(SqlUtils.sqlFormat(originalSql, this.format)).append(StringPool.NEWLINE);
        if (this.isWriteInLog()) {
            if (this.getMaxTime() >= 1 && timing > this.getMaxTime()) {
                LOGGER.error(formatSql.toString());
            } else {
                LOGGER.debug(formatSql.toString());
            }
        } else {
            System.err.println(formatSql.toString());
            Assert.isFalse(this.getMaxTime() >= 1 && timing > this.getMaxTime(),
                           " The SQL execution time is too large, please optimize ! ");
        }
        return result;
    }

    /**
     * Plugin object
     *
     * @param target target
     * @return the object
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * Sets properties *
     *
     * @param prop prop
     */
    @Override
    public void setProperties(@NotNull Properties prop) {
        String maxTime = prop.getProperty("maxTime");
        String format = prop.getProperty("format");
        if (StringUtils.isNotBlank(maxTime)) {
            this.maxTime = Long.parseLong(maxTime);
        }
        if (StringUtils.isNotBlank(format)) {
            this.format = Boolean.valueOf(format);
        }
    }

    /**
     * 获取此方法名的具体 Method
     *
     * @param clazz      class 对象
     * @param methodName 方法名
     * @return 方法 method regular
     */
    @Nullable
    private Method getMethodRegular(Class<?> clazz, String methodName) {
        if (Object.class.equals(clazz)) {
            return null;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return this.getMethodRegular(clazz.getSuperclass(), methodName);
    }

    /**
     * 获取sql语句开头部分
     *
     * @param sql ignore
     * @return ignore int
     */
    private int indexOfSqlStart(@NotNull String sql) {
        String upperCaseSql = sql.toUpperCase();
        Set<Integer> set = new HashSet<>();
        set.add(upperCaseSql.indexOf("SELECT "));
        set.add(upperCaseSql.indexOf("UPDATE "));
        set.add(upperCaseSql.indexOf("INSERT "));
        set.add(upperCaseSql.indexOf("DELETE "));
        set.remove(-1);
        if (CollectionUtils.isEmpty(set)) {
            return -1;
        }
        List<Integer> list = new ArrayList<>(set);
        list.sort(Comparator.naturalOrder());
        return list.get(0);
    }

}
