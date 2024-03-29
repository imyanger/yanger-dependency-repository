package com.yanger.starter.test.controller;

import com.yanger.starter.test.entity.ReturnData;
import com.yanger.starter.test.enums.FileType;
import com.yanger.starter.web.annotation.IgnoreLoginAuth;
import com.yanger.starter.web.annotation.LoginAuth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Mood的接口Controller类
 * @Author yanger
 * @Date 2021-01-08 16:58:04
 */
@Api
@RestController
@RequestMapping("MoodController接口")
@IgnoreLoginAuth
public class MoodController {

    /**
     * 获取心情数据
     * @Author yanger
     * @date 2021-01-08 16:58:04
     * @return 心情数据
     */
    @GetMapping("")
    @ApiOperation(value="根据id查找Mood", tags={"MoodController接口"}, notes="根据id查找Mood")
    public ReturnData findMoodData() {
        return ReturnData.builder().fileType(FileType.FILE).build();
    }

    /**
     * 保存Mood
     * @Author yanger
     * @date 2021-01-08 18:02:38
     * @return void
     */
    @LoginAuth
    @PostMapping
    @ApiOperation(value="保存Mood", tags={"MoodController接口"}, notes="保存Mood")
    public void save() {
    }

}
