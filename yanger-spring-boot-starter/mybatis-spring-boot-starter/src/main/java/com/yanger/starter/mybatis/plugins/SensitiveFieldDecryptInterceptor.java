package com.yanger.starter.mybatis.plugins;

import com.yanger.starter.basic.entity.BaseDTO;
import com.yanger.starter.mybatis.annotation.SensitiveField;
import com.yanger.starter.mybatis.entity.BasePO;
import com.yanger.tools.web.tools.AesKit;
import com.yanger.tools.web.tools.Base64Utils;
import com.yanger.tools.web.tools.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * 敏感字符解密
 * @Author yanger
 * @Date 2021/1/28 19:06
 */
@Slf4j
@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
public class SensitiveFieldDecryptInterceptor implements Interceptor {

    /** Sensitive key */
    private final String sensitiveKey;

    /**
     * Sensitive field encrypt intercepter
     * @param sensitiveKey sensitive key
     */
    @Contract(pure = true)
    public SensitiveFieldDecryptInterceptor(String sensitiveKey) {
        this.sensitiveKey = sensitiveKey;
    }

    /**
     * Intercept
     * @param invocation invocation
     * @return the object
     * @throws Throwable throwable
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
     * @param o o
     */
    private void process(Object o) {
        ReflectionUtils.doWithFields(o.getClass(), field -> {
            Object fieldValue = ReflectionUtils.getFieldValue(o, field.getName());
            if (!StringUtils.isEmpty(fieldValue)) {
                try {
                    String decrypt = AesKit.decryptToStr(
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
     * @param target target
     * @return the object
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * Sets properties
     * @param properties properties
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * Decrypt field
     * @param declaredFields declared fields
     * @param parameter      parameter
     * @param sqlCommandType sql command type
     * @throws IllegalAccessException illegal access exception
     */
    private void decryptField(Field @NotNull [] declaredFields, Object parameter, SqlCommandType sqlCommandType)
        throws IllegalAccessException {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(SensitiveField.class)) {
                // 如果使用了加密注解，对加密内容进行解密
                Object fieldValue = ReflectionUtils.getFieldValue(parameter, field.getName());
                String decrypt = AesKit.decryptToStr(Base64Utils.decodeFromString(String.valueOf(fieldValue)),
                                                       this.sensitiveKey);
                ReflectionUtils.setFieldValue(parameter, field.getName(), decrypt);
            }
        }
    }

}


