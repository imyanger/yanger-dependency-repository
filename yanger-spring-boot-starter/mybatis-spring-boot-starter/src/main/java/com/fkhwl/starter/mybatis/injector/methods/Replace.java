package com.fkhwl.starter.mybatis.injector.methods;

import com.fkhwl.starter.mybatis.injector.MybatisSqlMethod;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 插入一条数据（选择字段插入)</p>
 * 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样;
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 14:53
 * @since 1.0.0
 */
public class Replace extends AbstractInsertMethod {

    /**
     * Replace
     *
     * @since 1.0.0
     */
    public Replace() {
        super(MybatisSqlMethod.REPLACE_ONE);
    }
}

