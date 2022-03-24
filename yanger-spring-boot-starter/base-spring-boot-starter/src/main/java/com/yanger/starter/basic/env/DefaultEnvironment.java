package com.yanger.starter.basic.env;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 环境变量
 * @Author yanger
 * @Date 2020/12/29 18:55
 */
public class DefaultEnvironment extends StandardEnvironment {

    /** 默认配置 */
    public static final String DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME = "defaultProperties";

    /** 自定义配置 */
    public static final String CUSTOM_PROPERTIES_PROPERTY_SOURCE_NAME = "customProperties";

    /** Property source */
    private Map<String, Object> mapProperties;

    /** Property source */
    private Collection<PropertySource<?>> propertySources;

    public DefaultEnvironment() {
        super();
    }

    public DefaultEnvironment(Map<String, Object> mapProperties) {
        this();
        this.mapProperties = mapProperties;
    }

    public DefaultEnvironment(PropertySource<?> propertySource) {
        this(new ArrayList<>(Collections.singleton(propertySource)));
    }

    public DefaultEnvironment(Collection<PropertySource<?>> propertySources) {
        this();
        this.propertySources = propertySources;
    }

    /**
     * Customize property sources
     * @param propertySources property sources
     */
    @Override
    protected void customizePropertySources(@NotNull MutablePropertySources propertySources) {
        if (this.mapProperties != null) {
            propertySources.addLast(new DefaultEnvironmentPropertySource(DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME, this.mapProperties));
        }
        if (this.propertySources != null && propertySources.size() > 0) {
            propertySources.forEach(propertySources::addFirst);
        }
        super.customizePropertySources(propertySources);
    }

}
