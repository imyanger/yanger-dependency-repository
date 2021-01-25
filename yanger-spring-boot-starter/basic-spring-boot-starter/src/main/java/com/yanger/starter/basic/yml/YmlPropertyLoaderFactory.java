package com.yanger.starter.basic.yml;

import com.yanger.starter.basic.constant.ConfigKey;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;
import org.springframework.util.PropertiesPersister;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description yml 文件加载工具类
 * @Author yanger
 * @Date 2020/12/29 18:31
 */
@Slf4j
public class YmlPropertyLoaderFactory extends DefaultPropertySourceFactory {

    /** Properties persister */
    private final PropertiesPersister propertiesPersister = new YmlPropertiesPersister();

    /**
     * Create property source property source
     *
     * @param fullPathFileName full path file name
     * @return the property source
     * @throws Exception exception
     */
    @NotNull
    public static PropertySource<?> createPropertySource(String fullPathFileName) throws Exception {
        return createPropertySource(getSourceName(""), fullPathFileName);
    }

    /**
     * Create property source property source
     *
     * @param name             name
     * @param fullPathFileName full path file name
     * @return the property source
     * @throws Exception exception
     */
    @NotNull
    public static PropertySource<?> createPropertySource(String name, String fullPathFileName) throws Exception {
        YmlPropertyLoaderFactory ymlPropertyLoaderFactory = new YmlPropertyLoaderFactory();
        Resource resource = getResource(fullPathFileName);
        return ymlPropertyLoaderFactory.createPropertySource(name, new EncodedResource(resource, StandardCharsets.UTF_8));
    }

    /**
     * Create property source property source
     *
     * @param resource resource
     * @return the property source
     * @throws Exception exception
     */
    @NotNull
    public static PropertySource<?> createPropertySource(Resource resource) throws Exception {
        return createPropertySource(getNameForResource(resource), resource);
    }

    /**
     * Gets name for resource *
     *
     * @param resource resource
     * @return the name for resource
     */
    private static String getNameForResource(@NotNull Resource resource) {
        String name = resource.getDescription();
        if (StringUtils.isBlank(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }
        return name;
    }

    /**
     * Create property source property source
     *
     * @param name     name
     * @param resource resource
     * @return the property source
     * @throws Exception exception
     */
    @NotNull
    public static PropertySource<?> createPropertySource(String name, Resource resource) throws Exception {
        YmlPropertyLoaderFactory ymlPropertyLoaderFactory = new YmlPropertyLoaderFactory();
        return ymlPropertyLoaderFactory.createPropertySource(name, new EncodedResource(resource, StandardCharsets.UTF_8));
    }

    /**
     * Gets resource *
     *
     * @param fullPathFileName full path file name
     * @return the resource
     * @throws Exception exception
     */
    @NotNull
    public static Resource getResource(String fullPathFileName) throws Exception {
        InputStream inputStream = new FileInputStream(new File(fullPathFileName));
        return new InputStreamResource(inputStream);
    }

    /**
     * Create property source property source
     *
     * @param name            name
     * @param encodedResource encoded resource
     * @return the property source
     * @throws IOException io exception
     */
    @NotNull
    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, @NotNull EncodedResource encodedResource) throws IOException {
        Resource resource = encodedResource.getResource();
        String fileName = resource.getFilename();
        Properties properties = new Properties();
        properties.setProperty(ConfigKey.CONFIG_NAME, name);
        this.propertiesPersister.load(properties, resource.getInputStream());
        return new OriginTrackedMapPropertySource(getSourceName(fileName, name), properties);
    }

    /**
     * Empty property source property source
     *
     * @param name name
     * @return the property source
     */
    @NotNull
    @Contract("_ -> new")
    private static PropertySource<?> emptyPropertySource(@Nullable String name) {
        return new MapPropertySource(getSourceName(name), Collections.emptyMap());
    }

    /**
     * Gets source name *
     *
     * @param names names
     * @return the source name
     */
    @Contract("_ -> !null")
    private static String getSourceName(String... names) {
        return Stream.of(names).filter(StringUtils::isNotBlank).findFirst().orElse("yangerPropertySource");
    }

}
