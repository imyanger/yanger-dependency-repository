package com.yanger.tools.web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yanger.tools.general.constant.StringPool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * 通用返回体
 * @Author yanger
 * @Date 2020/12/21 18:23
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public abstract class Result<T> implements Serializable {

    public static final long serialVersionUID = 1L;

    /** CODE */
    public static final String FIELD_CODE = "code";

    /** MESSAGE */
    public static final String FIELD_MESSAGE = "message";

    /** DATA */
    public static final String FIELD_DATA = "data";

    /** SUCCESS */
    public static final String FIELD_SUCCESS = "success";

    /** TRACE_ID */
    public static final String FIELD_TRACE_ID = "traceId";

    /** MODULE_MARKER */
    public static final String FIELD_MODULE_MARKER = "moduleMarker";

    /** 请求响应状态码 */
    @ApiModelProperty(value = "状态码", required = true, example = "200")
    protected Integer code;

    /** 请求响应的消息 */
    @ApiModelProperty(value = "返回消息", required = false, example = "操作成功")
    protected String message;

    /** 请求响应的数据 */
    @ApiModelProperty(value = "承载数据", required = false)
    protected T data;

    /** 请求响应成功标识 */
    @ApiModelProperty(value = "成功状态", required = true, example = "true")
    protected boolean success;

    /** 请求响应的溯源标识 */
    @ApiModelProperty(value = "溯源标识", required = false, example = "38.197.15675853221800001")
    protected String traceId;

    /** 模块标识 */
    @ApiModelProperty(value = "模块标识", required = false, example = "yanger-base-server")
    protected String moduleMarker;

    /**
     * Result
     * @param code    code
     * @param message message
     * @param data    data
     * @param traceId trace id
     */
    @Contract(pure = true)
    protected Result(@NotNull Integer code, String message, T data, boolean success, String traceId, String moduleMarker) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
        this.traceId = StringUtils.isBlank(traceId) ? StringPool.NULL_STRING : traceId;
        this.moduleMarker = moduleMarker;
    }

    /**
     * Is ok boolean
     * @return the boolean
     */
    @JsonIgnore
    public boolean isOk() {
        return isOk(this);
    }

    /**
     * 请求是否成功
     * @param result result
     * @return the boolean
     */
    @Contract("null -> false")
    public static boolean isOk(@Nullable Result<?> result) {
        return result != null && result.isSuccess();
    }

    /**
     * Is fail boolean
     * @return the boolean
     */
    @JsonIgnore
    public boolean isFail() {
        return isFail(this);
    }

    /**
     * 请求是否失败
     * @param result result
     * @return the boolean
     */
    @Contract("null -> true")
    public static boolean isFail(@Nullable Result<?> result) {
        return !isOk(result);
    }

}
