package com.fkhwl.starter.mongo.autoconfigure.sync;

import com.fkhwl.starter.mongo.handler.DefaultTimeMetaHandler;
import com.fkhwl.starter.mongo.handler.MetaObjectHandler;
import com.fkhwl.starter.mongo.listener.AutoCreateIndexEventListener;
import com.fkhwl.starter.mongo.listener.AutoCreateKeyEventListener;
import com.fkhwl.starter.mongo.listener.AutoCreateTimeEventListener;
import com.fkhwl.starter.mongo.listener.AutoIncrementKeyEventListener;
import com.fkhwl.starter.mongo.spi.MongoLauncherInitiation;
import com.yanger.starter.basic.boost.YangerAutoConfiguration;
import com.yanger.starter.basic.constant.ConfigKey;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(MongoProperties.class)
@ConditionalOnClass(value = {MongoLauncherInitiation.class})
@Import(value = {MongoAutoConfiguration.class})
public class MongoEventListenerAutoConfiguration implements YangerAutoConfiguration {

    /** Meta object handler */
    private MetaObjectHandler metaObjectHandler;

    /**
     * Mongo event l istener auto configuration
     *
     * @param metaObjectHandlerObjectProvider meta object handler object provider
     */
    public MongoEventListenerAutoConfiguration(@NotNull ObjectProvider<MetaObjectHandler> metaObjectHandlerObjectProvider) {
        this.metaObjectHandler = metaObjectHandlerObjectProvider.getIfAvailable();
        if (this.metaObjectHandler == null) {
            this.metaObjectHandler = new DefaultTimeMetaHandler();
        }
    }

    /**
     * 自动生成 id 且自增
     *
     * @param mongoTemplate mongo template
     * @return the abstract mongo event listener
     */
    @Bean
    @ConditionalOnProperty(name = ConfigKey.MongoConfigKey.ENABLE_AUTO_INCREMENT_KEY, havingValue = "true")
    public AbstractMongoEventListener<?> autoIncrementKeyEventListener(MongoTemplate mongoTemplate) {
        return new AutoIncrementKeyEventListener(mongoTemplate);
    }

    /**
     * 自动生成 id (默认开启)
     *
     * @param properties properties
     * @return the abstract mongo event listener
     */
    @Bean
    @ConditionalOnProperty(name = ConfigKey.MongoConfigKey.ENABLE_AUTO_CREATE_KEY, havingValue = "true", matchIfMissing = true)
    public AbstractMongoEventListener<?> autoCreateKeyEventListener(@NotNull MongoProperties properties) {
        return new AutoCreateKeyEventListener(properties.isEnableAutoIncrementKey());
    }

    /**
     * 自动填充 create time 和 update time
     *
     * @return the abstract mongo event listener
     */
    @Bean
    @ConditionalOnProperty(name = ConfigKey.MongoConfigKey.ENABLE_AUTO_CREATE_TIME, havingValue = "true")
    public AbstractMongoEventListener<?> autoCreateTimeEventListener() {
        return new AutoCreateTimeEventListener(this.metaObjectHandler);
    }

    /**
     * 分表时自动创建索引
     *
     * @return the abstract mongo event listener
     */
    @Bean
    @ConditionalOnProperty(name = ConfigKey.MongoConfigKey.ENABLE_AUTO_CREATE_INDEX, havingValue = "true")
    public AbstractMongoEventListener<?> autoCreateIndexEventListener() {
        return new AutoCreateIndexEventListener();
    }

}
