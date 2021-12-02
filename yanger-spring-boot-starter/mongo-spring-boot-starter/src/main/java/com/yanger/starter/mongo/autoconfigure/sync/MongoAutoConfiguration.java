package com.yanger.starter.mongo.autoconfigure.sync;

import com.google.common.collect.Lists;

import com.mongodb.ConnectionString;
import com.yanger.starter.basic.boost.YangerAutoConfiguration;
import com.yanger.starter.basic.context.EarlySpringContext;
import com.yanger.starter.basic.exception.PropertiesException;
import com.yanger.starter.mongo.annotation.MongoCollection;
import com.yanger.starter.mongo.core.MongoBean;
import com.yanger.starter.mongo.exception.MongoException;
import com.yanger.starter.mongo.factory.MongoProviderFactory;
import com.yanger.starter.mongo.index.CustomMongoPersistentEntityIndexCreator;
import com.yanger.starter.mongo.scanner.EntityScanner;
import com.yanger.starter.mongo.spi.MongoLauncherInitiation;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.tools.StringTools;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexCreator;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * mongodb 自动配置类
 *     https://docs.spring.io/spring-data/mongodb/docs/2.2.1.RELEASE/reference/html
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(MongoProperties.class)
@ConditionalOnClass(value = {MongoLauncherInitiation.class})
@Import(value = {MongoDataAutoConfiguration.class})
public class MongoAutoConfiguration implements YangerAutoConfiguration {

    /**
     * Mongo provider factory (返回默认的数据源对应的 MongoTemplate).
     *
     * @param mongoProperties    the mongo properties
     * @param applicationContext application context
     * @param context            context
     * @param conversions        conversions
     * @return the mongo provider factory
     */
    @Bean
    @Primary
    public MongoTemplate mongoTemplate(@NotNull MongoProperties mongoProperties,
                                       ConfigurableApplicationContext applicationContext,
                                       MongoMappingContext context,
                                       MongoCustomConversions conversions) {

        Map<String, String> datasource = mongoProperties.getDatasource();

        // 配置了第一个数据源没有写具体配置或者配置了一个数据源名称不叫 default
        if (datasource.size() == 0) {
            this.addDefaultDatasource(datasource);
        }

        // 配置了一个数据源, 当名称不是 default
        if (datasource.size() == 1 && datasource.get(MongoProperties.DEFAULT_DATASOURCE) == null) {
            throw new MongoException("请使用 default 作为数据源名称");
        } else if (datasource.size() == 1 && datasource.get(MongoProperties.DEFAULT_DATASOURCE).equals(StringPool.EMPTY)) {
            // 配置了一个数据源, 但是 value 没有配置
            this.addDefaultDatasource(datasource);
        }

        // 默认数据源连接, 只可能存在一个
        List<String> defauleDatasourceConnectionString = Lists.newArrayListWithCapacity(1);

        datasource.forEach((k, v) -> {
            // 保存 defaule 配置
            if (k.equals(MongoProperties.DEFAULT_DATASOURCE)) {
                defauleDatasourceConnectionString.add(v);
            } else {
                // 处理非默认数据源
                this.initDatasource(k, v,
                                    mongoProperties,
                                    // mongoClientOptions,
                                    applicationContext,
                                    context,
                                    conversions);
            }
        });

        if (defauleDatasourceConnectionString.size() != 1) {
            throw new PropertiesException("默认数据源只能存在一个.");
        }

        // 添加默认数据源
        String defaultUri = defauleDatasourceConnectionString.get(0);
        try {
            // 使用 ConnectionString 检查 url, 错误将抛出异常
            new ConnectionString(defaultUri);
            // 初始化默认数据源
            this.initDatasource(MongoProperties.DEFAULT_DATASOURCE,
                                defaultUri,
                                mongoProperties,
                                // mongoClientOptions,
                                applicationContext,
                                context,
                                conversions);
        } catch (Exception e) {
            // 不是合法的 URL, 则查找现有数据源
            MongoTemplate defaultTemplate = MongoProviderFactory.getConfigureWithMongoTemplate(defaultUri);
            if (defaultTemplate == null) {
                throw new PropertiesException("指定的默认数据源不存在: [{}]", defaultUri);
            }
            // 添加默认数据源
            MongoProviderFactory.addConfigureWithMongoTemplate(MongoProperties.DEFAULT_DATASOURCE, defaultTemplate);
        }

        this.scanEntity(mongoProperties.getScanPath(), applicationContext);
        return MongoProviderFactory.getConfigureWithMongoTemplate(MongoProperties.DEFAULT_DATASOURCE);
    }

