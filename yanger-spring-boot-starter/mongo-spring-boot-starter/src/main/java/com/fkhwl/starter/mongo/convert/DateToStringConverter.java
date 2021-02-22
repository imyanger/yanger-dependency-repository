package com.fkhwl.starter.mongo.convert;

import com.fkhwl.starter.core.util.DateUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.LocalDateTime;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: Java -> MongoDB </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 10:20
 * @since 1.0.0
 */
@WritingConverter
public class DateToStringConverter implements Converter<LocalDateTime, String> {
    /**
     * Convert string
     *
     * @param source source
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String convert(@NotNull LocalDateTime source) {
        return DateUtils.formatDateTime(source);
    }
}


