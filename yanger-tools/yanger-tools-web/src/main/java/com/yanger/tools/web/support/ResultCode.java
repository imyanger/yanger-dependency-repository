package com.yanger.tools.web.support;

/**
 * @Description 请求响应代码接口枚举
 * @Author yanger
 * @Date 2020/12/18 18:01
 */
public enum ResultCode implements IResultCode {

    /** token过期 */
    TOKE_INVALID(-200, "token过期"),

    /** token过期 */
    TOKE_INVALID_MSG(-201, null),

    /** token过期 */
    TOKE_INVALID_PERCH(-202, "token过期，{}"),

    /** 权限不足 */
    AUTHORITY_INVALID(-100, "权限不足"),

    /** 权限不足 */
    AUTHORITY_INVALID_MSG(-101, null),

    /** 权限不足 */
    AUTHORITY_INVALID_PERCH(-102, "权限不足，{}"),

    /** 处理成功 */
    OK(200, "处理成功"),

    /** 处理成功 */
    OK_MSG(201, null),

    /** 处理成功 */
    OK_PERCH(202, "处理成功，{}"),

    /** 服务器异常 */
    ERROR(500, "服务器异常"),

    /** 服务器异常 */
    ERROR_MSG(501, null),

    /** 服务器异常 */
    ERROR_PERCH(502, "服务器异常，{}"),

    /** 参数错误 */
    PARAMETER_ERROR(600, "参数错误"),

    /** 参数错误 */
    PARAMETER_ERROR_MSG(601, null),

    /** 参数错误 */
    PARAMETER_ERROR_PERCH(602, "参数错误，{}"),

    /** 业务处理异常 */
    BUSINESS_ERROR(700, "业务处理异常"),

    /** 业务处理异常 */
    BUSINESS_ERROR_MSG(701, null),

    /** 业务处理异常 */
    BUSINESS_ERROR_PERCH(702, "业务处理异常，{}"),

    /** 调用第三方服务异常 */
    THIRD_ERROR(800, "调用第三方服务异常"),

    /** 调用第三方服务异常 */
    THIRD_ERROR_MSG(801, null),

    /** 调用第三方服务异常 */
    THIRD_ERROR_PERCH(802, "调用第三方服务异常，{}");

    /** 状态码 */
    private Integer code;

    /** 状态信息 */
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取返回消息, 可使用占位符
     *
     * @return String message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 获取返回状态码
     *
     * @return String code
     */
    @Override
    public Integer getCode() {
        return code;
    }

}
