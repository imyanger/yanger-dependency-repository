package com.fkhwl.starter.mongo.convert;

import org.jetbrains.annotations.Contract;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * <p>Company: 成都返空汇网络技术有限公司</p>
 * <p>Description: 类型 转换 服务,添加了 IEnum 转换 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dongshijie@fkhwl.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
public final class CustomMongoConversionService extends DefaultConversionService {

    /**
     * Custom conversion service
     *
     * @since 1.0.0
     */
    private CustomMongoConversionService() {
        super.addConverter(new EnumToDbConverter());
        super.addConverter(new DbToEnumConverter());
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
        private static final CustomMongoConversionService INSTANCE = new CustomMongoConversionService();

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
