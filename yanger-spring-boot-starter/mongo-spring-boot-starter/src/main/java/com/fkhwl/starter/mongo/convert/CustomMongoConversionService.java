package com.fkhwl.starter.mongo.convert;

import org.jetbrains.annotations.Contract;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * @Description 类型 转换 服务,添加了 IEnum 转换
 * @Author yanger
 * @Date 2020/12/29 17:32
 */
public final class CustomMongoConversionService extends DefaultConversionService {

    /**
     * Custom conversion service
     */
    private CustomMongoConversionService() {
        super.addConverter(new EnumToDbConverter());
        super.addConverter(new DbToEnumConverter());
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
     * <p>Company: 成都返空汇网络技术有限公司</p>
     * <p>Description: 静态内部类实现单例</p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dongshijie@fkhwl.com"
     * @date 2020.01.27 18:06
     */
    private static final class SingletonHolder {
        /** INSTANCE */
        private static final CustomMongoConversionService INSTANCE = new CustomMongoConversionService();

        /**
         * Singleton holder
         */
        @Contract(pure = true)
        private SingletonHolder() {

        }
    }

}
