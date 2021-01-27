package com.yanger.starter.web.jackson;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.yanger.tools.general.constant.Charsets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.TypeUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description 分读写的 json 消息 处理器
 * @Author yanger
 * @Date 2021/1/27 17:34
 */
public abstract class AbstractReadWriteJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    /** DEFAULT_CHARSET */
    private static final java.nio.charset.Charset DEFAULT_CHARSET = Charsets.UTF_8;

    /** Write object mapper */
    private final ObjectMapper writeObjectMapper;

    /** Sse pretty printer */
    @Nullable
    private PrettyPrinter ssePrettyPrinter;

    /**
     * Instantiates a new Abstract read write jackson 2 http message converter.
     *
     * @param readObjectMapper   the read object mapper
     * @param writeObjectMapper  the write object mapper
     * @param supportedMediaType the supported media type
     */
    public AbstractReadWriteJackson2HttpMessageConverter(ObjectMapper readObjectMapper, ObjectMapper writeObjectMapper,
                                                         MediaType supportedMediaType) {
        this(readObjectMapper, writeObjectMapper);
        this.setSupportedMediaTypes(Collections.singletonList(supportedMediaType));
        this.initSsePrettyPrinter();
    }

    /**
     * Instantiates a new Abstract read write jackson 2 http message converter.
     *
     * @param readObjectMapper  the read object mapper
     * @param writeObjectMapper the write object mapper
     */
    public AbstractReadWriteJackson2HttpMessageConverter(ObjectMapper readObjectMapper, ObjectMapper writeObjectMapper) {
        super(readObjectMapper);
        this.writeObjectMapper = writeObjectMapper;
        this.initSsePrettyPrinter();
    }

    /**
     * Init sse pretty printer
     */
    private void initSsePrettyPrinter() {
        this.setDefaultCharset(DEFAULT_CHARSET);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new DefaultIndenter("  ", "\ndata:"));
        this.ssePrettyPrinter = prettyPrinter;
    }

    /**
     * Instantiates a new Abstract read write jackson 2 http message converter.
     *
     * @param readObjectMapper    the read object mapper
     * @param writeObjectMapper   the write object mapper
     * @param supportedMediaTypes the supported media types
     */
    public AbstractReadWriteJackson2HttpMessageConverter(ObjectMapper readObjectMapper,
                                                         ObjectMapper writeObjectMapper,
                                                         MediaType... supportedMediaTypes) {
        this(readObjectMapper, writeObjectMapper);
        this.setSupportedMediaTypes(Arrays.asList(supportedMediaTypes));
    }

    /**
     * Can write boolean
     *
     * @param clazz     clazz
     * @param mediaType media type
     * @return the boolean
     */
    @Override
    public boolean canWrite(@NotNull Class<?> clazz, @Nullable MediaType mediaType) {
        if (!this.canWrite(mediaType)) {
            return false;
        }
        AtomicReference<Throwable> causeRef = new AtomicReference<>();
        if (this.objectMapper.canSerialize(clazz, causeRef)) {
            return true;
        }
        this.logWarningIfNecessary(clazz, causeRef.get());
        return false;
    }

    /**
     * 最终的序列化逻辑
     *
     * @param object        object
     * @param type          type
     * @param outputMessage output message
     * @throws IOException                     io exception
     * @throws HttpMessageNotWritableException http message not writable exception
     * @see AbstractGenericHttpMessageConverter#write
     */
    @Override
    protected void writeInternal(@NotNull Object object, @Nullable Type type, @NotNull HttpOutputMessage outputMessage)
        throws IOException, HttpMessageNotWritableException {

        MediaType contentType = outputMessage.getHeaders().getContentType();
        JsonEncoding encoding = this.getJsonEncoding(contentType);
        JsonGenerator generator = this.writeObjectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
        try {
            this.writePrefix(generator, object);

            Object value = object;
            Class<?> serializationView = null;
            FilterProvider filters = null;
            JavaType javaType = null;

            if (object instanceof MappingJacksonValue) {
                MappingJacksonValue container = (MappingJacksonValue) object;
                value = container.getValue();
                serializationView = container.getSerializationView();
                filters = container.getFilters();
            }
            if (type != null && TypeUtils.isAssignable(type, value.getClass())) {
                javaType = this.getJavaType(type, null);
            }

            ObjectWriter objectWriter = (serializationView != null
                                         ? this.writeObjectMapper.writerWithView(serializationView)
                                         : this.writeObjectMapper.writer());
            if (filters != null) {
                objectWriter = objectWriter.with(filters);
            }
            if (javaType != null && javaType.isContainerType()) {
                objectWriter = objectWriter.forType(javaType);
            }
            SerializationConfig config = objectWriter.getConfig();
            if (contentType != null
                && contentType.isCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                && config.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
                objectWriter = objectWriter.with(this.ssePrettyPrinter);
            }
            objectWriter.writeValue(generator, value);

            this.writeSuffix(generator, object);
            generator.flush();
        } catch (InvalidDefinitionException ex) {
            throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getOriginalMessage(), ex);
        }
    }

}
