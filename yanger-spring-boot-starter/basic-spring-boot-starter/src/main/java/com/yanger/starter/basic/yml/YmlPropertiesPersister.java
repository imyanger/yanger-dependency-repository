package com.yanger.starter.basic.yml;

import com.google.common.collect.Maps;

import com.yanger.starter.basic.constant.ConfigKey;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 18:33
 */
@Slf4j
public class YmlPropertiesPersister extends DefaultPropertiesPersister {

    /**
     * Load *
     *
     * @param props props
     * @param is    is
     */
    @Override
    public void load(@NotNull Properties props, @NotNull InputStream is) {
        List<PropertySource<?>> sources = null;
        try {
            sources = new YamlPropertySourceLoader().load(props.getProperty(ConfigKey.CONFIG_NAME), new InputStreamResource(is));
        } catch (IOException e) {
            log.error("", e);
        }
        if (sources == null || sources.isEmpty()) {
            return;
        }
        // yml 数据存储,合成一个 PropertySource
        Map<String, Object> ymlDataMap = Maps.newHashMapWithExpectedSize(32);
        for (PropertySource<?> source : sources) {
            ymlDataMap.putAll(((MapPropertySource) source).getSource());
        }
        props.putAll(ymlDataMap);
    }

}
