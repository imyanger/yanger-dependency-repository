package com.yanger.starter.web.handler;

import com.alibaba.fastjson.JSON;
import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.starter.web.annotation.IgnoreReponseAdvice;
import com.yanger.tools.web.entity.R;
import com.yanger.tools.web.entity.Result;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Description 统一返回
 * @Author yanger
 * @Date 2020/12/7 18:58
 */
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * @throws
     * @Description 判断支持的类型
     * @Author yanger
     * @Date 2021/1/25 18:31
     * @param: methodParameter
     * @param: aClass
     * @return: boolean
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 检查注解是否存在，存在则忽略拦截
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreReponseAdvice.class)) {
            return false;
        }
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreReponseAdvice.class)) {
            return false;
        }
        // swagger请求不处理
        if (methodParameter.getMethod().getReturnType().equals(org.springframework.http.ResponseEntity.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        // 判断为null构建ApiResponse对象进行返回
        if (o == null) {
            return R.succeed();
        }
        // 判断是ApiResponse子类或其本身就返回Object o本身，因为有可能是接口返回时创建了ApiResponse,这里避免再次封装
        if (o instanceof Result) {
            return o;
        }
        // String特殊处理，否则会抛异常
        if (o instanceof String) {
            return JsonUtils.toJson(R.succeed(o)).toString();
        }
        return R.succeed(o);
    }

}
