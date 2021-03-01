package com.yanger.starter.web.handler;

import com.yanger.tools.web.entity.R;
import com.yanger.tools.web.entity.Result;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.support.ResultCode;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 统一异常处理
 * @Author yanger
 * @Date 2020/12/7 19:04
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Exception 异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        log.error("服务器异常", e);
        return R.failed(ResultCode.ERROR);
    }

    /**
     * BasicException 异常处理
     */
    @ExceptionHandler(BasicException.class)
    public Result businessExceptionHandler(BasicException e) {
        return R.failed(e.getResultCode(), e.getMessage());
    }

    /**
     * BindException 异常处理方式，获取单条描述
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(@NotNull BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String msg = getBindingResultFirstMsg(bindingResult);
        log.warn("参数绑定校验异常: {} params: {}", msg, bindingResult.getTarget());
        return R.failed(ResultCode.PARAMETER_ERROR_PERCH, msg);
    }

    /**
     * MethodArgumentNotValidException 异常处理方式，获取单条描述
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(@NotNull MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String msg = getBindingResultFirstMsg(bindingResult);
        log.warn("参数绑定校验异常: {} params: {}", msg, bindingResult.getTarget());
        return R.failed(ResultCode.PARAMETER_ERROR_PERCH, msg);
    }

    /**
     * ConstraintViolationException 异常处理方式，获取单条描述
     *
     * @param e e
     * @return the result
     * @since 1.0.0
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleValidException(@NotNull ConstraintViolationException e) {
        String msg = null;
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> c : constraintViolations) {
            msg = c.getMessageTemplate() != null ? c.getMessageTemplate() : c.getMessage();
            if (msg != null) {
                break;
            }
        }
        if (msg == null) {
            msg = e.getMessage();
        }
        log.warn("参数校验失败: [{}]", msg);
        return R.failed(ResultCode.PARAMETER_ERROR_PERCH, msg);
    }

    /**
     * 获取 BindingResult 首条报错信息
     */
    private String getBindingResultFirstMsg(@NotNull BindingResult bindingResult) {
        String msg = null;
        for (ObjectError error : bindingResult.getAllErrors()) {
            if (StringUtils.isNotEmpty(error.getDefaultMessage())) {
                msg = error.getDefaultMessage();
            } else {
                if (error instanceof FieldError) {
                    msg = ((FieldError) error).getField() + "参数错误";
                }
            }
            if (msg != null) {
                break;
            }
        }
        return msg;
    }

}
