package com.yanger.starter.mongo.handler.metadata;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 严格填充模式 model
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Data
@AllArgsConstructor
public class StrictFill {
    /** 字段名 */
    private String fieldName;
    /** 字段类型 */
    private Class<?> fieldType;
    /** 获取字段值的函数 */
    private Supplier<Object> fieldVal;

    /**
     * Of strict fill
     *
     * @param fieldName field name
     * @param fieldType field type
     * @param fieldVal  field val
     * @return the strict fill
     */
    @Contract("_, _, _ -> new")
    public static @NotNull StrictFill of(String fieldName, Class<?> fieldType, Object fieldVal) {
        return new StrictFill(fieldName, fieldType, () -> fieldVal);
    }

    /**
     * Of strict fill
     *
     * @param fieldName field name
     * @param fieldType field type
     * @param fieldVal  field val
     * @return the strict fill
     */
    @Contract("_, _, _ -> new")
    public static @NotNull StrictFill of(String fieldName, Class<?> fieldType, Supplier<Object> fieldVal) {
        return new StrictFill(fieldName, fieldType, fieldVal);
    }
}
