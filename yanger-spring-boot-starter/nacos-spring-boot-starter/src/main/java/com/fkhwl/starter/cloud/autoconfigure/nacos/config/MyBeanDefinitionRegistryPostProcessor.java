// package com.fkhwl.starter.cloud.autoconfigure.nacos.config;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.BeansException;
// import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
// import org.springframework.beans.factory.support.BeanDefinitionBuilder;
// import org.springframework.beans.factory.support.BeanDefinitionRegistry;
// import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
//
// import java.util.Map;
//
// /**
//  * @Description TODO
//  * @Author yanger
//  * @Date 2021/2/25 9:40
//  */
// public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
//
//
//     private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//     @Override
//     public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
//
//         logger.info("bean 定义查看和修改...");
//
//         String beanName = "NacosPropertySourceLocator";
//
//         // 先移除原来的bean定义
//         beanDefinitionRegistry.removeBeanDefinition(beanName);
//
//         // 注册我们自己的bean定义
//         BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(YangerNacosBootstrapConfiguration.FkhNacosPropertySourceLocator.class);
//         // 如果有构造函数参数, 有几个构造函数的参数就设置几个 没有就不用设置
//         beanDefinitionBuilder.addConstructorArgValue("构造参数1");
//         beanDefinitionBuilder.addConstructorArgValue("构造参数2");
//         beanDefinitionBuilder.addConstructorArgValue("构造参数3");
//         // 设置 init方法 没有就不用设置
//         beanDefinitionBuilder.setInitMethodName("init");
//         // 设置 destory方法 没有就不用设置
//         beanDefinitionBuilder.setDestroyMethodName("destory");
//         // 将Bean 的定义注册到Spring环境
//         beanDefinitionRegistry.registerBeanDefinition("myTestService", beanDefinitionBuilder.getBeanDefinition());
//     }
//
//     @Override
//     public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//
//         // bean的名字为key, bean的实例为value
//         Map<String, Object> beanMap = configurableListableBeanFactory.getBeansWithAnnotation(RestController.class);
//         logger.info("所有 RestController 的bean {}", beanMap);
//     }
//
// }