package com.yanger.starter.web.support;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.starter.basic.util.DataTypeUtils;
import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.starter.web.annotation.RequestSingleParam;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.tools.ObjectUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.http.HttpInputMessage;

import java.util.Map;

import lombok.SneakyThrows;

/**
 * 处理 @RequestSingleParam
 *     1. 只支持 POST/PUT json 格式的数据解析
 *     2. 可解析多个字段, 前提是 request 允许多次读取
 * @Author yanger
 * @Date 2021/1/27 18:03
 */
public class RequestSingleParamHandlerMethodArgumentResolver extends AbstractMethodArgumentResolver<RequestSingleParam> {

    /**
     * Request single param handler method argument resolver
     *
     * @param objectMapper               object mapper
     * @param globalEnumConverterFactory global enum converter factory
     */
    @Contract(pure = true)
    public RequestSingleParamHandlerMethodArgumentResolver(ObjectMapper objectMapper,
                                                           ConverterFactory<String, SerializeEnum<?>> globalEnumConverterFactory) {
        super(objectMapper, globalEnumConverterFactory);
    }

    /**
     * Supports annotation
     *
     * @return the class
     */
    @Override
    protected Class<RequestSingleParam> supportsAnnotation() {
        return RequestSingleParam.class;
    }

    /**
     * Bundle argument
     *
     * @param parameter    parameter
     * @param javaType     java type
     * @param inputMessage input message
     * @param annotation   request single param
     * @return the object
     */
    @SneakyThrows
    @Override
    @SuppressWarnings("unchecked")
    protected Object bundleArgument(MethodParameter parameter,
                                    JavaType javaType,
                                    @NotNull HttpInputMessage inputMessage,
                                    @NotNull RequestSingleParam annotation) {

        String key = annotation.value();
        Map<Object, Object> map = JsonUtils.toMap(inputMessage.getBody(), String.class, Object.class);

        if (annotation.required() && ObjectUtils.isNull(map.get(key))) {
            throw new BasicException("[{}] is required", annotation.value());
        }

        Class<?> rawClass = javaType.getRawClass();
        // 如果是 EntityEnum 类型, 则先使用 value() 进行转换, 然后是 name(), 最后才是 ordinal()
        if (SerializeEnum.class.isAssignableFrom(rawClass)) {
            return this.convert((Class<? extends SerializeEnum<?>>) rawClass, map.get(key));
        }
        return DataTypeUtils.convert(rawClass, map.get(key));
    }

}
