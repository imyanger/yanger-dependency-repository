package com.fkhwl.starter.cache;

import com.fkhwl.starter.common.event.BaseEventHandler;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.07.08 16:27
 * @since 1.5.0
 */
@Slf4j
public class KeyExpirationListenerAdapter extends BaseEventHandler<RedisKeyExpiredEvent<String>> {

    /** Handers */
    private final Map<String, KeyExpirationHander> handers = new HashMap<>();

    /**
     * Add hander
     *
     * @param key    key
     * @param hander hander
     * @since 1.5.0
     */
    public void addHander(String key, KeyExpirationHander hander) {
        this.handers.put(key, hander);
    }

    /**
     * Handler.
     *
     * @param event the event
     * @since 1.5.0
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
