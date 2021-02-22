package com.fkhwl.starter.mongo.conditions;

import com.fkhwl.starter.mongo.mapper.Model;
import com.fkhwl.starter.mongo.support.SFunction;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 统一处理解析 lambda 获取 column</p>
 *
 * @param <M>        parameter
 * @param <Children> parameter
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.03.17 18:00
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public abstract class AbstractLambdaWrapper<M extends Model<M>, Children extends AbstractLambdaWrapper<M, Children>>
    extends AbstractWrapper<M, SFunction<M, ?>, Children> {

}
