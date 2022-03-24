package com.yanger.starter.cache.handler;

import com.yanger.starter.basic.event.BaseEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * 过期时间处理器
 * @Author yanger
 * @Date 2021/3/1 18:47
 */
@Slf4j
public class KeyExpirationListenerAdapter extends BaseEventHandler<RedisKeyExpiredEvent<String>> {

    /** Handers */
    private final Map<String, KeyExpirationHander> handers = new HashMap<>();

    /**
     * Add hander
     * @param key    key
     * @param hander hander
     */
    public void addHander(String key, KeyExpirationHander hander) {
        this.handers.put(key, hander);
    }

    /**
     * Handler
     * @param event the event
     */
    @Override
    @EventListener
    public void handler(@NotNull RedisKeyExpiredEvent<String> event) {
        log.trace("redis key expired event: {}", event);
        KeyExpirationHander keyExpirationHander = this.handers.get(new String(event.getId()));
        if (keyExpirationHander != null) {
            keyExpirationHander.hander();
            this.handers.remove(event.getKeyspace());
        }
    }

}
