package com.yanger.starter.test.mongo;

import com.fkhwl.starter.mongo.annotation.MongoCollection;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/3/2 18:00
 */
@MongoCollection(datasource = "mongodb://social:fkh%40zol4@192.168.2.23:12131,192.168.2.23:12132,192.168.2.23:12133/fkh_test")
public class MongoDataSource {

    private String datasource = "mongodb://social:fkh%40zol4@192.168.2.23:12131,192.168.2.23:12132,192.168.2.23:12133/fkh_test2";
}
