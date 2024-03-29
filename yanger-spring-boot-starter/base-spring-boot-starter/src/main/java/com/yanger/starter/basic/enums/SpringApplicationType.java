package com.yanger.starter.basic.enums;

import com.yanger.starter.basic.constant.FullyQualifiedName;
import org.springframework.util.ClassUtils;

/**
 * spring 应用运行类型
 * @Author yanger
 * @Date 2020/12/29 16:12
 */
public enum SpringApplicationType {

    /** Boot spring application type */
    BOOT,

    /** Cloud spring application type */
    CLOUD;

    /**
     * 获取 spring 应用运行类型
     * @return the web application type
     */
    public static SpringApplicationType deduceFromClasspath() {
        if (ClassUtils.isPresent(FullyQualifiedName.CLOUD_CLASS, null)) {
            return SpringApplicationType.CLOUD;
        }
        if (ClassUtils.isPresent(FullyQualifiedName.BOOT_CLASS, null)) {
            return SpringApplicationType.BOOT;
        }
        throw new IllegalStateException("不是 Spring Boot/Cloud 应用.");
    }

}
