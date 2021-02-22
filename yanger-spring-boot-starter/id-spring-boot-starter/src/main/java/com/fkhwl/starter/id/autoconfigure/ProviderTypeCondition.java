package com.fkhwl.starter.id.autoconfigure;

import com.fkhwl.starter.id.enums.ProviderType;

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
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: 自定义条件用于初始化指定的 bean, 默认雪花算法 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.21 19:37
 * @since 1.5.0
 */
public abstract class ProviderTypeCondition extends SpringBootCondition {

    /** Cache types */
    private final ProviderType providerType;

    /**
     * Captcha condition
     *
     * @param providerType provider type
     * @since 1.5.0
     */
    ProviderTypeCondition(ProviderType providerType) {
        Objects.requireNonNull(providerType, "ID 生成类型不能为空");
        this.providerType = providerType;
    }

    /**
     * Gets match outcome *
     *
     * @param conditionContext      condition context
     * @param annotatedTypeMetadata annotated type metadata
     * @return the match outcome
     * @since 1.5.0
     */
    @Override
    public ConditionOutcome getMatchOutcome(@NotNull ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Environment environment = conditionContext.getEnvironment();
        // 使用 binder 来解析配置
        Binder binder = Binder.get(environment);

        ProviderType checkProviderType;
        try {
            checkProviderType = binder.bind("fkh.id.provider-type", Bindable.of(ProviderType.class)).get();
        } catch (NoSuchElementException e) {
            checkProviderType = ProviderType.SNOW_FLAKE;
        }

        if (checkProviderType.equals(this.providerType)) {
            return ConditionOutcome.match();
        }
        return ConditionOutcome.noMatch("未匹配到 ID 生成器类型: " + this.providerType);
    }
}
