package com.yanger.web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yanger.general.constant.StringPool;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description 通用返回体
 * @Author yanger
 * @Date 2020/12/21 18:23
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class Result<T> implements Serializable {

    /** serialVersionUID */
    public static final long serialVersionUID = 1L;

    /** SUCCESS */
    public static final String FIELD_SUCCESS = "success";

    /** CODE */
    public static final String FIELD_CODE = "code";

    /** MESSAGE */
    public static final String FIELD_MESSAGE = "message";

    /** DATA */
    public static final String FIELD_DATA = "data";

    /** TRACE_ID */
    public static final String FIELD_TRACE_ID = "traceId";


    /** 请求响应成功标识 */
    @ApiModelProperty(value = "成功状态", required = true, example = "true")
    protected boolean success;

    /** 请求响应状态码 */
    @ApiModelProperty(value = "状态码", required = true, example = "2000")
    protected Integer code;

    /** 请求响应的消息 */
    @ApiModelProperty(value = "返回消息", required = true, example = "操作成功")
    protected String message;

    /** 请求响应的数据 */
    @ApiModelProperty(value = "承载数据", required = true)
    protected T data;

    /** 请求响应的溯源标识 */
    @ApiModelProperty(value = "溯源标识", required = true, example = "38.197.15675853221800001")
    protected String traceId;

    /**
     * Result
     *
     * @param code    code
     * @param message message
     * @param data    data
     * @param traceId trace id
     */
    @Contract(pure = true)
    protected Result(@NotNull Integer code, String message, T data, String traceId, boolean success) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.success = success;
        this.traceId = StringUtils.isBlank(traceId) ? StringPool.NULL_STRING : traceId;
    }

    /**
     * Is ok boolean
     *
     * @return the boolean
     */
    @JsonIgnore
    public boolean isOk() {
        return isOk(this);
    }

    /**
     * 请求是否成功
     *
     * @param result result
     * @return the boolean
     */
    @Contract("null -> false")
    public static boolean isOk(@Nullable Result<?> result) {
        return result != null && result.isSuccess();
    }

    /**
     * Is fail boolean
     *
     * @return the boolean
     */
    @JsonIgnore
    public boolean isFail() {
        return isFail(this);
    }

    /**
     * 请求是否失败
     *
     * @param result result
     * @return the boolean
     */
    @Contract("null -> true")
    public static boolean isFail(@Nullable Result<?> result) {
        return !isOk(result);
    }

}
