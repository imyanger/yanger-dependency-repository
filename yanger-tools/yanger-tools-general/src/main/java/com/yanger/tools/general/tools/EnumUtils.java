package com.yanger.tools.general.tools;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @Description 枚举工具类
 * @Author yanger
 * @Date 2020/8/27 19:14
 */
public class EnumUtils {

    private static Map<Class, Object> map = new ConcurrentHashMap<>();

    /**
     * 根据条件获取枚举对象
     *
     * @param className 枚举类
     * @param predicate 筛选条件
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getEnumObject(Class<T> className, Predicate<T> predicate) {
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
     *
     * @param <T>     the type parameter
     * @param clazz   the clazz
     * @param ordinal the ordinal   需要的枚举值在设定的枚举类中的顺序, 以 0 开始
     * @return t t
     * @author xiehao
     */
    public static <T extends Enum<T>> T indexOf(@NotNull Class<T> clazz, int ordinal) {
        return clazz.getEnumConstants()[ordinal];
    }

    /**
     * 传入的参数 name 指的是枚举值的名称, 一般是大写加下划线的
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @param name  the name
     * @return Enum T
     * @author xiehao
     */
    @NotNull
    public static <T extends Enum<T>> T nameOf(Class<T> clazz, String name) {
        return Enum.valueOf(clazz, name);
    }

}