    /**
     * 添加默认数据源
     *
     * @param datasource datasource
     */
    private void addDefaultDatasource(@NotNull Map<String, String> datasource) {
        datasource.put(MongoProperties.DEFAULT_DATASOURCE, "mongodb://127.0.0.1:27017/dev");
        log.warn("未配置默认数据源, 将使用默认配置: [mongodb://127.0.0.1:27017/dev], "
                 + "格式: mongodb://[username:password@]host1[:port1][,...hostN[:portN]][/[defaultauthdb][?options]]");
    }

    /**
     * Init datasource *
     *
     * @param datasourceName     datasource name
     * @param datasourceUri      datasource uri
     * @param mongoProperties    mongo properties
     * @param applicationContext application context
     * @param context            context
     * @param conversions        conversions
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    private void initDatasource(String datasourceName,
                                String datasourceUri,
                                @NotNull MongoProperties mongoProperties,
                                ConfigurableApplicationContext applicationContext,
                                MongoMappingContext context,
                                MongoCustomConversions conversions) {
        // 1. 通过 dataSourceUri 创建 MongoClient
        // MongoClient mongoClient = this.createMongoClient(mongoClientOptions, datasourceUri);
        // 2. 通过 MongoClient 创建 MongoDbFactory
        SimpleMongoClientDatabaseFactory mongoDbFactory = createMongoDbFactory(datasourceUri);
        // 3. 配置字段转换器
        MappingMongoConverter mappingMongoConverter = this.buildMappingMongoConverter(mongoDbFactory, context, conversions,
                                                                                      mongoProperties);
        // 4. 通过 MongoDbFactory 创建 MongoTemplate
        MongoTemplate mongoTemplate = createMongoTemplate(mongoDbFactory, mappingMongoConverter);
        mongoTemplate.setApplicationContext(applicationContext);
        // 5. 注入 mongoTemplate
        this.registerMongoTemplate(datasourceName, applicationContext, mongoTemplate);
        this.processorIndexes(mongoProperties, applicationContext, context, datasourceName, mongoTemplate);
        // 6. 保存 dataSource 与 MongoTemplate 的关系
        MongoProviderFactory.addConfigureWithMongoTemplate(datasourceName, mongoTemplate);
        // 7. 保存 MongoTransactionManager
        MongoProviderFactory.addMongoTransactionTemplate(mongoTemplate, new TransactionTemplate(this.transactionManager(mongoTemplate)));
    }

    /**
     * 将 mongoTemplate 注入到 IoC, bean name = datasource + MongoTemplate
     *
     * @param datasourceName     datasource name
     * @param applicationContext application context
     * @param mongoTemplate      mongo template
     */
    private void registerMongoTemplate(String datasourceName,
                                       ConfigurableApplicationContext applicationContext,
                                       MongoTemplate mongoTemplate) {
        EarlySpringContext.registerBean(applicationContext,
                                        datasourceName + MongoTemplate.class.getSimpleName(),
                                        mongoTemplate);
    }

    /**
     * 新增集合时自动创建索引
     *
     * @param mongoProperties    mongo properties
     * @param applicationContext application context
     * @param context            context
     * @param datasourceName     datasource name
     * @param mongoTemplate      mongo template
     */
    private void processorIndexes(@NotNull MongoProperties mongoProperties,
                                  ConfigurableApplicationContext applicationContext,
                                  MongoMappingContext context,
                                  String datasourceName,
                                  MongoTemplate mongoTemplate) {
        // 4. 使用自定义的索引处理器, 并注册到 IoC 中, 这样才能让创建索引的监听器失效
        if (mongoProperties.isEnableAutoIncrementKey()) {
            try {

                EarlySpringContext.registerBean(applicationContext,
                                                datasourceName + MongoPersistentEntityIndexCreator.class.getSimpleName(),
                                                new CustomMongoPersistentEntityIndexCreator(context, mongoTemplate));
            } catch (Exception ex) {
                if (ex instanceof DataIntegrityViolationException) {
                    log.warn(ex.getMessage());
                } else {
                    log.error("", ex);
                }
            }
        }
    }

    /**
     * 使用默认数据源管理实务
     *
     * @param mongoTemplate mongo template
     * @return the mongo transaction manager
     */
    @NotNull
    @Contract("_ -> new")
    private MongoTransactionManager transactionManager(@NotNull MongoTemplate mongoTemplate) {
        return new MongoTransactionManager(mongoTemplate.getMongoDbFactory());
    }

