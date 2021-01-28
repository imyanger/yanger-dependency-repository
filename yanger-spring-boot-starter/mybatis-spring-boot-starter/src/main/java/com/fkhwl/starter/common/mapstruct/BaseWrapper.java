package com.fkhwl.starter.common.mapstruct;

import com.fkhwl.starter.common.base.BaseDTO;
import com.fkhwl.starter.common.base.BasePO;
import com.fkhwl.starter.common.base.BaseVO;

import java.io.*;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: vo, dto, pojo 能相互访问的时候使用 (单模块) </p>
 *
 * @param <V> the type parameter
 * @param <D> the type parameter
 * @param <P> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 18:08
 * @see ServiceConverterWrapper
 * @see ViewConverterWrapper
 * @since 1.0.0
 */
@Deprecated
public interface BaseWrapper<V extends BaseVO<? extends Serializable>,
    D extends BaseDTO<? extends Serializable>,
    P extends BasePO<? extends Serializable, P>> {

    /**
     * vo 转 dto
     *
     * @param vo the vo
     * @return the d
     * @since 1.0.0
     */
    D dto(V vo);

    /**
     * dto 转 vo
     *
     * @param dto the dto
     * @return the v
     * @since 1.0.0
     */
    V vo(D dto);

    /**
     * dto 转 po
     *
     * @param dto the dto
     * @return the p
     * @since 1.0.0
     */
    P po(D dto);

    /**
     * po 转 dto
     *
     * @param po the po
     * @return the d
     * @since 1.0.0
     */
    D dto(P po);

    /**
     * To vo user vo.
     *
     * @param po the po
     * @return the user vo
     * @since 1.0.0
     */
    V vo(P po);

    /**
     * To po user.
     *
     * @param vo the vo
     * @return the user
     * @since 1.0.0
     */
    P po(V vo);
}
