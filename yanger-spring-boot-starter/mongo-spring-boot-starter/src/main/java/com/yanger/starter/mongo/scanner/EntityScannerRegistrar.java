package com.yanger.starter.mongo.scanner;

import com.yanger.starter.mongo.annotation.EnableEntityScanner;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * {@link ImportBeanDefinitionRegistrar} to store the base package from the importing configuration
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class EntityScannerRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * Register bean definitions *
     *
     * @param metadata metadata
     * @param registry registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        EntityScanPackages.register(registry, this.getPackagesToScan(metadata));
    }

    /**
     * Gets packages to scan *
     *
     * @param metadata metadata
     * @return the packages to scan
     */
    private @NotNull Set<String> getPackagesToScan(@NotNull AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes
            .fromMap(metadata.getAnnotationAttributes(EnableEntityScanner.class.getName()));

        String[] basePackages = Objects.requireNonNull(attributes).getStringArray("basePackages");
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        Set<String> packagesToScan = new LinkedHashSet<>(Arrays.asList(basePackages));
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        if (packagesToScan.isEmpty()) {
            String packageName = ClassUtils.getPackageName(metadata.getClassName());
            Assert.state(!StringUtils.isEmpty(packageName), "@EntityScan cannot be used with the default package");
            return Collections.singleton(packageName);
        }
        return packagesToScan;
    }

}
