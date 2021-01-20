package com.yanger.starter.basic.util;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description yaml文件解析
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

    public static Map<String, Object> getYamlPropValue(String filePath) {
        Map<String, Object> propValue = new HashMap<>();
        Map<String, Object> map = getYaml(filePath);
        getValue(propValue, "", map);
        return propValue;
    }

    private static void getValue(Map<String, Object> propValue, String pre, Map<String, Object> map){
        map.entrySet().forEach(s -> {
            if (s.getValue() instanceof Map) {
                getValue(propValue, StringUtils.isNoneBlank(pre) ? pre + "." + s.getKey() : s.getKey(), (Map<String, Object>) s.getValue());
            } else {
                propValue.put(StringUtils.isNoneBlank(pre) ? pre + "." + s.getKey() : s.getKey(), s.getValue());
            }
        });
    }

}
