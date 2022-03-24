package com.yanger.starter.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.yanger.tools.general.constant.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * jackson 默认值为 null 时的处理, 主要是为了避免 app 端出现null导致闪退
 *     规则:
 *     {@code
 *     number: 0
 *     string: null
 *     date: null
 *     boolean: false
 *     array: []
 *     Object: {}
 *     }
 * @Author yanger
 * @Date 2021/1/27 17:34
 */
public class DefaultBeanSerializerModifier extends com.fasterxml.jackson.databind.ser.BeanSerializerModifier {
    /**
     * Change properties list
     * @param config         config
     * @param beanDesc       bean desc
     * @param beanProperties bean properties
     * @return the list
     * @see DefaultSerializerProvider#_serializeNull(com.fasterxml.jackson.core.JsonGenerator)
     */
    @Override
    @SuppressWarnings("all")
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     @NotNull List<BeanPropertyWriter> beanProperties) {
        beanProperties.forEach(writer -> {
            // 如果已经有 null 序列化处理如注解: @JsonSerialize(nullsUsing = xxx) 跳过
            if (writer.hasNullSerializer()) {
                return;
            }
            JavaType type = writer.getType();
            Class<?> clazz = type.getRawClass();
            if (type.isTypeOrSubTypeOf(Number.class)) {
                // writer.assignNullSerializer(NullJsonSerializers.NUMBER_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Boolean.class)) {
                writer.assignNullSerializer(NullJsonSerializers.BOOLEAN_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Character.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(String.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isArrayType() || clazz.isArray() || type.isTypeOrSubTypeOf(Collection.class)) {
                writer.assignNullSerializer(NullJsonSerializers.ARRAY_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(OffsetDateTime.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Date.class) || type.isTypeOrSubTypeOf(TemporalAccessor.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else {
                writer.assignNullSerializer(NullJsonSerializers.OBJECT_JSON_SERIALIZER);
            }
        });
        return super.changeProperties(config, beanDesc, beanProperties);
    }

    /**
     * The interface Null json serializers.
     */
    @SuppressWarnings("checkstyle:InterfaceIsType")
    public interface NullJsonSerializers {

        /**
         * The constant STRING_JSON_SERIALIZER.
         */
        JsonSerializer<Object> STRING_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(StringPool.EMPTY);
            }
        };

        /**
         * The constant NUMBER_JSON_SERIALIZER.
         */
        JsonSerializer<Object> NUMBER_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(StringUtils.INDEX_NOT_FOUND);
            }
        };

        /**
         * The constant BOOLEAN_JSON_SERIALIZER.
         */
        JsonSerializer<Object> BOOLEAN_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeObject(Boolean.FALSE);
            }
        };

        /**
         * The constant ARRAY_JSON_SERIALIZER.
         */
        JsonSerializer<Object> ARRAY_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartArray();
                gen.writeEndArray();
            }
        };

        /**
         * The constant OBJECT_JSON_SERIALIZER.
         */
        JsonSerializer<Object> OBJECT_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeEndObject();
            }
        };

    }

}
