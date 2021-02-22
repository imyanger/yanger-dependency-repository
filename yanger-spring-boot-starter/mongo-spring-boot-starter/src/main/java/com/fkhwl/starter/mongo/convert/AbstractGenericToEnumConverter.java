package com.fkhwl.starter.mongo.convert;

import com.google.common.collect.Maps;

import com.fkhwl.starter.common.enums.SerializeEnum;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.io.*;
import java.util.Map;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 将 mongodb 的数据转换为枚举 </p>
 *
 * @param <E> parameter
 * @param <V> parameter
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.04 18:38
 * @since 1.0.0
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
     * @since 1.0.0
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
     * @since 1.0.0
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
