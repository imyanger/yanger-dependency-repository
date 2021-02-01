package com.yanger.starter.mybatis.listener;

import com.google.common.collect.Maps;

import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.function.CheckedRunnable;
import com.yanger.tools.web.exception.BasicException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 抽象所有事件处理器的公共代码
 *     * 以下事件全部由 {@link org.springframework.boot.context.event.EventPublishingRunListener} 发布
 *     * 每个实现类都必须重写后的方法的执行次数, 因为 spring boot 和 spring cloud 的原因, 会存在多个上下文, 因此会执行多次自定义事件处理逻辑
 * @Author yanger
 * @Date 2021/1/29 11:52
 */
public interface YangerApplicationListener extends GenericApplicationListener {

    /**
     * spring cloud 会通过 {org.springframework.cloud.context.restart.RestartListener} 再次发布
     * ApplicationPreparedEvent, ContextRefreshedEvent, ContextClosedEvent 这 3 个事件, 导致自定义事件被重复处理.
     * Spring Cloud 的引导上下文核心代码可见 {org.springframework.cloud.bootstrap.BootstrapApplicationListener},
     * 其中会再次调用 SpringApplicationBuilder.run() 方法执行
     *
     * @param event the even
     */
    @Override
    default void onApplicationEvent(@NotNull ApplicationEvent event) {

        if (event instanceof ApplicationStartingEvent) {
            this.onApplicationStartingEvent((ApplicationStartingEvent) event);
        } else if (event instanceof ApplicationEnvironmentPreparedEvent) {
            this.onApplicationEnvironmentPreparedEvent((ApplicationEnvironmentPreparedEvent) event);
        } else if (event instanceof ApplicationContextInitializedEvent) {
            this.onApplicationContextInitializedEvent((ApplicationContextInitializedEvent) event);
        } else if (event instanceof ApplicationPreparedEvent) {
            this.onApplicationPreparedEvent((ApplicationPreparedEvent) event);
        } else if (event instanceof ApplicationStartedEvent) {
            this.onApplicationStartedEvent((ApplicationStartedEvent) event);
        } else if (event instanceof ApplicationReadyEvent) {
            this.onApplicationReadyEvent((ApplicationReadyEvent) event);
        } else if (event instanceof ApplicationFailedEvent) {
            this.onApplicationFailedEvent((ApplicationFailedEvent) event);
        } else if (event instanceof WebServerInitializedEvent) {
            this.onWebServerInitializedEvent((WebServerInitializedEvent) event);
        } else if (event instanceof ContextRefreshedEvent) {
            this.onContextRefreshedEvent((ContextRefreshedEvent) event);
        } else if (event instanceof ContextStartedEvent) {
            this.onContextStartedEvent((ContextStartedEvent) event);
        } else if (event instanceof ContextStoppedEvent) {
            this.onContextStoppedEvent((ContextStoppedEvent) event);
        } else if (event instanceof ContextClosedEvent) {
            this.onContextClosedEvent((ContextClosedEvent) event);
        }

    }

    /**
     * 应用启动时事件处理器
     * {@link org.springframework.boot.SpringApplicationRunListener#starting()}
     * 在 {@link org.springframework.boot.SpringApplication#run(String...)} 中被调用
     *
     * @param event the event
     */
    default void onApplicationStartingEvent(ApplicationStartingEvent event) { }

