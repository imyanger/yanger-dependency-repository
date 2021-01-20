package com.yanger.starter.basic.launcher;

import com.yanger.starter.basic.annotation.RunningType;
import com.yanger.starter.basic.constant.ClassName;
import com.yanger.starter.basic.enums.ApplicationType;
import com.yanger.starter.basic.util.YmlUtils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.lang.reflect.Constructor;
import java.net.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import cn.hutool.core.util.ClassLoaderUtil;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 基础启动类
 * @Author yanger
 * @Date 2020/12/29 16:12
 */
@Slf4j
public abstract class BasicApplication implements CommandLineRunner {

    /** started */
    private static boolean started = false;

    /** applicationClass */
    private static Class<?> applicationClass;

    /** applicationClassName */
    private static String applicationClassName;

    /** applicationType */
    private static ApplicationType applicationType = ApplicationType.deduceFromClasspath();

    /** START_CLASS_ARGS */
    public static final String START_CLASS_ARGS = "--start.class=";

    /**
     * 在容器刷新完成后执行逻辑, {@link CommandLineRunner#run(java.lang.String...)}
     *
     * @param args args
     */
    @Override
    public void run(String... args) {}

    /**
     * 在 spring 容器启动之前执行自定义逻辑.
     *
     * @since 1.0.0
     */
    protected void before() {}

    /**
     * 在启动完成后且在 {@link BasicApplication#after()} 之前发送事件.
     *
     * @param context the context
     * @since 1.0.0
     */
    protected void publishEvent(ConfigurableApplicationContext context) {}

    /**
     * 在 spring 容器启动完成后执行自定义逻辑.
     *
     * @since 1.0.0
     */
    protected void after() {}

