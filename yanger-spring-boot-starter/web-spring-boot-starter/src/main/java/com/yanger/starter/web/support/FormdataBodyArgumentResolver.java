package com.yanger.starter.web.support;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.starter.web.annotation.FormDataBody;
import com.yanger.starter.web.util.WebUtils;
import com.yanger.tools.web.tools.IoUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;

/**
 * 处理 @FormDataBody, 用于将 formdata 数据转换为实体
 * @Author yanger
 * @Date 2021/1/27 18:52
 */
@Slf4j
public class FormdataBodyArgumentResolver extends AbstractMethodArgumentResolver<FormDataBody> {

    /**
     * Request single param handler method argument resolver
     * @param objectMapper               object mapper
     * @param globalEnumConverterFactory global enum converter factory
     */
    @Contract(pure = true)
    public FormdataBodyArgumentResolver(ObjectMapper objectMapper, ConverterFactory<String, SerializeEnum<?>> globalEnumConverterFactory) {
        super(objectMapper, globalEnumConverterFactory);
    }

    /**
     * Supports annotation
     * @return the class
     */
    @Override
    protected Class<FormDataBody> supportsAnnotation() {
        return FormDataBody.class;
    }

    /**
     * Bundle argument
     * @param parameter    parameter
     * @param javaType     java type
     * @param inputMessage input message
     * @param annotation   request single param
     * @return the object
     */
    @Override
    protected Object bundleArgument(@NotNull MethodParameter parameter, @NotNull JavaType javaType,
                                    @NotNull HttpInputMessage inputMessage, @NotNull FormDataBody annotation) {
        Class<?> rawClass = javaType.getRawClass();
        try {
            return JsonUtils.parse(JsonUtils.toJson(WebUtils.converterToMap(IoUtils.toString(inputMessage.getBody()))), rawClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
