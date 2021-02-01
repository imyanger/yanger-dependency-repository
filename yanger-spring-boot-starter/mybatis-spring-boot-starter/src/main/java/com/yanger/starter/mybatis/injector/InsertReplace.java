package com.yanger.starter.mybatis.injector;

/**
 * @Description 插入一条数据（选择字段插入)
 *     表示插入替换数据,需求表中有PrimaryKey,或者unique索引,如果数据库已经存在数据,则用新数据替换,如果没有数据效果则和 insert into一样;
 * @Author yanger
 * @Date 2021/1/28 18:06
 */
public class InsertReplace extends AbstractInsertMethod {

    /**
     * Replace
     */
    public InsertReplace() {
        super(MybatisSqlMethod.REPLACE_ONE);
    }

}
