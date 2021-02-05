package com.yanger.starter.mybatis.injector;

/**
 * 插入数据，使用自定义id（即使是自增id的情况）
 *
 * @Author yanger
 * @Date 2021/1/28 18:05
 */
public class InsertUseId extends AbstractInsertMethod {

    public InsertUseId() {
        super(MybatisSqlMethod.INSERT_USE_ID);
    }

}
