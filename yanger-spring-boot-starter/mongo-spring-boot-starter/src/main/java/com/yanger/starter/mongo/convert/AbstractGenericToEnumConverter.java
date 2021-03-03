package com.yanger.starter.mongo.convert;

import com.google.common.collect.Maps;

import com.yanger.starter.basic.enums.SerializeEnum;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.io.*;
import java.util.Map;

/**
 * @Description 将 mongodb 的数据转换为枚举
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public abstract class AbstractGenericToEnumConverter<E extends Enum<?>, V extends Serializable> implements Converter<V, E> {

    /** Enum class */
    private final Class<E> enumClass;

    /** 枚举的 {@link SerializeEnum#getValue()} 与 枚举的映射关系 */
    private final Map<V, E> enumMap = Maps.newHashMap();

    /**
     * 建立映射关系 (V[value] <--> E[SerializeEnum])
     *
     * @param enumClass enum class
     */
    @SuppressWarnings("unchecked")
    @Contract(pure = true)
    public AbstractGenericToEnumConverter(@NotNull Class<E> enumClass) {
        this.enumClass = enumClass;

        // 获取当前枚举类型的所有已定义的枚举
        E[] enums = enumClass.getEnumConstants();
        if (SerializeEnum.class.isAssignableFrom(enumClass)) {
            for (E e : enums) {
                this.enumMap.put((V) ((SerializeEnum) e).getValue(), e);
            }
        }
    }

    /**
     * 枚举转换优先级:
     * 1. value
     * 2. name
     * 3. 枚举下标
     *
     * @param source source
     * @return the t
     */
    @Override
    public E convert(@NotNull V source) {
        E result = this.enumMap.get(source);
        if (result != null) {
            return result;
        }

        return SerializeEnum.getEnumByOrder(this.enumClass, source);
    }

}
