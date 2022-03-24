package com.yanger.starter.id.config;

import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.id.enums.ProviderType;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 自定义条件用于初始化指定的 bean, 默认雪花算法
 * @Author yanger
 * @Date 2021/1/28 19:08
 */
public abstract class ProviderTypeCondition extends SpringBootCondition {

    private final ProviderType providerType;

    ProviderTypeCondition(ProviderType providerType) {
        Objects.requireNonNull(providerType, "ID 生成类型不能为空");
        this.providerType = providerType;
    }

    /**
     * 匹配 ID 生成器类型
     * @param conditionContext      condition context
     * @param annotatedTypeMetadata annotated type metadata
     * @return the match outcome
     */
    @Override
    public ConditionOutcome getMatchOutcome(@NotNull ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        // 使用 binder 来解析配置
        Binder binder = Binder.get(environment);

        ProviderType checkProviderType;
        try {
            checkProviderType = binder.bind(ConfigKey.IdConfigKey.PROVIDER_TYPE, Bindable.of(ProviderType.class)).get();
        } catch (NoSuchElementException e) {
            checkProviderType = ProviderType.SNOW_FLAKE;
        }

        if (checkProviderType.equals(this.providerType)) {
            return ConditionOutcome.match();
        }

        return ConditionOutcome.noMatch("未匹配到 ID 生成器类型: " + this.providerType);
    }

}
