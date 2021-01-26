package com.yanger.starter.web.handler;

import com.alibaba.fastjson.JSON;
import com.yanger.starter.web.annotation.IgnoreLoginAuth;
import com.yanger.starter.web.annotation.LoginAuth;
import com.yanger.starter.web.entity.AuthUser;
import com.yanger.starter.web.entity.JwtConst;
import com.yanger.tools.web.entity.R;
import com.yanger.tools.web.support.ResultCode;
import com.yanger.tools.web.tools.JwtUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 登录拦截器
 * @Author yanger
 * @Date 2020/9/15 15:38
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * Post handle
     *
     * @param request      request
     * @param response     response
     * @param handler      handler
     * @param modelAndView model and view
     * @since 1.0.0
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        String token = request.getHeader(JwtConst.HEADER_TOKEN_KEY);
        if (StringUtils.isNotEmpty(token)) {
            Date expiration = JwtUtils.getExpiration(token);
            LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(JwtConst.TOKEN_RENEWAL_TIME_MINUTE);
            ZoneId zoneId = ZoneId.systemDefault();
            Date timeline = Date.from(localDateTime.atZone(zoneId).toInstant());
            if (timeline.after(expiration)) {
                AuthUser authUser = JwtUtils.parse(AuthUser.class, token);
                response.setHeader(JwtConst.HEADER_TOKEN_KEY, JwtUtils.sign(authUser, JwtConst.TOKEN_AVAILABLE_TIME));
            }
        }
    }

    /**
     * 登录拦截
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return the boolean
     * @throws Exception exception
     * @since 1.0.0
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // @IgnoreLoginAuth 跳过登录
        if(handler instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod)handler;
            IgnoreLoginAuth classIgnoreLoginAuth = h.getMethod().getDeclaringClass().getAnnotation(IgnoreLoginAuth.class);
            IgnoreLoginAuth methodIgnoreLoginAuth = h.getMethodAnnotation(IgnoreLoginAuth.class);
            LoginAuth methodLoginAuth = h.getMethodAnnotation(LoginAuth.class);
            if ((classIgnoreLoginAuth != null || methodIgnoreLoginAuth != null) && methodLoginAuth == null) {
                return true;
            }
        }
        String token = request.getHeader(JwtConst.HEADER_TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            returnJsonMsg(response, R.failed(ResultCode.TOKE_INVALID_MSG.getCode(), "未获取到用户信息，请重新登录"));
            return false;
        } else {
            if (StringUtils.isNotEmpty(token)) {
                if(new Date().after(JwtUtils.getExpiration(token))) {
                    returnJsonMsg(response, R.failed(ResultCode.TOKE_INVALID_MSG.getCode(), "用户信息已过期，请重新登录"));
                    return false;
                }
                AuthUser authUser = JwtUtils.parse(AuthUser.class, token);
                request.setAttribute(JwtConst.REQUEST_ATTR_KEY, authUser);
            }
        }
        return true;
    }

    /**
     * 返回json信息
     *
     * @param response response
     * @param msg      msg
     * @throws IOException io exception
     * @since 1.0.0
     */
    private void returnJsonMsg(HttpServletResponse response, Object msg) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(JSON.toJSON(msg));
        writer.close();
        response.flushBuffer();
    }

}
