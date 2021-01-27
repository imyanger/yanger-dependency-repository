package com.yanger.starter.web.support;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanger.starter.basic.enums.SerializeEnum;
import com.yanger.starter.basic.util.JsonUtils;
import com.yanger.starter.web.annotation.RequestAbstractForm;
import com.yanger.tools.general.format.StringFormatter;
import com.yanger.tools.web.exception.BasicException;
import com.yanger.tools.web.tools.ReflectionUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.http.HttpInputMessage;

import java.io.*;

/**
 * @Description 处理 @RequestAbstractForm, 用于接口接收抽象类, 此注解将自动转换为对应子类
 * @Author yanger
 * @Date 2021/1/27 18:47
 */
public class RequestAbstractFormMethodArgumentResolver extends AbstractMethodArgumentResolver<RequestAbstractForm> {

    /**
     * Request single param handler method argument resolver
     *
     * @param objectMapper               object mapper
     * @param globalEnumConverterFactory global enum converter factory
     */
    @Contract(pure = true)
    public RequestAbstractFormMethodArgumentResolver(ObjectMapper objectMapper, ConverterFactory<String, SerializeEnum<?>> globalEnumConverterFactory) {
        super(objectMapper, globalEnumConverterFactory);
    }

    /**
     * Supports annotation
     *
     * @return the class
     */
    @Override
    protected Class<RequestAbstractForm> supportsAnnotation() {
        return RequestAbstractForm.class;
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
    @Override
    @SuppressWarnings("unchecked")
    protected Object bundleArgument(@NotNull MethodParameter parameter,
                                    JavaType javaType,
                                    HttpInputMessage inputMessage,
                                    @NotNull RequestAbstractForm annotation) {

        Class<? extends SubClassType> subClass = annotation.value();

        if (!SerializeEnum.class.isAssignableFrom(subClass)) {
            throw new BasicException(
                StringFormatter.format("参数转换失败, [{}] 需要实现 SerializeEnum 接口. RequestAbstractForm.value: [{}]", subClass.getSimpleName(), subClass));
        }
        Object[] parse = new Object[1];
        ReflectionUtils.doWithFields(parameter.getParameter().getType(), field -> {
            ReflectionUtils.makeAccessible(field);
            try {
                Object typeValue = JsonUtils.toMap(inputMessage.getBody(), String.class, Object.class).get(field.getName());
                Class<?> type = field.getType();
                Object convert = this.convert((Class<? extends SerializeEnum<?>>) type, typeValue);
                SubClassType subClassType = (SubClassType) convert;
                parse[0] = JsonUtils.parse(inputMessage.getBody(), subClassType.getSubClass());
            } catch (IOException e) {
                throw new BasicException(e);
            }
        }, field -> field.getType().getName().equals(subClass.getName()));
        return parse[0];
    }

}
