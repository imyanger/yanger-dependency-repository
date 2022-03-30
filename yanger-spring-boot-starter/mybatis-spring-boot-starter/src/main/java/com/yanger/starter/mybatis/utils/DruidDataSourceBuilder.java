package com.yanger.starter.mybatis.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.yanger.starter.mybatis.property.DataSourceDetail;

/**
 * @Author yanger
 * @Date 2022/3/26/026 23:58
 */
public class DruidDataSourceBuilder {

    public static DruidDataSource build(DataSourceDetail dataSourceDetail){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSourceDetail.getUrl());
        druidDataSource.setUsername(dataSourceDetail.getUsername());
        druidDataSource.setPassword(dataSourceDetail.getPassword());
        druidDataSource.setDriverClassName(dataSourceDetail.getDriverClassName());
        return druidDataSource;
    }

}
