package com.yanger.starter.web.handler;

import com.yanger.starter.web.annotation.LoginUser;
import com.yanger.starter.web.config.TokenConfig;
import com.yanger.starter.web.entity.AuthUser;
import com.yanger.tools.web.tools.JwtUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;

/**
 * 登录信息解析
 * @Author yanger
 * @Date 2020/9/15 15:38
 */
@Component
public class LoginUserResolver implements HandlerMethodArgumentResolver {

    @Resource
    private TokenConfig tokenConfig;

    /**
     * 支持注解的对象类型
     *
     * @param parameter parameter
     * @return the boolean
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AuthUser.class)
               && parameter.hasParameterAnnotation(LoginUser.class);
    }

    /**
     * 解析 LoginUser 参数
     *
     * @param methodParameter       method parameter
     * @param modelAndViewContainer model and view container
     * @param nativeWebRequest      native web request
     * @param webDataBinderFactory  web data binder factory
     * @return the object
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        Object attrObj = nativeWebRequest.getAttribute(tokenConfig.getUserKey(), RequestAttributes.SCOPE_REQUEST);
        if (attrObj instanceof AuthUser) {
            return attrObj;
        }
        if (attrObj == null) {
            String token = nativeWebRequest.getHeader(tokenConfig.getUserKey());
            if (StringUtils.isNotEmpty(token)) {
                return JwtUtils.parse(AuthUser.class, token);
            }
        }
        return null;
    }

}
