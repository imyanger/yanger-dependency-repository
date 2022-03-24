package com.yanger.starter.mongo.convert;

import com.yanger.starter.basic.enums.SerializeEnum;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.io.Serializable;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public abstract class AbstractEnumToGenericConverter<E extends Enum<?>, V extends Serializable> implements Converter<E, V> {

    /**
     * 如果实现了 {@link SerializeEnum} 接口, 则存储 value, 没有则存储 name()
     * @param source source
     * @return the t
     */
    @Override
    @SuppressWarnings("unchecked")
    public V convert(@NotNull E source) {
        if (SerializeEnum.class.isAssignableFrom(source.getClass())) {
            return (V) ((SerializeEnum) source).getValue();
        } else {
            return (V) source.name();
        }
    }

}
