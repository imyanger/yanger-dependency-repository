package com.fkhwl.starter.mybatis.injector.methods;

import com.fkhwl.starter.mybatis.injector.MybatisSqlMethod;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 插入一条数据（选择字段插入）插入如果中已经存在相同的记录,则忽略当前新数据 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 */
public class InsertIgnore extends AbstractInsertMethod {

    /**
     * Insert ignore
     *
     * @since 1.0.0
     */
    public InsertIgnore() {
        super(MybatisSqlMethod.INSERT_IGNORE_ONE);
    }
}
