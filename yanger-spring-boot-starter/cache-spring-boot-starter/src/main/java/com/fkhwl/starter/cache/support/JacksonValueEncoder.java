package com.fkhwl.starter.cache.support;

import com.alicp.jetcache.CacheValueHolder;
import com.alicp.jetcache.support.AbstractValueEncoder;
import com.alicp.jetcache.support.CacheEncodeException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fkhwl.starter.basic.util.JsonUtils;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.support.NullValue;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: json 系列化器
 * useIdentityNumber 属性用于修改 serialPolicy 以后的值兼容（比如 serialPolicy 改成新的了,redis 里面还是旧的）
 * todo-dong4j : (2020年02月12日 14:11) [兼容处理 v4 双引号问题]
 * </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.11 18:55
 * @since 1.0.0
 */
public class JacksonValueEncoder extends AbstractValueEncoder {
    /** INSTANCE */
    public static final JacksonValueEncoder INSTANCE = new JacksonValueEncoder();
    /** header 填充 */
    static final int IDENTITY_NUMBER = 0x4A953A83;
    /** Mapper */
    static ObjectMapper mapper;

    /**
     * Jackson value encoder
     *
     * @since 1.0.0
     */
    public JacksonValueEncoder() {
        this(Boolean.parseBoolean(System.getProperty("jetcache.useIdentityNumber", "true")));
    }

    /**
     * Jackson value encoder
     *
     * @param useIdentityNumber use identity number
     * @since 1.0.0
     */
    private JacksonValueEncoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
        this.init((String) null);
    }

    /**
     * 使用给定名称配置默认类型的 ObjectMapper, 对于空字符串, 将使用默认的 JsonTypeInfo.Id.CLASS
     *
     * @param classPropertyTypeName class property type name
     * @since 1.0.0
     */
    @SuppressWarnings("deprecation")
    private void init(@Nullable String classPropertyTypeName) {
        mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addSerializer(new NullValueSerializer(classPropertyTypeName)));

        // 序列化时添加类名, , 反序列化时使用当前的 mapper
        if (StringUtils.hasText(classPropertyTypeName)) {
            mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, classPropertyTypeName);
        } else {
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
    }

    /**
     * 序列化 value 逻辑, 如果 userIdentityNumber 为 true, 则会在序列化为 byte[] 前加上版本号, 用于新旧版本的兼容处理
     *
     * @param value value
     * @return the byte [ ]
     * @since 1.0.0
     */
    @Override
    public byte[] apply(Object value) {
        try {
            if (value == null) {
                return JsonUtils.EMPTY_ARRAY;
            }

            // 其他不是 CacheValueHolder 类型的数据全部包装为 CacheValueHolder (实体 + 集合)
            byte[] bs1 = this.wrapper(value, value.getClass());

            // 添加 header 作为版本号
            if (this.useIdentityNumber) {
                byte[] bs2 = new byte[bs1.length + 4];
                this.writeHeader(bs2, IDENTITY_NUMBER);
                System.arraycopy(bs1, 0, bs2, 4, bs1.length);
                return bs2;
            } else {
                return bs1;
            }

        } catch (Exception e) {
            throw new CacheEncodeException("Jsokson Encode error. " + "msg=" + e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> byte[] wrapper(@NotNull Object value, Class<T> type) throws JsonProcessingException {
        byte[] bs1;

        if (!value.getClass().isAssignableFrom(CacheValueHolder.class)) {
            CacheValueHolder<T> holder = new CacheValueHolder<>((T) value, 3748289283947L);
            bs1 = mapper.writeValueAsBytes(holder);
        } else {
            bs1 = mapper.writeValueAsBytes(value);
        }
        return bs1;
    }

    /**
     * {@link StdSerializer} 添加默认类型所需的类信息. 这允许对 {@link NullValue}. 进行反序列化
     *
     * @author dong4j
     * @version 1.2.4
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.02.11 19:25
     * @since 1.0.0
     */
    private static class NullValueSerializer extends StdSerializer<NullValue> {

        /** serialVersionUID */
        private static final long serialVersionUID = 1999052150548658808L;
        /** Class identifier */
        private final String classIdentifier;

        /**
         * Null value serializer
         *
         * @param classIdentifier can be {@literal null} and will be defaulted to {@code @class}.
         * @since 1.0.0
         */
        NullValueSerializer(@Nullable String classIdentifier) {

            super(NullValue.class);
            this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
        }

        /**
         * Serialize *
         *
         * @param value    value
         * @param jgen     jgen
         * @param provider provider
         * @throws IOException io exception
         * @since 1.0.0
         */
        @Override
        public void serialize(NullValue value, @NotNull JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

            jgen.writeStartObject();
            jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
            jgen.writeEndObject();
        }
    }

}
