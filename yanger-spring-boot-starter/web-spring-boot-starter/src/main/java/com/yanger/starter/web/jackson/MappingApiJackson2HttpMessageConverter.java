package com.yanger.starter.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

/**
 * 针对 api 服务对 android 和 ios 和 web 处理的 分读写的 jackson 处理
 *     1. app 端上报数据是 使用 readObjectMapper
 *     2. 返回给 app 端的数据使用 writeObjectMapper
 * @Author yanger
 * @Date 2021/1/27 17:34
 */
public class MappingApiJackson2HttpMessageConverter extends AbstractReadWriteJackson2HttpMessageConverter {

    /** Json prefix */
    @Nullable
    private String jsonPrefix;

    public MappingApiJackson2HttpMessageConverter() {
        this(Jackson2ObjectMapperBuilder.json().build());
    }

    public MappingApiJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, initWriteObjectMapper(objectMapper), MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
    }

    /**
     * 初始化负责序列化的 ObjectMapper, 设置 null 处理.
     * 如果需要扩展, 只需要重写 setSerializerFactory 即可.
     * @param readObjectMapper read object mapper
     * @return the object mapper
     */
    private static @NotNull ObjectMapper initWriteObjectMapper(@NotNull ObjectMapper readObjectMapper) {
        // 拷贝 readObjectMapper
        ObjectMapper writeObjectMapper = readObjectMapper.copy();
        writeObjectMapper.setSerializerFactory(writeObjectMapper.getSerializerFactory()
                                                   .withSerializerModifier(new DefaultBeanSerializerModifier()));
        return writeObjectMapper;
    }

    /**
     * Specify a custom prefix to use for this view's JSON output.
     * Default is none.
     * @param jsonPrefix jsonPrefix
     * @see #setPrefixJson #setPrefixJson#setPrefixJson#setPrefixJson#setPrefixJson#setPrefixJson
     */
    public void setJsonPrefix(@Nullable String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    /**
     * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is false.
     * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
     * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
     * This prefix should be stripped before parsing the string as JSON.
     * @param prefixJson prefixJson
     * @see #setJsonPrefix #setJsonPrefix#setJsonPrefix#setJsonPrefix#setJsonPrefix#setJsonPrefix
     */
    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = (prefixJson ? ")]}', " : null);
    }

    /**
     * Write prefix
     * @param generator generator
     * @param object    object
     * @throws IOException io exception
     */
    @Override
    protected void writePrefix(@NotNull JsonGenerator generator, @NotNull Object object) throws IOException {
        if (this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }
    }

}
