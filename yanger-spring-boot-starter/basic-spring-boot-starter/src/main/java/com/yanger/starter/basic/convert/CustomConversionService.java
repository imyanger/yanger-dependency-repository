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
     *
     * @since 1.0.0
     */
    private CustomConversionService() {
        this(null);
    }

    /**
     * Custom conversion service
     *
     * @param embeddedValueResolver the embedded value resolver
     * @since 1.0.0
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
     * @since 1.0.0
     */
    @Contract(pure = true)
    public static GenericConversionService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * <p>Company: 成都返空汇网络技术有限公司</p>
     * <p>Description: 静态内部类实现单例</p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.01.27 18:06
     * @since 1.0.0
     */
    private static final class SingletonHolder {
        /** INSTANCE */
        private static final CustomConversionService INSTANCE = new CustomConversionService();

        /**
         * Singleton holder
         *
         * @since 1.0.0
         */
        @Contract(pure = true)
        private SingletonHolder() {

        }
    }

}
