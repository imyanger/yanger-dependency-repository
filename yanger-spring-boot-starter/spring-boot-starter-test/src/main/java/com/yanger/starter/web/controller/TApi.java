package com.yanger.starter.web.controller;

import com.yanger.starter.web.entity.AuthUser;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author yanger
 * @Date 2021/1/25 19:00
 */
@RequestMapping("test")
@RestController
public class TApi {

    @GetMapping("aa")
    public AuthUser authUser() {
        return AuthUser.builder().build();
    }

}
