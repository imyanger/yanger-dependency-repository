package com.yanger.starter.mongo.convert;

import com.yanger.starter.mongo.enums.EnableEnum;

import org.springframework.data.convert.ReadingConverter;

/**
 * @Description 查询时将 boolean 转为枚举(EnableEnum)
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@ReadingConverter
public class BooleanToEnumConverter extends AbstractSerializableToEnumConverter<EnableEnum> {

    /**
     * Boolean to enum converter
     */
    public BooleanToEnumConverter() {
        super(EnableEnum.class);
    }
}
