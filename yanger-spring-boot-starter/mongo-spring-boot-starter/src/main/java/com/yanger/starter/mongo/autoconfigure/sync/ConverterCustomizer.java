package com.yanger.starter.mongo.autoconfigure.sync;

import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@FunctionalInterface
public interface ConverterCustomizer {

    /**
     * 添加自定义转换器
     * @param list list
     */
    void customize(List<Converter<?, ?>> list);
}
