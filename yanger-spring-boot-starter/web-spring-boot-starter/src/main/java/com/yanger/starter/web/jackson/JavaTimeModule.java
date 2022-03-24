package com.yanger.starter.web.jackson;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.yanger.tools.general.format.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * java 8 时间默认序列化
 * @Author yanger
 * @Date 2021/1/27 17:34
 */
public class JavaTimeModule extends SimpleModule {

    private static final long serialVersionUID = -8312156928465504942L;

    public JavaTimeModule() {
        super(PackageVersion.VERSION);
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormat.DATETIME_FORMAT));
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormat.DATE_FORMAT));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormat.TIME_FORMAT));
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormat.DATETIME_FORMAT));
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormat.DATE_FORMAT));
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormat.TIME_FORMAT));
    }

}
