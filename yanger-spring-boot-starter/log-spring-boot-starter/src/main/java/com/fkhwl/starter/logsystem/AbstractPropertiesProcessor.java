package com.fkhwl.starter.logsystem;

import com.fkhwl.starter.core.util.StringUtils;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 抽象的参数处理器, 将配置文件中的配置(Environment) 写入到 JVM, logsystem 才能读取. </p>
 *
 * @author dong4j
 * @version 1.4.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.05.20 11:41
 * @since 1.4.0
 */
public abstract class AbstractPropertiesProcessor {

    /** Environment */
    protected final ConfigurableEnvironment environment;
    /** Resolver */
    protected final PropertyResolver resolver;

    /**
     * Abstract properties processor
     *
     * @param environment environment
     * @since 1.4.0
     */
    protected AbstractPropertiesProcessor(ConfigurableEnvironment environment) {
        Assert.notNull(environment, "Environment must not be null");
        this.environment = environment;
        this.resolver = this.getPropertyResolver();
    }

    /**
     * Gets property resolver *
     *
     * @return the property resolver
     * @since 1.0.0
     */
    protected PropertyResolver getPropertyResolver() {
        PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(this.environment.getPropertySources());
        resolver.setIgnoreUnresolvableNestedPlaceholders(true);
        return resolver;
    }

    /**
     * Apply
     *
     * @since 1.4.0
     */
    public abstract void apply();

    /**
     * Gets log file property *
     *
     * @param propertyName           property 配置名
     * @param deprecatedPropertyName deprecated 配置别名
     * @param defaultValue           default 默认值
     * @return the log file property
     * @since 1.0.0
     */
    public String getProperty(String propertyName,
                              String deprecatedPropertyName,
                              String defaultValue) {
        String property = this.resolver.getProperty(propertyName);
        if (StringUtils.isNotBlank(property)) {
            return property;
        }

        if (StringUtils.isNotBlank(deprecatedPropertyName)) {
            property = this.resolver.getProperty(deprecatedPropertyName);
        }

        return StringUtils.isBlank(property) ? defaultValue : property;
    }

    /**
     * Sets system property *
     *
     * @param value value
     * @param names names
     * @since 1.0.0
     */
    protected void setSystemProperty(String value, String... names) {
        Arrays.stream(names).forEach(name -> {
            if (System.getProperty(name) == null && value != null) {
                System.setProperty(name, value);
            }
        });
    }
}
