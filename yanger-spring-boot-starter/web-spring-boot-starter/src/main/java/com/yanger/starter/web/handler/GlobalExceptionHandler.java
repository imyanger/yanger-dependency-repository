package com.yanger.starter.web.handler;

import com.yanger.tools.web.entity.R;
import com.yanger.tools.web.entity.Result;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.support.ResultCode;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 统一异常处理
 * @Author yanger
 * @Date 2020/12/7 19:04
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("服务器异常", e);
        return R.failed(ResultCode.ERROR);
    }

    @ExceptionHandler(BasicException.class)
    public Result businessExceptionHandler(BasicException e) {
        return R.failed(e.getMessage());
    }

}
