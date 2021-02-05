package com.yanger.starter.mybatis.injector;

/**
 * 忽略型 插入一条数据（选择字段插入）
 * 插入中如果已经存在相同的记录（PrimaryKey 或者 unique索引存在冲突的情况），则忽略该次插入，不会报错
 *
 * @Author yanger
 * @Date 2021/1/28 18:05
 */
public class InsertIgnore extends AbstractInsertMethod {

    public InsertIgnore() {
        super(MybatisSqlMethod.INSERT_IGNORE_ONE);
    }

}
