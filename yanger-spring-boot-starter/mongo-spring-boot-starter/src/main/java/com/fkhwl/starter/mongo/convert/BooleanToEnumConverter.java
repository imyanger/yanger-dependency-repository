package com.fkhwl.starter.mongo.convert;

import com.fkhwl.starter.common.enums.EnableEnum;

import org.springframework.data.convert.ReadingConverter;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 查询时将 boolean 转为枚举(EnableEnum) </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.04 18:29
 * @since 1.0.0
 */
@ReadingConverter
public class BooleanToEnumConverter extends AbstractSerializableToEnumConverter<EnableEnum> {

    /**
     * Boolean to enum converter
     *
     * @since 1.0.0
     */
    public BooleanToEnumConverter() {
        super(EnableEnum.class);
    }
}
