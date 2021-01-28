package com.fkhwl.starter.mybatis.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fkhwl.starter.basic.constant.ConfigKey;
import com.fkhwl.starter.common.base.BaseQuery;
import com.fkhwl.starter.common.util.ConfigKit;
import com.fkhwl.starter.core.util.CollectionUtils;
import com.fkhwl.starter.core.util.Tools;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 分页工具 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.24 15:33
 * @since 1.0.0
 */
public class Condition {

    /**
     * Gets page *
     *
     * @param <T>   parameter
     * @param query query
     * @return the page
     * @since 1.6.0
     */
    @NotNull
    @Deprecated
    public static <T> IPage<T> getPage(@NotNull Query<?> query) {
        return getPage((BaseQuery<?>) query);
    }

    /**
     * 转化成mybatis plus中的Page
     *
     * @param <T>   the type parameter
     * @param query the query
     * @return page page
     * @since 1.0.0
     */
    @NotNull
    public static <T> IPage<T> getPage(@NotNull BaseQuery<?> query) {
        Page<T> page = new Page<>(
            Tools.toLong(query.getPage(), ConfigKit.getLongValue(ConfigKey.MybatisConfigKey.PAGE, 1L)),
            Tools.toLong(query.getLimit(), ConfigKit.getLongValue(ConfigKey.MybatisConfigKey.LIMIT, 10L)),
            query.getStartTime(),
            query.getEndTime());

        Arrays.stream(Tools.toStrArray(SqlKeyword.filter(query.getAscs()))).forEach(o -> page.addOrder(OrderItem.asc(o)));
        Arrays.stream(Tools.toStrArray(SqlKeyword.filter(query.getDescs()))).forEach(o -> page.addOrder(OrderItem.desc(o)));
        return page;
    }

    /**
     * Gets page *
     *
     * @param <T>    parameter
     * @param params params
     * @return the page
     * @since 1.0.0
     * @deprecated 请使用 {@link Condition#getPage(com.fkhwl.starter.common.base.BaseQuery)}, 将在 1.7.0 删除
     */
    @NotNull
    @Deprecated
    public static <T> IPage<T> getPage(Map<String, Object> params) {
        Query<?> query = new Query<>();
        query.setPage(CollectionUtils.findValueOfType(params, Query.PAGE, Integer.class));
        params.remove(Query.PAGE);
        query.setLimit(CollectionUtils.findValueOfType(params, Query.LIMIT, Integer.class));
        params.remove(Query.LIMIT);
        query.setAscs(CollectionUtils.findValueOfType(params, Query.ASCS, String.class));
        params.remove(Query.ASCS);
        query.setDescs(CollectionUtils.findValueOfType(params, Query.DESCS, String.class));
        params.remove(Query.DESCS);
        query.setStartTime(CollectionUtils.findValueOfType(params, Query.START_TIME, Date.class));
        params.remove(Query.START_TIME);
        query.setEndTime(CollectionUtils.findValueOfType(params, Query.END_TIME, Date.class));
        params.remove(Query.END_TIME);
        return getPage(query);
    }

    /**
     * 获取 mybatis plus 中的 QueryWrapper
     *
     * @param <T>    the type parameter
     * @param entity the entity
     * @return query wrapper
     * @since 1.0.0
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
     * @since 1.0.0
     */
    public static <T> @NotNull QueryWrapper<T> getQueryWrapper(@NotNull Map<String, Object> query, Class<T> clazz) {
        query.remove("page");
        query.remove("limit");
        query.remove("descs");
        query.remove("ascs");
        QueryWrapper<T> qw = new QueryWrapper<>();
        qw.setEntity(Tools.newInstance(clazz));
        SqlKeyword.buildCondition(query, qw);
        return qw;
    }

}
