package com.fkhwl.starter.mybatis.plugins;

import com.fkhwl.starter.common.base.BaseDTO;
import com.fkhwl.starter.common.base.BasePO;
import com.fkhwl.starter.core.util.AesUtils;
import com.fkhwl.starter.core.util.Base64Utils;
import com.fkhwl.starter.core.util.ReflectionUtils;
import com.fkhwl.starter.core.util.StringUtils;
import com.fkhwl.starter.mybatis.support.annotation.SensitiveBody;
import com.fkhwl.starter.mybatis.support.annotation.SensitiveField;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: </p>
 *
 * @author zhubo
 * @version 1.0.0
 * @email zhubo @fkhwl.com
 * @date 2020.05.13 11:24
 * @since 2020 /5/12 16:06
 */
@Intercepts(@Signature(
    type = ResultSetHandler.class,
    method = "handleResultSets",
    args = {Statement.class}))
@Slf4j
public class SensitiveFieldDecryptIntercepter implements Interceptor {
    /** Sensitive key */
    private final String sensitiveKey;

    /**
     * Sensitive field encrypt intercepter
     *
     * @param sensitiveKey sensitive key
     * @since 1.5.0
     */
    @Contract(pure = true)
    public SensitiveFieldDecryptIntercepter(String sensitiveKey) {
        this.sensitiveKey = sensitiveKey;
    }

    /**
     * Intercept
     * todo-dong4j : (2020.05.25 19:50) [类型转换问题]
     *
     * @param invocation invocation
     * @return the object
     * @throws Throwable throwable
     * @since 1.0.0
     */
    @Override
    public Object intercept(@NotNull Invocation invocation) throws Throwable {
        //执行请求方法, 并将所得结果保存到result中
        Object result = invocation.proceed();
        if (result instanceof ArrayList) {
            for (Object o : (ArrayList) result) {
                if (o instanceof BasePO || o instanceof BaseDTO) {
                    process(o);
                }
            }
        }
        return result;

    }

    /**
     * Process
     * 加密处理，目前递归
     *
     * @param o o
     * @since 1.6.0
     */
    private void process(Object o) {
        ReflectionUtils.doWithFields(o.getClass(), field -> {
            Object fieldValue = ReflectionUtils.getFieldValue(o, field.getName());
            if (fieldValue != null) {
                process(fieldValue);
            }
        }, field -> o instanceof BaseDTO && field.getAnnotation(SensitiveBody.class) != null);

        ReflectionUtils.doWithFields(o.getClass(), field -> {
            Object fieldValue = ReflectionUtils.getFieldValue(o, field.getName());
            if (!StringUtils.isEmpty(fieldValue)) {
                try {
                    String decrypt = AesUtils.decryptToStr(
                        Base64Utils.decodeFromString(String.valueOf(fieldValue)),
                        this.sensitiveKey);

                    ReflectionUtils.setFieldValue(o, field.getName(), decrypt);
                } catch (Exception e) {
                    log.debug("敏感字段解密异常, fieldValue={}, exception={}", fieldValue, e.getMessage());
                }
            }
        }, field -> field.getAnnotation(SensitiveField.class) != null);
    }

    /**
     * Plugin
     *
     * @param target target
     * @return the object
     * @since 1.0.0
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * Sets properties *
     *
     * @param properties properties
     * @since 1.0.0
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * Decrypt field
     *
     * @param declaredFields declared fields
     * @param parameter      parameter
     * @param sqlCommandType sql command type
     * @throws IllegalAccessException illegal access exception
     * @since 1.0.0
     */
    private void decryptField(Field @NotNull [] declaredFields, Object parameter, SqlCommandType sqlCommandType)
        throws IllegalAccessException {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(SensitiveField.class)) {
                // 如果使用了指定注解
                // 对内容加密再存储
                Object fieldValue = ReflectionUtils.getFieldValue(parameter, field.getName());
                String decrypt = AesUtils.decryptToStr(Base64Utils.decodeFromString(String.valueOf(fieldValue)),
                                                       this.sensitiveKey);
                ReflectionUtils.setFieldValue(parameter, field.getName(), decrypt);
            }
        }
    }
}
