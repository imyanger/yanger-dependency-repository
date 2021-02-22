package com.fkhwl.starter.mongo.convert;

import com.fkhwl.starter.core.util.DateUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDateTime;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: MongoDB -> Java</p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 10:20
 * @since 1.0.0
 */
@ReadingConverter
public class StringToDateConverter implements Converter<String, LocalDateTime> {
    /**
     * Convert local date time
     *
     * @param source source
     * @return the local date time
     * @since 1.0.0
     */
    @Override
    public LocalDateTime convert(@NotNull String source) {
        return LocalDateTime.parse(source, DateUtils.DATETIME_FORMATTER);
    }
}
