package com.yanger.starter.mybatis.dynamic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("execute use dataSource {}", DataSourceContextHolder.getDB());
        return DataSourceContextHolder.getDB();
    }

}
