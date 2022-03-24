package com.yanger.starter.web.context;

import com.yanger.starter.web.entity.SerializeEnumData;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 装载所有 SerializeEnum 接口实现类的枚举数据
 * map 的 key 默认为 SerializeEnum 实现类 类名，如果使用注解 @EnumName, 则使用 EnumName 的 value值
 * map 的 value 为枚举数据集合
 * @Author yanger
 * @Date 2022/3/22/022 21:47
 */
public class SerializeEnumContainer {

    private static final Map<String, List<SerializeEnumData>> ENUM_DATA_MAP = new HashMap<>();

    /**
     * Context
     * @return the thread context map
     * @Author yanger
     */
    @Contract(pure = true)
    public static Map<String, List<SerializeEnumData>> context() {
        return ENUM_DATA_MAP;
    }

    /**
     * ata into enum data map
     * @param enumName 枚举名称，默认为 SerializeEnum 实现类 类名，如果使用注解 @EnumName, 则使用 EnumName 的 value值
     * @param serializeEnumDataList 枚举数据
     * @return
     * @Author yanger
     * @Date 2022/03/22 22:05
     */
    @Contract(pure = true)
    public static void put(String enumName, List<SerializeEnumData> serializeEnumDataList) {
        ENUM_DATA_MAP.put(enumName, serializeEnumDataList);
    }

    /**
     * et enum data from enum data map
     * @param enumName 枚举名称，默认为 SerializeEnum 实现类 类名，如果使用注解 @EnumName, 则使用 EnumName 的 value值
     * @return {@link List< SerializeEnumData>}
     * @Author yanger
     * @Date 2022/03/22 22:03
     */
    @Contract(pure = true)
    public static List<SerializeEnumData> get(String enumName) {
        return ENUM_DATA_MAP.get(enumName);
    }

    /**
     * Clear enum data map
     * @Author yanger
     */
    @Contract(pure = true)
    public static void clear() {
        ENUM_DATA_MAP.clear();
    }

}
