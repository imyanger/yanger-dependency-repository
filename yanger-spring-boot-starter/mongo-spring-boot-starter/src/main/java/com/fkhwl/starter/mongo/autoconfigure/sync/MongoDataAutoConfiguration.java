package com.fkhwl.starter.mongo.autoconfigure.sync;

import com.fkhwl.starter.common.start.FkhAutoConfiguration;
import com.fkhwl.starter.core.util.CollectionUtils;
import com.fkhwl.starter.mongo.annotation.MongoCollection;
import com.fkhwl.starter.mongo.convert.CustomMongoMappingContext;
import com.fkhwl.starter.mongo.convert.FkhMongoCustomConversions;

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

import lombok.SneakyThrows;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.04 22:09
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoDataAutoConfiguration implements FkhAutoConfiguration {
    /** Converter customizers */
    private final List<ConverterCustomizer> converterCustomizers;

    /**
     * Mongo data configuration
     *
     * @param converterCustomizersProvider converter customizers provider
     * @since 1.0.0
     */
    MongoDataAutoConfiguration(@NotNull ObjectProvider<List<ConverterCustomizer>> converterCustomizersProvider) {
        this.converterCustomizers = converterCustomizersProvider.getIfAvailable();
    }

    /**
     * 自定义 mongodb 字段类型转换
     *
     * @param converterCustomizers converter customizers
     * @return the mongo custom conversions
     * @since 1.0.0
     */
    @Bean
    MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(this.converterCustomizers)) {
            this.converterCustomizers.forEach(l -> l.customize(converters));
        }

        return new FkhMongoCustomConversions(converters);
    }

    /**
     * 配置 mongo 映射上下文
     *
     * @param properties         properties
     * @param conversions        conversions
     * @param applicationContext application context
     * @return the mongo mapping context
     * @throws ClassNotFoundException class not found exception
     * @since 1.0.0
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
