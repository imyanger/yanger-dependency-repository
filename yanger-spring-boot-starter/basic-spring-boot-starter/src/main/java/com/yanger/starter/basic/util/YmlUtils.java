package com.yanger.starter.basic.util;

import com.yanger.starter.basic.constant.App;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * yaml文件解析
 * @Author yanger
 * @Date 2021/1/20 16:39
 */
@Slf4j
public class YmlUtils {

    public static Map<String, Object> getYaml(String filePath) {
        Yaml yml = new Yaml();
        Reader reader = null;
        try {
            // String filePath = ResourceUtils.getURL("classpath:").getPath() + "/application.yml";
            reader = new FileReader(new File(filePath));
        } catch (Exception e) {
            log.error("解析yaml文件异常", e);
        }
        return yml.loadAs(reader, Map.class);
    }

    public static Object getPropValue(Map<String, Object> map, String key) {
        Map<String, Object> temp = map;
        int length = key.split("\\.").length;
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                //叶子节点直接获取
                return temp.get(key.split("\\.")[i]);
            } else {
                temp = (Map<String, Object>) temp.get(key.split("\\.")[i]);
            }
        }
        return null;
    }

    public static Map<String, Object> getYamlProperties(@NotNull String yamlName) {
        Map<String, Object> propValue = new HashMap<>();
        String yamlPath = getYamlPath(yamlName);
        if (yamlPath == null) {
            return null;
        }
        Map<String, Object> map = getYaml(yamlPath);
        if(map != null) {
            getValue(propValue, "", map);
        }
        return propValue;
    }

    /**
     * 获取yaml文件路径
     *
     * @throws
     * @Date 2021/1/22 17:55
     * @param: yamlName
     * @return: java.lang.String
     */
    public static String getYamlPath(@NotNull String yamlName) {
        String configPath = ConfigKit.getConfigPath();
        String propertiesPath = configPath + yamlName;
        File file = new File(propertiesPath);
        if (file.exists()) {
            return propertiesPath;
        }
        // 如果单元测试时在 test-classes 下不存在 configFileName 配置, 则查找 target/classes 下的 configFileName 配置
        if (!configPath.contains(App.Const.JUNIT_FLAG)) {
            return null;
        }
        propertiesPath = propertiesPath.replace(App.Const.JUNIT_FLAG, "classes");
        file = new File(propertiesPath);
        if (file.exists()) {
            return propertiesPath;
        }
        return null;
    }

    private static void getValue(Map<String, Object> propValue, String pre, Map<String, Object> map) {
        map.entrySet().forEach(s -> {
            if (s.getValue() instanceof Map) {
                getValue(propValue, StringUtils.isNoneBlank(pre) ? pre + "." + s.getKey() : s.getKey(), (Map<String, Object>) s.getValue());
            } else {
                propValue.put(StringUtils.isNoneBlank(pre) ? pre + "." + s.getKey() : s.getKey(), s.getValue());
            }
        });
    }

}
