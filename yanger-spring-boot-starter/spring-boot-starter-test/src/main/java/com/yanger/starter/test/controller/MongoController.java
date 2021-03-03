package com.yanger.starter.test.controller;

import com.yanger.starter.mongo.conditions.query.LambdaQueryWrapper;
import com.yanger.starter.test.mongo.MongoTestLog;
import com.yanger.starter.test.mongo.MongoTestService;
import com.yanger.starter.web.annotation.IgnoreLoginAuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description id生成接口
 * @Author yanger
 * @Date 2021-01-08 16:58:04
 */
@Slf4j
@Api(tags={"cache接口"})
@RestController
@RequestMapping("mongo")
@IgnoreLoginAuth
public class MongoController {

    @Autowired
    private MongoTestService mongoTestService;

    /**
     * @Description 获取id
     * @Author yanger
     * @date 2021-01-08 16:58:04
     */
    @ApiOperation(value="打印日志", tags={"mongo接口"}, notes="打印日志")
    @GetMapping("set")
    public Object set(String name, Integer age) {
        mongoTestService.insert(name, age);
        return mongoTestService.get(name);
    }

    /**
     * @Description 获取id
     * @Author yanger
     * @date 2021-01-08 16:58:04
     */
    @ApiOperation(value="打印日志", tags={"mongo接口"}, notes="打印日志")
    @GetMapping("set2")
    public Object set2(String name, Integer age) {
        MongoTestLog mongoTestLog = MongoTestLog.builder().name(name).age(age).build();
        mongoTestLog.insert();
        mongoTestLog.setName("xxx");
        MongoTestLog mongoTestLog2 = mongoTestLog.selectOne();
        MongoTestLog mongoTestLog1 = mongoTestLog.selectOne(new LambdaQueryWrapper<MongoTestLog>().eq("name", name));
        mongoTestLog.setName("mongo" + name);
        mongoTestLog.setId(null);
        return mongoTestLog1;
    }

}