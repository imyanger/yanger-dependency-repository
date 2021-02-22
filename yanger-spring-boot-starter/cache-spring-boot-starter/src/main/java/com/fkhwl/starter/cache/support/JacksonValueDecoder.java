package com.fkhwl.starter.cache.support;

import com.alicp.jetcache.CacheValueHolder;
import com.alicp.jetcache.spi.ValueDecoderRegister;
import com.alicp.jetcache.support.AbstractValueDecoder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fkhwl.starter.basic.util.JsonUtils;
import com.fkhwl.starter.processor.annotation.AutoService;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.util.ByteUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.02.11 18:55
 * @since 1.0.0
 */
@Slf4j
@AutoService(ValueDecoderRegister.class)
public class JacksonValueDecoder extends FkhAbstractValueDecoder implements ValueDecoderRegister {
    /** INSTANCE */
    public static final JacksonValueDecoder INSTANCE = new JacksonValueDecoder();

    /** 反序列化时使用序列化的 mapper */
    private static final ObjectMapper MAPPER = JacksonValueEncoder.mapper;

    /**
     * Jackson value decoder
     *
     * @since 1.0.0
     */
    public JacksonValueDecoder() {
        this(Boolean.parseBoolean(System.getProperty("jetcache.useIdentityNumber", "true")));
    }

    /**
     * Jackson value decoder
     *
     * @param useIdentityNumber use identity number
     * @since 1.0.0
     */
    private JacksonValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    /**
     * Do apply object
     *
     * @param buffer buffer
     * @return the object
     * @since 1.0.0
     */
    @Override
    public Object doApply(@Nullable byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return null;
        }

        if (this.useIdentityNumber) {
            // header
            byte[] prefix = new byte[] {74, -107, 58, -125};
            // 存在 header 的数据肯定是被 CacheValueHolder 包装过
            if (ByteUtils.startsWith(buffer, prefix)) {
                byte[] bs = new byte[buffer.length - 4];
                System.arraycopy(buffer, 4, bs, 0, bs.length);
                return this.deserialize(bs, true);
            } else {
                return this.deserialize(buffer, false);
            }
        } else {
            return this.deserialize(buffer, false);
        }
    }

    /**
     * 所有的数据全部被 {@link CacheValueHolder} 包装
     *
     * @param <T>    parameter
     * @param source can be {@literal null}.
     * @return {@literal null} for empty source.
     * @since 1.0.0
     */
    @Contract("null, _ -> null")
    @SneakyThrows
    @Nullable
    private <T> T deserialize(@Nullable byte[] source, boolean isWrapper) {
        if (source == null || source.length == 0) {
            return null;
        }

        // 如果是被 CacheValueHolder 包装过的数据, 分实体数据和基础数据处理
        if (isWrapper) {
            CacheValueHolder<?> parse = JsonUtils.parse(source, CacheValueHolder.class);
            if (parse == null) {
                return null;
            }

            Object value = parse.getValue();
            if (value == null) {
                return null;
            } else {
                return JsonUtils.parse(MAPPER, source, new TypeReference<CacheValueHolder<T>>() {
                });
            }
        } else {
            return JsonUtils.parse(MAPPER, source, new TypeReference<T>() {
            });
        }
    }

    /**
     * Identity number int
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public int identityNumber() {
        return JacksonValueEncoder.IDENTITY_NUMBER;
    }

    /**
     * Gets decoder *
     *
     * @return the decoder
     * @since 1.0.0
     */
    @Override
    public AbstractValueDecoder getDecoder() {
        return INSTANCE;
    }
}
