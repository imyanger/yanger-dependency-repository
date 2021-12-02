package com.yanger.starter.basic.launcher;

import com.yanger.starter.basic.enums.ApplicationType;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理主类上的启动标识, 如果是 {@link ApplicationType#SERVICE} 则启动一个守护线程, 防止主线程退出
 * @Author yanger
 * @Date 2020/12/29 17:07
 */
@Slf4j
public final class HookRunner {

    /** LOCK */
    private static final ReentrantLock LOCK = new ReentrantLock();

    /** STOP */
    private static final Condition STOP = LOCK.newCondition();

    /**
     * 启动守护线程
     *
     * @param applicationContext
     */
    public static void start(ConfigurableApplicationContext applicationContext) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                applicationContext.stop();
            } catch (Exception e) {
                log.error("BasicApplication stop exception.", e);
            }
            log.info("JVM exited, All service stopped.");
            LOCK.lock();
            try {
                STOP.signal();
            } finally {
                LOCK.unlock();
            }
        }, "Shutdown-Hook"));
        // 主线程阻塞等待, 守护线程释放锁后退出
        LOCK.lock();
        try {
            STOP.await();
        } catch (InterruptedException e) {
            log.warn("service stopped, interrupted by other thread.", e);
        } finally {
            LOCK.unlock();
        }
    }

}
