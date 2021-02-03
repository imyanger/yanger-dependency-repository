package com.yanger.starter.mybatis.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.starter.basic.entity.BaseQuery;
import com.yanger.starter.basic.util.ConfigKit;
import com.yanger.tools.general.tools.StringTools;
import com.yanger.tools.web.tools.BeanUtils;
import com.yanger.tools.web.tools.NumberUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

/**
 * @Description 分页工具
 * @Author yanger
 * @Date 2021/1/29 9:40
 */
public class Condition {

    /**
     * 转化成mybatis plus中的Page
     *
     * @param <T>   the type parameter
     * @param query the query
     * @return page page
     */
    @NotNull
    public static <T> IPage<T> getPage(@NotNull BaseQuery<?> query) {
        Page<T> page = new Page<T>(
            NumberUtils.toLong(String.valueOf(query.getPage()), ConfigKit.getLongValue(ConfigKey.MybatisConfigKey.PAGE, 1L)),
            NumberUtils.toLong(String.valueOf(query.getLimit()), ConfigKit.getLongValue(ConfigKey.MybatisConfigKey.LIMIT, 10L)),
            query.getStartTime(),
            query.getEndTime());

        Arrays.stream(StringTools.toStrArray(",", SqlKeyword.filter(query.getAscs()))).forEach(o -> page.addOrder(OrderItem.asc(o)));
        Arrays.stream(StringTools.toStrArray(",", SqlKeyword.filter(query.getDescs()))).forEach(o -> page.addOrder(OrderItem.desc(o)));
        return page;
    }

    /**
     * 获取 mybatis plus 中的 QueryWrapper
     *
     * @param <T>    the type parameter
     * @param entity the entity
     * @return query wrapper
     */
    @NotNull
    @Contract("_ -> new")
    public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
        return new QueryWrapper<>(entity);
    }

    /**
     * 获取 mybatis plus中的 QueryWrapper
     *
     * @param <T>   the type parameter
     * @param query the query
     * @param clazz the clazz
     * @return query wrapper
     */
    public static <T> @NotNull QueryWrapper<T> getQueryWrapper(@NotNull Map<String, Object> query, Class<T> clazz) {
        query.remove("page");
        query.remove("limit");
        query.remove("descs");
        query.remove("ascs");
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(BeanUtils.newInstance(clazz));
        SqlKeyword.buildCondition(query, qw);
        return qw;
    }

}
