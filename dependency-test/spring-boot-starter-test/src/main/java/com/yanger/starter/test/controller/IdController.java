package com.yanger.starter.test.controller;

import com.yanger.starter.id.service.IdService;
import com.yanger.starter.web.annotation.IgnoreLoginAuth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * id生成接口
 * @Author yanger
 * @Date 2021-01-08 16:58:04
 */
@Slf4j
@Api(tags={"id生成接口"})
@RestController
@RequestMapping("id")
@IgnoreLoginAuth
public class IdController {

    @Resource
    private IdService idService;

    /**
     * 获取id
     * @Author yanger
     * @date 2021-01-08 16:58:04
     */
    @ApiOperation(value="获取id", tags={"id生成接口"}, notes="获取id")
    @GetMapping("id")
    public Long find() {
        long l = idService.genId();
        // long l1 = idService.makeId(System.currentTimeMillis(), 1L);
        return l;
    }

}