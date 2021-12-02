package com.yanger.starter.cache.el;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * redis 分布式锁 key 解析工具
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class AspectSupportUtils {

    /** evaluator */
    private static final ExpressionEvaluator EVALUATOR = new ExpressionEvaluator();

    /**
     * Gets key value *
     *
     * @param joinPoint     join point
     * @param keyExpression key expression
     * @return the key value
     */
    public static Object getKeyValue(@NotNull JoinPoint joinPoint, String keyExpression) {
        return getKeyValue(joinPoint.getTarget(),
                           joinPoint.getArgs(),
                           joinPoint.getTarget().getClass(),
                           ((MethodSignature) joinPoint.getSignature()).getMethod(),
                           keyExpression);
    }

    /**
     * 如果存在 EL 表达式占位符, 则解析 EL 表达式, 不存在则返回 "", 需要保证 key 冲突问题.
     *
     * @param object        object
     * @param args          args
     * @param clazz         clazz
     * @param method        method
     * @param keyExpression key expression
     * @return the key value
     */
    private static Object getKeyValue(Object object, Object[] args, Class<?> clazz, Method method, String keyExpression) {
        // 如果存在 EL 表达式占位符, 则解析 EL 表达式
        if (StringUtils.hasText(keyExpression)) {
            EvaluationContext evaluationContext = EVALUATOR.createEvaluationContext(object, clazz, method, args);
            AnnotatedElementKey methodKey = new AnnotatedElementKey(method, clazz);
            return EVALUATOR.key(keyExpression, methodKey, evaluationContext);
        }
        return "";
    }

}