    /**
     * 所有环境配置准备完成后被调用, 且在 {@link ApplicationContext} 被创建之前执行
     *
     * @param event the event
     */
    default void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event) { }

    /**
     * 当 {@link ApplicationContext} 被创建且初始化完成后执行
     *
     * @param event the event
     */
    default void onApplicationContextInitializedEvent(ApplicationContextInitializedEvent event) { }

    /**
     * 当 {@link ApplicationContext} 被加载后(全部的 bean 都加载到 ICO)执行
     *
     * @param event the event
     */
    default void onApplicationPreparedEvent(ApplicationPreparedEvent event) { }

    /**
     * {@link ApplicationContext} 的所有 bean 处理器处理完成后执行,
     * 这发生在 {@link org.springframework.boot.CommandLineRunner} 和 {@link org.springframework.boot.ApplicationRunner} 之前
     * refresh --> {@link org.springframework.context.support.AbstractApplicationContext#refresh()}
     *
     * @param event the event
     */
    default void onApplicationStartedEvent(ApplicationStartedEvent event) { }

    /**
     * 当所有的 {@link org.springframework.boot.CommandLineRunner}, {@link org.springframework.boot.ApplicationRunner}
     * 和 {@link org.springframework.context.support.AbstractApplicationContext#refresh()} 执行完成之后,
     * 且在 {@link org.springframework.boot.SpringApplication#run(String...)} 执行完成之前调用
     *
     * @param event the event
     */
    default void onApplicationReadyEvent(ApplicationReadyEvent event) { }

    /**
     * 运行应用程序时发生故障时调用
     *
     * @param event the event
     */
    default void onApplicationFailedEvent(ApplicationFailedEvent event) { }

    /**
     * Event to be published after the application context is refreshed and the
     * {@link org.springframework.boot.web.server.WebServer} is ready.
     * Useful for obtaining the local port of a running server.
     *
     * @param event the event
     */
    default void onWebServerInitializedEvent(WebServerInitializedEvent event) { }

    /**
     * 完成 refresh 之后执行 (所有对象已被实例化和初始化之后)
     * {org.springframework.context.support.AbstractApplicationContext#finishRefresh()}
     *
     * @param event the event
     */
    default void onContextRefreshedEvent(ContextRefreshedEvent event) { }

    /**
     * 当 Spring 容器加载所有 bean 并完成初始化之后(refreshed), 接着回调实现 {@link org.springframework.context.SmartLifecycle#start()} 接口的类中 start()
     * 当上下文被刷新 (所有对象已被实例化和初始化之后) 时,将调用该方法,默认生命周期处理器将检查每个 SmartLifecycle 对象的 isAutoStartup()方法返回的布尔值,
     * 如果为 true 则该方法会被调用,而不是等待显式调用自己的start()方法
     *
     * @param event the event
     */
    default void onContextStartedEvent(ContextStartedEvent event) { }

    /**
     * 接口 Lifecycle 的子类的方法,只有非 SmartLifecycle 的子类才会执行该方法
     * 1. 该方法只对直接实现接口 Lifecycle 的类才起作用,对实现 SmartLifecycle 接口的类无效
     * 2. 方法 stop() 和方法 stop(Runnable callback) 的区别只在于后者是 SmartLifecycle 子类的专属.
     * {@link org.springframework.context.SmartLifecycle#stop()}
     *
     * @param event the event
     */
    default void onContextStoppedEvent(ContextStoppedEvent event) { }

    /**
     * 当容器关闭时执行
     *
     * @param event the event
     */
    default void onContextClosedEvent(ContextClosedEvent event) { }

    /**
     * Supports event types boolean.
     *
     * @param resolvableType the resolvable type
     * @return the boolean
     */
    @Override
    default boolean supportsEventType(@NotNull ResolvableType resolvableType) {
        return this.isAssignableFrom(resolvableType.getRawClass(), Constant.EVENT_TYPES);
    }

    /**
     * Supports source types boolean.
     *
     * @param sourceType the source type
     * @return the boolean
     */
    @Override
    default boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    /**
     * Is assignable from boolean.
     *
     * @param type           the type
     * @param supportedTypes the supported types
     * @return the boolean
     */
    @Contract("null, _ -> false")
    default boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @UtilityClass
    class Constant {
        /** 定义需要处理的事件类型 */
        static final Class<?>[] EVENT_TYPES = {
            ApplicationStartingEvent.class,
            ApplicationEnvironmentPreparedEvent.class,
            ApplicationContextInitializedEvent.class,
            ApplicationPreparedEvent.class,
            ApplicationStartedEvent.class,
            ApplicationReadyEvent.class,
            ApplicationFailedEvent.class,
            WebServerInitializedEvent.class,
            ContextRefreshedEvent.class,
            ContextStartedEvent.class,
            ContextStoppedEvent.class,
            ContextClosedEvent.class
        };
    }

    /**
     * 生成唯一 key
     *
     * @param event    event
     * @param subClass sub class
     * @return the string
     */
    default String key(@NotNull ApplicationEvent event, @NotNull Class<? extends YangerApplicationListener> subClass) {
        return String.join(StringPool.COLON, event.getClass().getName(), subClass.getName());
    }

    /**
     * 统计 listener 执行次数, 提供在第几次执行 listener, 主要用于解决在 spring cloud 下多次执行 listener 的问题
     */
    @Slf4j
    class Runner {
        /** 执行次数统计 */
        private static final Map<String, Integer> EXECTIE_COUNT_MAP = Maps.newConcurrentMap();
        /** listener 执行次数 */
        private static final AtomicInteger EXECUTE_COUNT = new AtomicInteger(0);

        /**
         * 执行后增加次数
         *
         * @param key key
         */
        public static void execution(String key) {
            EXECTIE_COUNT_MAP.merge(key, 1, Integer::sum);
        }

        /**
         * 是否执行过
         *
         * @param key key
         * @return the boolean      执行过返回 true
         */
        @Contract(pure = true)
        public static boolean executed(String key) {
            return EXECTIE_COUNT_MAP.get(key) != null;
        }

        /**
         * 统计执行次数 (使用有个优先级最高的 listener 统计执行次数)
         */
        public static void executeCount() {
            EXECUTE_COUNT.getAndIncrement();
        }

        /**
         * Get execute count
         *
         * @return the int
         */
        public static int getExecuteTotalCount() {
            return EXECUTE_COUNT.get();
        }

        /**
         * 获取已经执行过的次数
         *
         * @param key key
         * @return the execute current count
         */
        @Contract(pure = true)
        public static int getExecuteCurrentCount(String key) {
            return EXECTIE_COUNT_MAP.get(key) == null ? 0 : EXECTIE_COUNT_MAP.get(key);
        }

        /**
         * 第一次执行, 需要注意的是如果是 spring cloud 项目, 第一次执行只能读取到 bootstrap.yml 配置, application.yml 配置只能在第二次才能读取到.
         *
         * @param key      key
         * @param consumer consumer
         */
        @Contract(pure = true)
        public static void executeAtFirst(String key, CheckedRunnable consumer) {
            executeAt(key, 1, consumer);
        }

        /**
         * 在最后一次执行, 需要注意的是, 如果是 spring cloud 项目, 最后一次执行只能读取到 application[-{env}].yml 配置, 不会再读取 bootstrap.yml.
         *
         * @param key      key
         * @param consumer consumer
         */
        @Contract(pure = true)
        public static void executeAtLast(String key, CheckedRunnable consumer) {
            executeAt(key, EXECUTE_COUNT.get(), consumer);
        }

        /**
         * 在指定的 index 执行
         *
         * @param key      key
         * @param index    index
         * @param consumer consumer
         */
        public static void executeAt(String key, int index, CheckedRunnable consumer) {
            execution(key);
            if (index < 0 || index > EXECUTE_COUNT.get()) {
                throw new BasicException("index error");
            }

            if (getExecuteCurrentCount(key) == index) {
                execute(consumer);
            }
        }

        /**
         * 每次都执行
         *
         * @param consumer consumer
         */
        public static void execute(CheckedRunnable consumer) {
            try {
                consumer.run();
            } catch (Throwable throwable) {
                log.error("", throwable);
            }
        }
    }

}
