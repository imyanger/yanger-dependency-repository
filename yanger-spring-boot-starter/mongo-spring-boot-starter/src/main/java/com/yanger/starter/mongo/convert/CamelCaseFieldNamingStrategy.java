package com.yanger.starter.mongo.convert;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.data.mapping.model.CamelCaseSplittingFieldNamingStrategy;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;
import org.springframework.data.util.ParsingUtils;

import java.util.Iterator;
import java.util.List;

/**
 * 自定义策略, 目的: myName -> MY_NAME, 其他自带的策略:
 *     {@link CamelCaseAbbreviatingFieldNamingStrategy}: 首字母大写
 *     {@link CamelCaseSplittingFieldNamingStrategy}: 分隔驼峰命名, 传入自定义分隔符
 *     {@link SnakeCaseFieldNamingStrategy}: 下划线风格
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class CamelCaseFieldNamingStrategy implements FieldNamingStrategy {

    /**
     * Gets field name *
     * @param property property
     * @return the field name
     */
    @NotNull
    @Override
    public String getFieldName(@NotNull PersistentProperty<?> property) {
        List<String> parts = ParsingUtils.splitCamelCaseToLower(property.getName());
        StringBuilder sb = new StringBuilder();
        Iterator it = parts.iterator();
        if (it.hasNext()) {
            sb.append(it.next().toString().toUpperCase());
            while (it.hasNext()) {
                sb.append("_");
                sb.append(it.next().toString().toUpperCase());
            }
        }
        return sb.toString();
    }

}
