package com.yanger.starter.cache.el;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Expression evaluator
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
public class ExpressionEvaluator extends CachedExpressionEvaluator {

    /** Param name discoverer */
    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    /** Condition cache */
    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    /** Target method cache */
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    /**
     * Create evaluation context
     * @param object      object
     * @param targetClass target class
     * @param method      method
     * @param args        args
     * @return the evaluation context
     */
    public EvaluationContext createEvaluationContext(Object object, Class<?> targetClass, Method method, Object[] args) {
        Method targetMethod = this.getTargetMethod(targetClass, method);
        ExpressionRootObject root = new ExpressionRootObject(object, args);
        return new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
    }

    /**
     * Key
     * @param conditionExpression condition expression
     * @param elementKey          element key
     * @param evalContext         eval context
     * @return the object
     */
    public Object key(String conditionExpression, AnnotatedElementKey elementKey, EvaluationContext evalContext) {
        return this.getExpression(this.conditionCache, elementKey, conditionExpression).getValue(evalContext);
    }

    /**
     * Gets target method
     * @param targetClass target class
     * @param method      method
     * @return the target method
     */
    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

}
