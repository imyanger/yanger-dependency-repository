package com.fkhwl.starter.mybatis.listener;

import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.basic.util.StringPool;
import com.fkhwl.starter.common.FkhApplicationListener;
import com.fkhwl.starter.core.util.StringUtils;
import com.fkhwl.starter.processor.annotation.AutoListener;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 配置加载完成后检查是否存在 datasource 配置, 如果不不存在, 则排除 datasource 自动配置, 避免启动失败 </p>
 * todo-dong4j : (2020.05.22 17:31) [暂时不使用]
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.05.19 18:46
 * @since 1.0.0
 */
@Slf4j
@AutoListener
public class DatasourceInitializeListener implements FkhApplicationListener {
    /** DATASOURCEAUTOCONFIGURATION */
    public static final String DATASOURCE_AUTOCONFIGURATION = "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration";
    /** MYBATISPLUS_AUTOCONFIGURATION */
    public static final String MYBATISPLUS_AUTOCONFIGURATION = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration";
    /** DRUIDDATASOURCE_AUTOCONFIGURE */
    public static final String DRUIDDATASOURCE_AUTOCONFIGURE = "com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure";
    /** MYBATIS_AUTOCONFIGURATION */
    public static final String MYBATIS_AUTOCONFIGURATION = "com.fkhwl.starter.mybatis.autoconfigure.MybatisAutoConfiguration";
    /** Inited */
    private static boolean inited = false;

    /**
     * Gets order *
     *
     * @return the order
     * @since 1.0.0
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    /**
     * On application context initialized event
     *
     * @param event event
     * @since 1.4.0
     */
    @Override
    public void onApplicationContextInitializedEvent(@NotNull ApplicationContextInitializedEvent event) {
        if (!inited) {
            FkhApplicationListener.Runner.executeAtLast(this.key(event, this.getClass()), () -> {
                ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
                String datasourceUrl = environment.getProperty("spring.datasource.url");

                if (StringUtils.isBlank(datasourceUrl)) {
                    log.error("未检测到 JDBC 配置, 但是引入了 JDBC 相关依赖包, 将会禁用自动配置. 请根据业务处理此错误!");
                    String property = System.getProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE);

                    String value;
                    if (StringUtils.isBlank(property)) {
                        value = String.join(StringPool.COMMA,
                                            DATASOURCE_AUTOCONFIGURATION,
                                            MYBATISPLUS_AUTOCONFIGURATION,
                                            DRUIDDATASOURCE_AUTOCONFIGURE,
                                            MYBATIS_AUTOCONFIGURATION);

                    } else {
                        value = String.join(StringPool.COMMA,
                                            property,
                                            DATASOURCE_AUTOCONFIGURATION,
                                            MYBATISPLUS_AUTOCONFIGURATION,
                                            DRUIDDATASOURCE_AUTOCONFIGURE,
                                            MYBATIS_AUTOCONFIGURATION);
                    }
                    System.setProperty(ConfigKey.SpringConfigKey.AUTOCONFIGURE_EXCLUDE, value);
                }
                inited = true;
            });
        }
    }
}
