package com.yanger.starter.mybatis.plugins;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.yanger.starter.mybatis.annotation.PasswordField;
import com.yanger.starter.mybatis.annotation.SensitiveField;
import com.yanger.tools.general.tools.EncryptUtils;
import com.yanger.tools.web.tools.AesKit;
import com.yanger.tools.web.tools.Base64Utils;
import com.yanger.tools.web.tools.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 敏感字符加密
 * @Author yanger
 * @Date 2021/1/28 19:06
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class SensitiveFieldEncryptInterceptor implements Interceptor {

    /** Sensitive key */
    private final String sensitiveKey;

    /**
     * Sensitive field encrypt intercepter
     */
    @Contract(pure = true)
    public SensitiveFieldEncryptInterceptor(String sensitiveKey) {
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
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        List<Object> parameterList = new ArrayList<>(2);
        // 更新操作获取参数实体需要特殊处理下
        if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
            Object arg = invocation.getArgs()[1];
            // 如果期望实现铭感字段更新自动托敏，请使用UpdateWrapper方式操作更新
            if (arg instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) arg;
                // et 实体对象 (set 条件值,可以为 null)
                if (paramMap.containsKey(Constants.ENTITY)) {
                    Object parameter1 = paramMap.get("et");
                    if (parameter1 != null) {
                        parameterList.add(parameter1);
                    }
                }
            }
        } else if (SqlCommandType.INSERT.equals(sqlCommandType)) {
            // 插入只会有一个实体
            Object parameter = invocation.getArgs()[1];
            if (parameter != null) {
                parameterList.add(parameter);
            }
        }
        if (CollectionUtils.isNotEmpty(parameterList)) {
            for (Object o : parameterList) {
                Class<?> clazz = o.getClass();
                if (!clazz.getSuperclass().isInstance(Object.class)) {
                    // 如果有父类, 需要对父类中判断是否使用自定义注解
                    Class<?> superclass = clazz.getSuperclass();
                    this.encryptField(superclass.getDeclaredFields(), o, sqlCommandType);
                }
                this.encryptField(o.getClass().getDeclaredFields(), o, sqlCommandType);
            }
        }

        return invocation.proceed();
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
     * Encrypt field
     * @param declaredFields declared fields
     * @param parameter      parameter
     * @param sqlCommandType sql command type
     * @throws IllegalAccessException illegal access exception
     */
    private void encryptField(Field @NotNull [] declaredFields, Object parameter, SqlCommandType sqlCommandType) throws IllegalAccessException {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(SensitiveField.class)) {
                // 如果使用了加密注解，对内容加密再存储
                Object fieldValue = ReflectionUtils.getFieldValue(parameter, field.getName());
                if (!StringUtils.isEmpty(fieldValue)) {
                    byte[] encrypt = AesKit.encrypt(String.valueOf(fieldValue), this.sensitiveKey);
                    String encryptStr = Base64Utils.encodeToString(encrypt);
                    ReflectionUtils.setFieldValue(parameter, field.getName(), encryptStr);
                }
            }
            if (field.isAnnotationPresent(PasswordField.class)) {
                // 密码md5加密
                Object fieldValue = ReflectionUtils.getFieldValue(parameter, field.getName());
                if (!StringUtils.isEmpty(fieldValue)) {
                    ReflectionUtils.setFieldValue(parameter, field.getName(), EncryptUtils.getMD5(fieldValue.toString()));
                }
            }
        }
    }

}
