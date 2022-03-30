package com.yanger.starter.mybatis.config;

import com.yanger.starter.basic.constant.ConfigKey;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author yanger
 * @Date 2022/3/27/027 0:15
 */
public class DynamicCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        boolean dynamicEnable = Boolean.parseBoolean(conditionContext.getEnvironment().getProperty(ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_ENABLE));
        boolean dynamicReadWriteEnable = Boolean.parseBoolean(conditionContext.getEnvironment().getProperty(ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_READ_WRITE_ENABLE));
        return dynamicEnable || dynamicReadWriteEnable;
    }

}
