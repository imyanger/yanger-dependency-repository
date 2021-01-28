package com.yanger.starter.mybatis.injector;

/**
 * @Description 插入一条数据（选择字段插入）插入如果中已经存在相同的记录,则忽略当前新数据
 * @Author yanger
 * @Date 2021/1/28 18:05
 */
public class InsertIgnore extends AbstractInsertMethod {

    /**
     * Insert ignore
     */
    public InsertIgnore() {
        super(MybatisSqlMethod.INSERT_IGNORE_ONE);
    }

}
