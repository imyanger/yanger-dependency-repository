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
 * TODO
 * @Author yanger
 * @Date 2020/12/29 18:55
 */
public class DefaultEnvironment extends StandardEnvironment {

    /** DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME */
    public static final String DEFAULT_PROPERTIES_PROPERTY_SOURCE_NAME = "defaultProperties";

    /** CUSTOM_PROPERTIES_PROPERTY_SOURCE_NAME */
    public static final String CUSTOM_PROPERTIES_PROPERTY_SOURCE_NAME = "customProperties";

    /** Property source */
    private Map<String, Object> mapProperties;

    /** Property source */
    private Collection<PropertySource<?>> propertySources;

    /**
     * Default environment
     */
    public DefaultEnvironment() {
        super();
    }

    /**
     * Default environment
     *
     * @param mapProperties map properties
     */
    public DefaultEnvironment(Map<String, Object> mapProperties) {
        this();
        this.mapProperties = mapProperties;
    }

    /**
     * Default environment
     *
     * @param propertySource property source
     */
    public DefaultEnvironment(PropertySource<?> propertySource) {
        this(new ArrayList<>(Collections.singleton(propertySource)));
    }

    /**
     * Default environment
     *
     * @param propertySources property sources
     */
    public DefaultEnvironment(Collection<PropertySource<?>> propertySources) {
        this();
        this.propertySources = propertySources;
    }

    /**
     * Customize property sources *
     *
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
