package com.yanger.starter.basic.convert;

import com.yanger.starter.basic.constant.ConfigDefaultValue;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.tools.general.tools.DateUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * @Description String 转 Date 的转化器, 适用于 controller 基础字段转换 (非实体), 实体由 Jackson 进行转换
 * @Author yanger
 * @Date 2021/1/27 17:11
 */
public class StringToDateConverter implements Converter<String, Date> {

    /** PATTERN */
    public static final String PATTERN = ConfigDefaultValue.JsonConfigValue.JSON_DATE_FORMAT;

    /**
     * Convert date
     *
     * @param source source
     * @return the date
     */
    @Override
    public Date convert(@NotNull String source) {
        return DateUtils.parse(source, System.getProperty(ConfigKey.JsonConfigKey.JSON_DATE_FORMAT, PATTERN));
    }
}