    /**
     * Create mongo db factory simple mongo db factory
     *
     * @param dataSourceUri data source uri
     * @return the simple mongo db factory
     */
    @Contract("_ -> new")
    @NotNull
    private static SimpleMongoClientDatabaseFactory createMongoDbFactory(String dataSourceUri) {
        return new SimpleMongoClientDatabaseFactory(dataSourceUri);
    }

    /**
     * 配置转换器
     *
     * @param factory         factory
     * @param context         context
     * @param conversions     conversions
     * @param mongoProperties mongo properties
     * @return the mapping mongo converter
     */
    private @NotNull MappingMongoConverter buildMappingMongoConverter(SimpleMongoClientDatabaseFactory factory,
                                                                      MongoMappingContext context,
                                                                      MongoCustomConversions conversions,
                                                                      @NotNull MongoProperties mongoProperties) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(conversions);

        if (!mongoProperties.isEnableSaveClassName()) {
            // 保存 _class
            mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        }
        // 因为不是作为一个 bean, 这里手动调用进行初始化
        mappingConverter.afterPropertiesSet();
        return mappingConverter;
    }


    /**
     * Create mongo template mongo template
     *
     * @param mongoDbFactory mongo db factory
     * @param mongoConverter mongo converter
     * @return the mongo template
     */
    @Contract("_, _ -> new")
    @NotNull
    private static MongoTemplate createMongoTemplate(SimpleMongoClientDatabaseFactory mongoDbFactory,
                                                     @Nullable MongoConverter mongoConverter) {
        return new MongoTemplate(mongoDbFactory, mongoConverter);
    }

    /**
     * 扫描实体, 设置 className 和 mongoTemplate 之间的关系.
     * 如果没有使用 @EnableEntityScanner 或使用了但是没有配置 basePackages 等属性, 则默认扫描启动类所在包及子包;
     * 如果配置了 yanger.mongo.scan-path 和 @EnableEntityScanner, 将优先使用 yanger.mongo.scan-path;
     *
     * @param scanPath           scan path
     * @param applicationContext application context
     */
    @SneakyThrows
    private void scanEntity(List<String> scanPath, ApplicationContext applicationContext) {
        EntityScanner entityScanner = new EntityScanner(applicationContext);

        if (CollectionUtils.isEmpty(scanPath)) {
            scanPath = entityScanner.getScannerPackages();
            if (CollectionUtils.isEmpty(scanPath)) {
                log.warn("未配置 yanger.mongo.scan-path, "
                         + "将使用 {} 作为默认 root package 进行扫描, "
                         + "为加快扫描速度, 请使用 @EnableEntityScanner 或者外部化配置 Mongodb Entity 包名!",
                         scanPath);
            }

        } else {
            entityScanner.setScannerPackages(scanPath);
        }

        // 扫描指定的注解
        Set<Class<?>> classesWithAnnotation = entityScanner.scan(MongoCollection.class);

        if (classesWithAnnotation.size() == 0) {
            log.warn("{} 包下未找到被 @MongoCollection 标识的实体, 不能通过 MongoDataSource.getDataSource(Class claz) 获取数据源", scanPath);
            return;
        }

        classesWithAnnotation.forEach(c -> {
            try {
                MongoCollection mongoCollection = AnnotationUtils.findAnnotation(c, MongoCollection.class);
                if (mongoCollection != null) {
                    MongoProviderFactory.addAnnotation2BeanMap(c.getName(), build(c, mongoCollection));
                }
            } catch (Exception e) {
                throw new MongoException(e.getMessage());
            }
        });
    }

    /**
     * 构建 MongoCollection 注解映射的实体, 如果 datasource 未设置, 则使用默认数据源
     * 如果 collectionName 为配置, 将使用类名(驼峰转下划线) 命名
     *
     * @param clazz           clazz
     * @param mongoCollection the mongo collection
     * @return the mongo bean
     */
    private static @NotNull MongoBean build(Class<?> clazz, @NotNull MongoCollection mongoCollection) {

        String collectionName = mongoCollection.value();
        if (StringUtils.isBlank(collectionName)) {
            collectionName = StringTools.camelCaseToUnderline(clazz.getSimpleName());
        }
        // 处理数据源
        String datasource = mongoCollection.datasource();
        String desc = mongoCollection.desc();

        MongoBean mongoBean = new MongoBean();
        mongoBean.setCollectionName(collectionName);
        mongoBean.setDesc(desc);

        if (StringUtils.isBlank(datasource)) {
            mongoBean.setMongoTemplate(MongoProviderFactory.getConfigureWithMongoTemplate(MongoProperties.DEFAULT_DATASOURCE));
        } else {
            mongoBean.setMongoTemplate(MongoProviderFactory.getConfigureWithMongoTemplate(datasource));
        }

        return mongoBean;
    }

}
