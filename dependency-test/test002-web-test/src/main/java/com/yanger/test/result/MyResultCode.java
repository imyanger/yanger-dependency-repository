package com.yanger.test.result;

import com.yanger.tools.web.support.IResultCode;

/**
 * @Author yanger
 * @Date 2022/3/21/021 22:59
 */
public enum  MyResultCode implements IResultCode {

    /** 参数错误 */
    PARAMETER_ERROR_PERCH(602, "参数错误，{}"),

    /** 权限不足 */
    AUTHORITY_INVALID_PERCH(-102, "权限不足，{}");

    private Integer code;
    private String message;

    MyResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getModuleMarker() {
        return "test-module";
    }

}
