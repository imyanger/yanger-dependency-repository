package com.yanger.starter.mybatis.injector;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 扩展的自定义方法
 * @Author yanger
 * @Date 2021/1/28 18:07
 */
@Getter
@AllArgsConstructor
public enum MybatisSqlMethod {

    /** 插入如果中已经存在相同的记录,则忽略当前新数据 */
    INSERT_IGNORE_ONE("insertIgnore", "插入一条数据（选择字段插入）", "<script>\nINSERT IGNORE INTO %s %s VALUES %s\n</script>"),

    /** 表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和insert into一样 */
    REPLACE_ONE("replace", "插入一条数据（选择字段插入）", "<script>\nREPLACE INTO %s %s VALUES %s\n</script>"),

    /** 插入数据，使用自定义id（即使是自增id的情况） */
    INSERT_USE_ID("insertUseId", "插入数据，使用自定义id（即使是自增id的情况）", "<script>\nINSERT INTO %s %s VALUES %s\n</script>");

    /** Method */
    private final String method;

    /** Desc */
    private final String desc;

    /** Sql */
    private final String sql;

}
