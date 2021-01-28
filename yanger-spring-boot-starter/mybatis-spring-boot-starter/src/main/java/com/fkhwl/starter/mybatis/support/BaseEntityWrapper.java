package com.fkhwl.starter.mybatis.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 视图包装基类 </p>
 *
 * @param <E> parameter
 * @param <V> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 18:21
 * @since 1.0.0
 */
@Deprecated
public abstract class BaseEntityWrapper<E, V> {

    /**
     * 分页实体类集合包装
     *
     * @param pages pages
     * @return page page
     * @since 1.0.0
     */
    public IPage<V> pageVO(@NotNull IPage<E> pages) {
        List<V> records = listVO(pages.getRecords());
        IPage<V> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(records);
        return pageVo;
    }

    /**
     * 实体类集合包装
     *
     * @param list list
     * @return list list
     * @since 1.0.0
     */
    public List<V> listVO(@NotNull List<E> list) {
        return list.stream().map(this::entityVO).collect(Collectors.toList());
    }

    /**
     * 单个实体类包装
     *
     * @param entity entity
     * @return v v
     * @since 1.0.0
     */
    public abstract V entityVO(E entity);

}
