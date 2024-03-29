package com.yanger.starter.mongo.convert;

import com.yanger.tools.general.constant.DateConstant;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MongoDB -> Java
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@ReadingConverter
public class StringToDateConverter implements Converter<String, LocalDateTime> {

    /**
     * Convert local date time
     * @param source source
     * @return the local date time
     */
    @Override
    public LocalDateTime convert(@NotNull String source) {
        return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DateConstant.PATTERN_DATETIME));
    }

}
