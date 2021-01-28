package com.fkhwl.starter.mybatis.injector;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 扩展的自定义方法 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum MybatisSqlMethod {

    /** 插入如果中已经存在相同的记录,则忽略当前新数据 */
    INSERT_IGNORE_ONE("insertIgnore", "插入一条数据（选择字段插入）", "<script>\nINSERT IGNORE INTO %s %s VALUES %s\n</script>"),

    /** 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样 */
    REPLACE_ONE("replace", "插入一条数据（选择字段插入）", "<script>\nREPLACE INTO %s %s VALUES %s\n</script>");

    /** Method */
    private final String method;
    /** Desc */
    private final String desc;
    /** Sql */
    private final String sql;
}
