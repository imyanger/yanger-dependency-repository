package com.yanger.starter.mybatis.listener;

import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.constant.FullyQualifiedName;
import com.yanger.starter.basic.listener.BaseApplicationListener;
import com.yanger.tools.general.constant.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 配置加载完成后检查是否存在 datasource 配置, 如果不不存在, 则排除 datasource 自动配置, 避免启动失败
 * @Author yanger
 * @Date 2021/1/29 9:40
 */
@Slf4j
public class DatasourceInitializeListener implements BaseApplicationListener {

    /** Inited */
    private static boolean inited = false;

    /**
     * Gets order
     * @return the order
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * On application context initialized event
     * @param event event
     */
    @Override
    public void onApplicationContextInitializedEvent(@NotNull ApplicationContextInitializedEvent event) {
        if (!inited) {
            BaseApplicationListener.Runner.executeAtFirst(this.key(event, this.getClass()), () -> {
                ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
                String datasourceUrl = environment.getProperty("spring.datasource.url");
                boolean dynamicEnable = Boolean.parseBoolean(environment.getProperty(ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_ENABLE));
                boolean dynamicReadWriteEnable = Boolean.parseBoolean(environment.getProperty(ConfigKey.DynamicDataSourceConfigKey.DYNAMIC_READ_WRITE_ENABLE));
                if(dynamicEnable) {
                    log.info("已开启动态数据源 spring.datasource.dynamic-enable = true，请注意 spring.datasource.dynamic 多数据源信息");
                }
                if(dynamicReadWriteEnable) {
                    log.info("已开启动态读写数据源 spring.datasource.dynamic-read-write-enable = true，请注意 spring.datasource.dynamic-read && dynamic-write 读写数据源信息");
                }
                if (StringUtils.isBlank(datasourceUrl) && (!dynamicEnable || !dynamicReadWriteEnable)) {
                    log.error("未检测到 JDBC 配置，但是引入了 JDBC 相关依赖包，将会禁用 datasource 相关自动装配，如有 dao 层将注入失败，请根据业务处理此错误");
                    String property = System.getProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE);
                    String value;
                    if (StringUtils.isBlank(property)) {
                        value = String.join(StringPool.COMMA,
                                            FullyQualifiedName.DATASOURCE_AUTOCONFIGURATION,
                                            FullyQualifiedName.MYBATISPLUS_AUTOCONFIGURATION,
                                            FullyQualifiedName.DRUIDDATASOURCE_AUTOCONFIGURE,
                                            FullyQualifiedName.MYBATIS_AUTOCONFIGURATION);
                    } else {
                        value = String.join(StringPool.COMMA,
                                            property,
                                            FullyQualifiedName.DATASOURCE_AUTOCONFIGURATION,
                                            FullyQualifiedName.MYBATISPLUS_AUTOCONFIGURATION,
                                            FullyQualifiedName.DRUIDDATASOURCE_AUTOCONFIGURE,
                                            FullyQualifiedName.MYBATIS_AUTOCONFIGURATION);
                    }
                    System.setProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE, value);
                }
                inited = true;
            });
        }
    }

}
