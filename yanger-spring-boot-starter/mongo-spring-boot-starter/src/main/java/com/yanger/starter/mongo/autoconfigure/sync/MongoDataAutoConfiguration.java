package com.yanger.starter.mongo.autoconfigure.sync;

import com.yanger.starter.basic.config.BaseAutoConfiguration;
import com.yanger.starter.mongo.annotation.MongoCollection;
import com.yanger.starter.mongo.convert.CustomMongoMappingContext;
import com.yanger.starter.mongo.convert.DefaultMongoCustomConversions;
import com.yanger.starter.mongo.property.MongoProperties;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoDataAutoConfiguration implements BaseAutoConfiguration {

    /** Converter customizers */
    private final List<ConverterCustomizer> converterCustomizers;

    /**
     * Mongo data configuration
     * @param converterCustomizersProvider converter customizers provider
     */
    MongoDataAutoConfiguration(@NotNull ObjectProvider<List<ConverterCustomizer>> converterCustomizersProvider) {
        this.converterCustomizers = converterCustomizersProvider.getIfAvailable();
    }

    /**
     * 自定义 mongodb 字段类型转换
     * @return the mongo custom conversions
     */
    @Bean
    MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(this.converterCustomizers)) {
            this.converterCustomizers.forEach(l -> l.customize(converters));
        }

        return new DefaultMongoCustomConversions(converters);
    }

    /**
     * 配置 mongo 映射上下文
     * @param properties         properties
     * @param conversions        conversions
     * @param applicationContext application context
     * @return the mongo mapping context
     * @throws ClassNotFoundException class not found exception
     */
    @Bean
    @SneakyThrows
    MongoMappingContext mongoMappingContext(@NotNull MongoProperties properties,
                                            MongoCustomConversions conversions,
                                            ApplicationContext applicationContext) {
        PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        MongoMappingContext context = new CustomMongoMappingContext();
        // 关闭自动创建 index, 由 ConsumerMongoPersistentEntityIndexCreator 接管.
        mapper.from(false).to(context::setAutoIndexCreation);
        // 扫描 mongo 实体
        context.setInitialEntitySet(new EntityScanner(applicationContext).scan(MongoCollection.class));
        // 配置字段映射关系
        Class<?> strategyClass = properties.getFieldNamingStrategy();
        if (strategyClass != null) {
            context.setFieldNamingStrategy((FieldNamingStrategy) BeanUtils.instantiateClass(strategyClass));
        }
        context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
        return context;

    }

}
