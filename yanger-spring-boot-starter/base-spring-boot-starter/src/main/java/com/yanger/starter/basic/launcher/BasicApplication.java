package com.yanger.starter.basic.launcher;

import cn.hutool.core.util.ClassLoaderUtil;
import com.yanger.starter.basic.annotation.RunningType;
import com.yanger.starter.basic.constant.App;
import com.yanger.starter.basic.constant.FullyQualifiedName;
import com.yanger.starter.basic.enums.ApplicationType;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;

/**
 * 基础启动类
 * @Author yanger
 * @Date 2020/12/29 16:12
 */
@Slf4j
public abstract class BasicApplication implements CommandLineRunner {

    /** started */
    private static boolean started = false;

    /**
     * 在容器刷新完成后执行逻辑, {@link CommandLineRunner#run(java.lang.String...)}
     * @param args args
     */
    @Override
    public void run(String... args) {}

    /**
     * 在 spring 容器启动之前执行自定义逻辑.
     */
    protected void before() {}

    /**
     * 在 spring 容器启动完成后执行自定义逻辑.
     */
    protected void after(ApplicationContext context) {}

    /**
     * springboot启动入口：before -> run -> after
     * @param args the input arguments
     */
    @SuppressWarnings("checkstyle:UncommentedMain")
    public static void main(String[] args) {
        try {
            if (!started) {
                // 获取应用名称
                processAppName();
                // 处理应用类型
                processApplicationType();

                // 反射实例化子类来启动 Spring Boot
                BasicApplication application = (BasicApplication) App.applicationClass.getConstructor().newInstance();
                ConfigurableApplicationContext configurableApplicationContext = application.start(args);

                started = true;

                // service hook thread
                if (App.applicationType == ApplicationType.SERVICE) {
                    HookRunner.start(configurableApplicationContext);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("BasicApplication 启动失败: ", e);
        }
    }

    /**
     * 启动
     * @param args args
     * @return the configurable application context
     * @throws Exception exception
     */
    private ConfigurableApplicationContext start(String[] args) throws Exception {
        this.before();
        ConfigurableApplicationContext context = run(App.applicationClass, args);
        this.after(context);
        return context;
    }

    /**
     * 单元测试时使用, 作为内嵌应用
     * @param source source
     * @param args   args
     * @return the configurable application context
     * @throws Exception exception
     */
    public static ConfigurableApplicationContext run(Class<?> source, String... args) throws Exception {
        return BasicRunner.start(source, App.applicationType, args);
    }

    /**
     * 获取应用名称
     */
    private static void processAppName() {
        ClassInfo classInfo = check();
        String applicationClassName = classInfo.getName();
        App.applicationClassName = applicationClassName;
        try {
            Class<?> mainClass = ClassLoaderUtil.getClassLoader().loadClass(applicationClassName);
            App.applicationClass = mainClass;
            checkStartClass(mainClass);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * 检查启动类: 一个应用只允许存在一个被 @SpringBootApplication 标识的主类.
     */
    private static ClassInfo check() {
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
        return subclasses.get(0);
    }

    /**
     * 校验启动类
     * @param mainClass 启动类
     */
    private static void checkStartClass(Class<?> mainClass) {
        boolean matched = Arrays.stream(mainClass.getAnnotations())
            .anyMatch(m -> m.annotationType().getName().matches(FullyQualifiedName.SPRING_BOOT_APPLICATION) ||
                           m.annotationType().getName().matches(FullyQualifiedName.ENABLE_AUTOCONFIGURATION));
        if (!matched) {
            throw new IllegalStateException("启动类必须标注 @SpringBootApplication 或者 @EnableAutoConfiguration 注解");
        }
    }

    /**
     * 判断应用类型
     */
    private static void processApplicationType() {
        RunningType runningType = AnnotationUtils.findAnnotation(App.applicationClass, RunningType.class);
        if (runningType != null) {
            App.applicationType = runningType.value();
        }
        checkRunningType();
    }

    /**
     * 校验应用类型和依赖
     */
    private static void checkRunningType() {
        ApplicationType automaticDeterminationType = ApplicationType.deduceFromClasspath();
        switch (App.applicationType) {
            case NONE:
                if (automaticDeterminationType == ApplicationType.SERVLET || automaticDeterminationType == ApplicationType.REACTIVE) {
                    log.warn("当前应用已设置为非 WEB 应用，请检查 WEB 相关依赖，请删除冗余依赖以减少部署包体积");
                }
                break;
            case SERVICE:
                if (automaticDeterminationType == ApplicationType.SERVLET || automaticDeterminationType == ApplicationType.REACTIVE) {
                    log.warn("当前应用已设置非 WEB 应用，请检查 WEB 相关依赖，请删除冗余依赖以减少部署包体积");
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

}
