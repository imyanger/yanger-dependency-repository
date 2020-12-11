package com.yanger.generator.exception;

/**
 * @Description Config校验异常
 * @Author yanger
 * @Date 2020/8/29 13:48
 */
public class ConfigValidateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;

    public ConfigValidateException(String message) {
        super(message);
    }

}
