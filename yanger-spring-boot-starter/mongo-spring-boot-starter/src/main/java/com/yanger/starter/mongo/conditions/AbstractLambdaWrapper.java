package com.yanger.starter.mongo.conditions;

import com.yanger.starter.mongo.mapper.Model;
import com.yanger.starter.mongo.support.SFunction;

/**
 * 统一处理解析 lambda 获取 column
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
@SuppressWarnings("serial")
public abstract class AbstractLambdaWrapper<M extends Model<M>, Children extends AbstractLambdaWrapper<M, Children>>
    extends AbstractWrapper<M, SFunction<M, ?>, Children> {

}
