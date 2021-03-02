package com.fkhwl.starter.mongo.convert;

import com.fkhwl.starter.mongo.enums.EnableEnum;

import org.springframework.data.convert.WritingConverter;

/**
 * @Description 枚举转成 boolean 存储
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@WritingConverter
public class EnumToBooleanConverter extends AbstractEnumToSerializableConverter<EnableEnum> {

}
