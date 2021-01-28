package com.fkhwl.starter.common.mapstruct;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fkhwl.starter.common.base.BaseDTO;
import com.fkhwl.starter.common.base.BasePO;
import com.fkhwl.starter.mybatis.support.Page;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 服务层转换包装基类 </p>
 *
 * @param <D> the type parameter    dto
 * @param <P> parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2019.12.22 20:52
 * @since 1.0.0
 * @deprecated 请使用 {@link ServiceConverter}
 */
@Deprecated
public interface ServiceConverterWrapper<D extends BaseDTO<? extends Serializable>, P extends BasePO<? extends Serializable, P>> {

    /**
     * 服务层入参必须是 dto, 在操作数据库时, 入参必须是 po, 此接口用于 dto -> po, 且只能用于服务层.
     *
     * @param dto dto
     * @return the p
     * @since 1.0.0
     */
    P po(D dto);

    /**
     * 服务层操作数据库时返回的可能是 po 或者是 dto, 此接口用于 op -> dto, 且只能用于服务层.
     *
     * @param po po
     * @return the d
     * @since 1.0.0
     */
    D dto(P po);

    /**
     * 分页实体类集合包装
     *
     * @param pages pages
     * @return page page
     * @since 1.6.0
     */
    default IPage<D> dto(@NotNull IPage<P> pages) {
        List<D> records = this.dto(pages.getRecords());
        IPage<D> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(records);
        return pageVo;
    }

    /**
     * 实体类集合包装
     *
     * @param list list
     * @return list list
     * @since 1.6.0
     */
    default List<D> dto(@NotNull List<P> list) {
        return list.stream().map(this::dto).collect(Collectors.toList());
    }

    /**
     * Po
     *
     * @param list list
     * @return the list
     * @since 1.6.0
     */
    default List<P> po(@NotNull List<D> list) {
        return list.stream().map(this::po).collect(Collectors.toList());
    }
}
