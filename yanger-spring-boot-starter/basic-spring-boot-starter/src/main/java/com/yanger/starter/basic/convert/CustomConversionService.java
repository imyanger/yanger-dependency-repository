package com.yanger.starter.basic.convert;

import org.jetbrains.annotations.Contract;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/**
 * @Description 类型 转换 服务,添加了 IEnum 转换
 * @Author yanger
 * @Date 2020/12/29 19:18
 */
public final class CustomConversionService extends ApplicationConversionService {

    /**
     * Custom conversion service
     */
    private CustomConversionService() {
        this(null);
    }

    /**
     * Custom conversion service
     *
     * @param embeddedValueResolver the embedded value resolver
     */
    private CustomConversionService(@Nullable StringValueResolver embeddedValueResolver) {
        super(embeddedValueResolver);
        super.addConverter(new EnumToStringConverter());
        super.addConverter(new StringToEnumConverter());
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    @Contract(pure = true)
    public static GenericConversionService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 静态内部类实现单例
     */
    private static final class SingletonHolder {

        /** INSTANCE */
        private static final CustomConversionService INSTANCE = new CustomConversionService();

        @Contract(pure = true)
        private SingletonHolder() {}

    }

}
