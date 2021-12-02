package com.yanger.starter.test.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import io.swagger.models.auth.In;

/**
 * TODO
 * @Author yanger
 * @Date 2021/3/2 15:36
 */
@Service
public class MongoTestService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(String name, Integer age) {
        MongoTestLog build = MongoTestLog.builder().name(name).age(age).build();
        mongoTemplate.insert(build);
    }

    public MongoTestLog get(String name) {
        Criteria criteria = Criteria.where("name").is(name);
        Query query = new Query(criteria);
        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        MongoTestLog mongoTestLog = mongoTemplate.findOne(query, MongoTestLog.class);
        return mongoTestLog;
    }

}