    /**
     * springboot启动入口
     *
     * @param args the input arguments
     */
    @SuppressWarnings("checkstyle:UncommentedMain")
    public static void main(String[] args) {
        try {
            if (!started) {
                getYaml();
                // 获取参数中应用名称
                processAppNameFromArgs(args);
                // 启动类检查
                check();
                // 处理应用类型
                processApplicationType();
                // 反射实例化子类来启动 Spring Boot
                Constructor<?> constructor = applicationClass.getConstructor();
                BasicApplication application = (BasicApplication) constructor.newInstance();

                // before -> run -> publishEvent -> after
                application.before();
                ConfigurableApplicationContext configurableApplicationContext = application.start(args);

                started = true;

                if (applicationType == ApplicationType.SERVICE) {
                    HookRunner.start(configurableApplicationContext);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("启动失败: ", e);
        }
    }

    /**
     * 单元测试时使用, 作为内嵌应用.
     *
     * @param source source
     * @param args   args
     * @return the configurable application context
     * @throws Exception exception
     * @since 1.0.0
     */
    public static ConfigurableApplicationContext run(Class<?> source, String... args) throws Exception {
        return BasicRunner.start(source, applicationType, args);
    }

    /**
     * 从参数中获取启动类名, 此方法主要用于处理使用 shell 脚本启动时的增强处理
     *
     * @param args args
     * @since 1.0.0
     */
    private static void processAppNameFromArgs(String @NotNull [] args) {
        for (String arg : args) {
            if (arg.startsWith(START_CLASS_ARGS)) {
                applicationClassName = arg.substring(START_CLASS_ARGS.length());
                break;
            }
        }
    }

    /**
     * 检查启动类: 一个应用只允许存在一个被 @SpringBootApplication 标识的主类.
     *
     * @since 1.0.0
     */
    private static void check() {
        if (StringUtils.isBlank(applicationClassName)) {
            ClassInfoList subclasses = new ClassGraph()
                .enableClassInfo()
                .scan()
                .getSubclasses(BasicApplication.class.getName());
            if (CollectionUtils.isEmpty(subclasses)) {
                throw new IllegalStateException("错误原因: \n"
                                                + "没有找到 BasicApplication 的子类: 不能直接通过 BasicApplication.main() 启动, 必须通过子类启动, 写法如下: \n"
                                                + "@SpringBootApplication\n"
                                                + "public class DemoApplication extends BasicApplication {\n"
                                                + "    // 不需要写 main()\n"
                                                + "}");
            }
            if (subclasses.size() > 1) {
                throw new IllegalStateException("一个应用只允许存在一个启动类!");
            }
            applicationClassName = subclasses.get(0).getName();
        }

        verificationStartClass(applicationClassName);
    }

    /**
     * 校验启动类
     *
     * @param startClassName 启动类
     * @since 1.0.0
     */
    private static void verificationStartClass(String startClassName) {
        try {
            Class<?> mainClass = ClassLoaderUtil.getClassLoader().loadClass(startClassName);
            boolean matched = Arrays.stream(mainClass.getAnnotations())
                .anyMatch(m -> m.annotationType().getName().matches(ClassName.SPRING_BOOT_APPLICATION) ||
                               m.annotationType().getName().matches(ClassName.ENABLE_AUTOCONFIGURATION));
            if (!matched) {
                throw new IllegalStateException("启动类必须标注 @SpringBootApplication 或者 @EnableAutoConfiguration 注解");
            }
            applicationClass = mainClass;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * 判断应用类型
     *
     * @since 1.0.0
     */
    private static void processApplicationType() {
        RunningType runningType = AnnotationUtils.findAnnotation(applicationClass, RunningType.class);
        if (runningType != null) {
            applicationType = runningType.value();
        }
        processRunningType();
    }

    /**
     * 校验应用类型和依赖
     *
     * @since 1.0.0
     */
    private static void processRunningType() {
        ApplicationType automaticDeterminationType = ApplicationType.deduceFromClasspath();
        switch (applicationType) {
            case NONE:
                if (automaticDeterminationType == ApplicationType.SERVLET || automaticDeterminationType == ApplicationType.REACTIVE) {
                    log.warn("当前应用已设置为非 WEB 应用，但是存在 WEB 相关依赖，请删除以减少部署包体积");
                }
                break;
            case SERVICE:
                if (automaticDeterminationType == ApplicationType.SERVLET || automaticDeterminationType == ApplicationType.REACTIVE) {
                    log.warn("当前应用已设置非 WEB 应用，但是存在 WEB 相关依赖，请删除以减少部署包体积");
                }
                break;
            case SERVLET:
                if (automaticDeterminationType == ApplicationType.NONE) {
                    throw new IllegalStateException("当前应用已设置 WEB 应用，但是不存在 WEB(Servlet) 相关依赖");
                }
                break;
            case REACTIVE:
                if (automaticDeterminationType == ApplicationType.NONE) {
                    throw new IllegalStateException("当前应用已设置 WEB 应用，但是不存在 WEB(Web.flux) 相关依赖");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 启动流程
     *
     * @param args args
     * @return the configurable application context
     * @throws Exception exception
     * @since 1.0.0
     */
    private ConfigurableApplicationContext start(String[] args) throws Exception {
        ConfigurableApplicationContext context = run(applicationClass, args);
        this.publishEvent(context);
        this.callRunners(context);
        return context;
    }

    /**
     * 启动完成后执行逻辑
     *
     * @param context context
     * @since 1.5.0
     */
    private void callRunners(@NotNull ApplicationContext context) {
        Collection<BasicApplication> values = context.getBeansOfType(BasicApplication.class).values();
        values.forEach(BasicApplication::after);
    }

    private static void getYaml(){
        try{

            // ResourceUtils.getURL("classpath:").getPath().
            Map<String,Object> map = YmlUtils.getYamlPropValue(ResourceUtils.getURL("classpath:").getPath() + "/application.yml");
            System.out.println(map);
            // Object val = YmlUtils.getPropValue(map, "spring.servlet.multipart.max-file-size");
        }catch (Exception e){

        }
    }

}
