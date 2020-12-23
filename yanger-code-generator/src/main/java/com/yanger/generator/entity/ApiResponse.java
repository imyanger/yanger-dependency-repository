package com.yanger.generator.entity;

import lombok.Data;

/**
 * @Description api调用通用处理对象
 * @Author yanger
 * @Date 2020/5/26 17:24
 */
@Data
public class ApiResponse<T> {

    /** 响应状态码-成功 */
    public static final int SUCCESS = 1;

    /** 响应状态码-失败 */
    public static final int ERROR = -1;

    /** 响应状态码-无效token */
    public static final int ERROR_TOKEN = -2;

    /** 默认成功的相应信息 */
    public static final String SUCCESS_TEXT = "Call processing successful.";

    /** 默认失败的相应信息 */
    public static final String ERROR_TEXT = "Call processing error.";

    /** 响应成功标志 */
    private boolean success = Boolean.TRUE;

    /** 响应状态码 */
    private Integer status = SUCCESS;

    /** 响应的令牌 */
    private String token;

     /** 响应返回信息 */
    private String msg = SUCCESS_TEXT;

     /** 响应数据 */
    private T data;


    public ApiResponse() {}

    public ApiResponse(String token) {
        this();
        this.token = token;
    }

    public ApiResponse(Integer status) {
        this();
        this.status = status;
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    public ApiResponse(Integer status, String token) {
        this();
        this.status = status;
        this.token = token;
    }

    public ApiResponse(Integer status, String token, T data) {
        this();
        this.status = status;
        this.token = token;
        this.data = data;
    }

    public ApiResponse(Integer status, String token, String msg) {
        this();
        this.status = status;
        this.token = token;
        this.msg = msg;
    }

    public ApiResponse(Integer status, String token, String msg, T data) {
        this();
        this.status = status;
        this.token = token;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResponse ok() {
        return new ApiResponse<>();
    }

    public static ApiResponse ok(String token) {
        return new ApiResponse<>(token);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data);
    }

    public static <T> ApiResponse<T> ok(String token, T data) {
        ApiResponse<T> api = new ApiResponse<>(token);
        api.setData(data);
        return api;
    }

    public static ApiResponse fail() {
        return ApiResponse.fail(null, ApiResponse.ERROR_TEXT);
    }

    public static ApiResponse failWithToken(String token) {
        return ApiResponse.fail(token, ApiResponse.ERROR_TEXT);
    }

    public static ApiResponse failWithMsg(String msg) {
        return ApiResponse.fail(null, msg);
    }

    public static ApiResponse fail(String token, String msg) {
        return ApiResponse.fail(token, msg,null);
    }

    public static <T> ApiResponse<T> fail(String token, String msg, T data) {
        return new ApiResponse<T>(ApiResponse.ERROR, token, msg, data);
    }

}
