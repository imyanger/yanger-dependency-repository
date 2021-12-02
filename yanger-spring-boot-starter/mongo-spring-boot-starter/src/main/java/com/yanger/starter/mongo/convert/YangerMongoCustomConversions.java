package com.yanger.starter.mongo.convert;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**

 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public class YangerMongoCustomConversions extends MongoCustomConversions {

    /**
     * Init list
     *
     * @param customerConverters customer converters
     * @return the list
     */
    private static @NotNull List<Object> init(List<Converter<?, ?>> customerConverters) {
        List<Object> converters = new ArrayList<>();
        converters.add(new BooleanToEnumConverter());
        converters.add(new EnumToBooleanConverter());
        converters.add(new EnumToDbConverter());
        converters.add(new DbToEnumConverter());

        converters.addAll(customerConverters);
        return Collections.unmodifiableList(converters);
    }

    /**
     * Yanger mongo custom conversions
     *
     * @param converters converters
     */
    public YangerMongoCustomConversions(List<Converter<?, ?>> converters) {
        super(init(converters));
    }

}
