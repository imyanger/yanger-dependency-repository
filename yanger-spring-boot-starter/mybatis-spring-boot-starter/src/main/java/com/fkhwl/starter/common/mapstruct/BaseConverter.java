package com.fkhwl.starter.common.mapstruct;

import com.fkhwl.starter.common.base.BaseDTO;
import com.fkhwl.starter.common.base.BasePO;
import com.fkhwl.starter.common.base.BaseVO;

import java.io.Serializable;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.6.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2020.09.30 16:56
 * @since 1.6.0
 */
@Deprecated
public interface BaseConverter<V extends BaseVO<? extends Serializable>,
    D extends BaseDTO<? extends Serializable>,
    P extends BasePO<? extends Serializable, P>> extends BaseWrapper<V, D, P> {
}
