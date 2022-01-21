package com.yanger.tools.general.tools;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * 枚举工具类
 * @Author yanger
 * @Date 2020/8/27 19:14
 */
@Slf4j
public class EnumUtils {

    private static Map<Class, Object> map = new ConcurrentHashMap<>();

    /**
     * 根据条件获取枚举对象
     * @param className 枚举类
     * @param predicate 筛选条件
     * @return {@link Optional<T>}
     * @Author yanger
     * @Date 2021/12/08 23:42
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getEnum(Class<T> className, Predicate<T> predicate) {
        if (!className.isEnum()) {
            return Optional.empty();
        }
        Object obj = map.get(className);
        T[] ts;
        if (obj == null) {
            ts = className.getEnumConstants();
            map.put(className, ts);
        } else {
            ts = (T[]) obj;
        }
        return Arrays.stream(ts).filter(predicate).findAny();
    }

    /**
     * 通过枚举的 index 获取枚举
     * @param clazz 枚举类
     * @param ordinal 顺序
     * @return {@link T}
     * @Author yanger
     * @Date 2021/12/08 23:47
     */
    public static <T extends Enum<T>> T index(@NotNull Class<T> clazz, int ordinal) {
        return clazz.getEnumConstants()[ordinal];
    }

    /**
     * 传入的参数 name 指的是枚举值的名称, 一般是大写加下划线的
     * @param clazz 枚举类
     * @param name 枚举名
     * @return {@link T}
     * @Author yanger
     * @Date 2021/12/08 23:47
     */
    @NotNull
    public static <T extends Enum<T>> T name(Class<T> clazz, String name) {
        return Enum.valueOf(clazz, name);
    }

}
