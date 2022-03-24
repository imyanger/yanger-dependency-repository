package com.yanger.starter.mongo.convert;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 将 mongodb 的数据转换为枚举
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public abstract class AbstractSerializableToEnumConverter<T extends Enum<?>> extends AbstractGenericToEnumConverter<T, Serializable> {

    /**
     * 建立映射关系 (V[value] <--> E[SerializeEnum])
     * @param enumClass enum class
     */
    public AbstractSerializableToEnumConverter(@NotNull Class<T> enumClass) {
        super(enumClass);
    }

}
