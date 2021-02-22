package com.fkhwl.starter.mongo.convert;

import com.fkhwl.starter.common.enums.EnableEnum;

import org.springframework.data.convert.WritingConverter;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 枚举转成 boolean 存储 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.04 18:29
 * @since 1.0.0
 */
@WritingConverter
public class EnumToBooleanConverter extends AbstractEnumToSerializableConverter<EnableEnum> {

}
