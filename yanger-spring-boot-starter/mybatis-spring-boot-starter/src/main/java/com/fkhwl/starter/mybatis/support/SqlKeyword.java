package com.fkhwl.starter.mybatis.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fkhwl.starter.basic.util.StringPool;
import com.fkhwl.starter.core.util.StringUtils;
import com.fkhwl.starter.core.util.Tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import lombok.experimental.UtilityClass;

/**
 * 定义常用的 sql关键字
 *
 * @author Chill
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.01 00:24
 * @since 1.0.0
 */
@UtilityClass
public class SqlKeyword {
    /** SQL_REGEX */
    private static final String SQL_REGEX =
        "'|%|--|insert|delete|update|select|count|group|union|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|sql";

    /** EQUAL */
    private static final String EQUAL = "_equal";
    /** NOT_EQUAL */
    private static final String NOT_EQUAL = "_notequal";
    /** LIKE */
    private static final String LIKE = "_like";
    /** NOT_LIKE */
    private static final String NOT_LIKE = "_notlike";
    /** GT */
    private static final String GT = "_gt";
    /** LT */
    private static final String LT = "_lt";
    /** DATE_GT */
    private static final String DATE_GT = "_dategt";
    /** DATE_EQUAL */
    private static final String DATE_EQUAL = "_dateequal";
    /** DATE_LT */
    private static final String DATE_LT = "_datelt";
    /** IS_NULL */
    private static final String IS_NULL = "_null";
    /** NOT_NULL */
    private static final String NOT_NULL = "_notnull";
    /** IGNORE */
    private static final String IGNORE = "_ignore";

    /**
     * 条件构造器, 根据后缀来构建 qw 条件, 比如 _like --> qw.like
     *
     * @param query 查询字段
     * @param qw    查询包装类
     * @since 1.0.0
     */
    public static void buildCondition(Map<String, Object> query, QueryWrapper<?> qw) {
        if (StringUtils.isEmpty(query)) {
            return;
        }
        query.forEach((k, v) -> {
            if (Tools.hasEmpty(k, v) || k.endsWith(IGNORE)) {
                return;
            }
            if (k.endsWith(LIKE)) {
                qw.like(getColumn(k, LIKE), v);
            } else if (k.endsWith(NOT_EQUAL)) {
                qw.ne(getColumn(k, NOT_EQUAL), v);
            } else if (k.endsWith(NOT_LIKE)) {
                qw.notLike(getColumn(k, NOT_LIKE), v);
            } else if (k.endsWith(GT)) {
                qw.gt(getColumn(k, GT), v);
            } else if (k.endsWith(LT)) {
                qw.lt(getColumn(k, LT), v);
            } else if (k.endsWith(DATE_GT)) {
                qw.gt(getColumn(k, DATE_GT), v);
            } else if (k.endsWith(DATE_EQUAL)) {
                qw.eq(getColumn(k, DATE_EQUAL), v);
            } else if (k.endsWith(DATE_LT)) {
                qw.lt(getColumn(k, DATE_LT), v);
            } else if (k.endsWith(IS_NULL)) {
                qw.isNull(getColumn(k, IS_NULL));
            } else if (k.endsWith(NOT_NULL)) {
                qw.isNotNull(getColumn(k, NOT_NULL));
            } else {
                qw.eq(getColumn(k, EQUAL), v);
            }
        });
    }

    /**
     * 获取数据库字段
     *
     * @param column  字段名
     * @param keyword 关键字
     * @return column column
     * @since 1.0.0
     */
    @NotNull
    private static String getColumn(String column, String keyword) {
        return StringUtils.humpToUnderline(StringUtils.removeSuffix(column, keyword));
    }

    /**
     * 把SQL关键字替换为空字符串
     *
     * @param param 关键字
     * @return string string
     * @since 1.0.0
     */
    @Contract(value = "null -> null; !null -> !null", pure = true)
    public static String filter(String param) {
        if (param == null) {
            return null;
        }
        return param.replaceAll("(?i)" + SQL_REGEX, StringPool.EMPTY);
    }

}
