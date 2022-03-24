package com.yanger.starter.mongo.util;

import com.yanger.starter.mongo.convert.CustomMongoMappingContext;
import com.yanger.starter.mongo.enums.FieldConvert;
import com.yanger.tools.general.tools.StringTools;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * 字段名处理工具
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@Slf4j
@UtilityClass
public class FieldConvertUtils {

    /**
     * 字段转换
     * @param fields the fields
     * @return the string [ ]
     */
    @NotNull
    public static String[] convert(@NotNull String[] fields) {
        return Arrays.stream(fields).map(FieldConvertUtils::convert).toArray(String[]::new);
    }

    /**
     * Field convert string.
     * @param field the field
     * @return the string
     */
    public static String convert(String field) {
        return CustomMongoMappingContext.getFieldConvert() == FieldConvert.UNDERSCORE
               ? StringTools.camelCaseToUnderline(field)
               : field;
    }

}
