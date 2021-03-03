package com.yanger.starter.mongo.convert;

import com.yanger.tools.general.tools.DateUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDateTime;

/**
 * @Description Java -> MongoDB
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@WritingConverter
public class DateToStringConverter implements Converter<LocalDateTime, String> {

    /**
     * Convert string
     *
     * @param source source
     * @return the string
     */
    @Override
    public String convert(@NotNull LocalDateTime source) {
        return DateUtils.formatDateTime(source);
    }

}


