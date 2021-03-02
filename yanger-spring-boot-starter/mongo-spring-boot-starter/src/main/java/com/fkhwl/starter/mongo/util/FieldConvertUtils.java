package com.fkhwl.starter.mongo.util;

import com.fkhwl.starter.mongo.convert.CustomMongoMappingContext;
import com.fkhwl.starter.mongo.enums.FieldConvert;
import com.yanger.tools.general.tools.StringTools;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 字段名处理工具</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.03 11:50
 * @since 1.0.0
 */

/**
 * @Description
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
@UtilityClass
public class FieldConvertUtils {

    /**
     * 字段转换
     *
     * @param fields the fields
     * @return the string [ ]
     */
    @NotNull
    public static String[] convert(@NotNull String[] fields) {
        return Arrays.stream(fields).map(FieldConvertUtils::convert).toArray(String[]::new);
    }

    /**
     * Field convert string.
     *
     * @param field the field
     * @return the string
     */
    public static String convert(String field) {
        return CustomMongoMappingContext.getFieldConvert() == FieldConvert.UNDERSCORE
               ? StringTools.camelCaseToUnderline(field)
               : field;
    }

}
