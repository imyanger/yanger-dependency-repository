package com.yanger.starter.web.config;

import com.yanger.starter.web.api.IndexApi;
import com.yanger.starter.web.service.DefaultLoginService;
import com.yanger.starter.web.service.LoginService;
import com.yanger.starter.web.wx.agent.WxAgent;
import com.yanger.tools.web.tools.BeanUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicReference;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;

/**
 * 装在基础 Bean 对象
 * @Author yanger
 * @Date 2021/2/1 17:38
 */
@Configuration
public class BaseBeanConfiguration {

    @Bean
    public IndexApi indexApi() {
        return new IndexApi();
    }

    @Bean
    public WxAgent wxAgent() {
        return new WxAgent();
    }

    @Bean
    public TokenConfig tokenConfig() {
        return new TokenConfig();
    }

    @Bean
    public LoginService loginService() {
        ClassInfoList implClasses = new ClassGraph()
            .enableClassInfo()
            .scan()
            .getClassesImplementing(LoginService.class.getName());
        AtomicReference<LoginService> loginService = new AtomicReference<>();
        implClasses.forEach(s -> {
            String className = s.getName();
            if (!className.equals(DefaultLoginService.class.getName())) {
                loginService.set(BeanUtils.newInstance(className));
            }
        });
        return loginService.get() == null ? new DefaultLoginService() : loginService.get();
    }

}
