package com.yanger.starter.log.aspect;

import com.yanger.starter.log.annotation.BusinessLog;
import com.yanger.starter.log.entity.BusinessInfo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 业务日志注解拦截实现
 * @Author yanger
 * @Date 2021/3/10 14:02
 */
@Aspect
@Slf4j
@Component
public class BusinessLogAspect {

    @Pointcut("@annotation(com.yanger.starter.log.annotation.BusinessLog)")
    public void businessAspect() {}

    /**
     * 环绕通知需要携带ProceedingJoinPoint类型的参数
     * 环绕通知类似于动态代理的全过程：ProceedingJoinPoint类型的参数可以决定是否执行目标方法。
     * 而且环绕通知必须有返回值，返回值即为目标方法的返回值
     *
     * @param proceedingJoinPoint the proceeding join point
     * @return the object
     */
    @Around("businessAspect()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        BusinessInfo businessInfo = getControllerMethodDescription(proceedingJoinPoint);
        Object result = proceedingJoinPoint.proceed();
        long exeTimes = System.currentTimeMillis() - startTime;
        log.info("{}[{}-{}]({}) 执行耗时{}ms", businessInfo.getDescription(), businessInfo.getClassSimpleName(),
                 businessInfo.getMethodName(), businessInfo.getUri(), exeTimes);
        return result;
    }

    /**
     * 获取注解中对方法的描述信息
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    private BusinessInfo getControllerMethodDescription(JoinPoint joinPoint) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String className = targetClass.getName();
        String classSimpleName = targetClass.getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(BusinessLog.class).description();
                    break;
                }
            }
        }
        BusinessInfo businessInfo = BusinessInfo.builder()
            .className(className)
            .classSimpleName(classSimpleName)
            .methodName(methodName)
            .description(description)
            .build();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null) {
            //从获取RequestAttributes中获取HttpServletRequest的信息
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            if(request != null) {
                handle(businessInfo, request);
                businessInfo.setUri(request.getRequestURI());
            }
        }
        return businessInfo;
    }

    public void handle(BusinessInfo businessInfo, HttpServletRequest request) {}

}
