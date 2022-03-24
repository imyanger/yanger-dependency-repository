package com.yanger.starter.web.config;

import com.yanger.starter.basic.config.BaseAutoConfiguration;
import com.yanger.starter.web.api.EnumApi;
import com.yanger.starter.web.api.HealthApi;
import com.yanger.starter.web.api.IndexApi;
import com.yanger.starter.web.property.EnumProperties;
import com.yanger.starter.web.property.TokenProperties;
import com.yanger.starter.web.service.DefaultILoginService;
import com.yanger.starter.web.service.ILoginService;
import com.yanger.starter.web.wx.agent.WxAgent;
import com.yanger.tools.web.tools.BeanUtils;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 装在基础 Bean 对象
 * @Author yanger
 * @Date 2021/2/1 17:38
 */
@Configuration
public class DefaultWebBeanConfiguration implements BaseAutoConfiguration {

    @Bean
    public IndexApi indexApi() {
        return new IndexApi();
    }

    @Bean
    public WxAgent wxAgent() {
        return new WxAgent();
    }

    @Bean
    public TokenProperties tokenConfig() {
        return new TokenProperties();
    }

    @Bean
    public ILoginService loginService() {
        ClassInfoList implClasses = new ClassGraph()
            .enableClassInfo()
            .scan()
            .getClassesImplementing(ILoginService.class.getName());
        AtomicReference<ILoginService> loginService = new AtomicReference<>();
        implClasses.forEach(s -> {
            String className = s.getName();
            if (!className.equals(DefaultILoginService.class.getName())) {
                loginService.set(BeanUtils.newInstance(className));
            }
        });
        return loginService.get() == null ? new DefaultILoginService() : loginService.get();
    }

    @Bean
    public EnumProperties enumConfig() {
        return new EnumProperties();
    }

    @Bean
    public EnumApi enumApi() {
        return new EnumApi();
    }

    @Bean
    public HealthApi healthApi() {
        return new HealthApi();
    }

}
