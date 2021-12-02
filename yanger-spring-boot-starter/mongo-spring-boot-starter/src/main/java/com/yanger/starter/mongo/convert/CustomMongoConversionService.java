package com.yanger.starter.mongo.convert;

import org.jetbrains.annotations.Contract;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * 类型 转换 服务,添加了 IEnum 转换
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
     * 静态内部类实现单例
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
