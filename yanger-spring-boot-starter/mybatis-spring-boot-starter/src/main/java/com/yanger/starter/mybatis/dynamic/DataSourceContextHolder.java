package com.yanger.starter.mybatis.dynamic;

import java.util.HashMap;
import java.util.Map;

public class DataSourceContextHolder {

    /** 默认数据源 */
    private static String DEFAULT_DATA_SOURCE = null;

    /** 读库  */
    public static final String READ_DATA_SOURCE = "dynamic-read";

    /** 写库  */
    public static final String WRITE_DATA_SOURCE = "dynamic-write";
    
    /** 包名和数据源映射map  */
    public static final Map<String, String> PACKAGE_DATA_SOURCE_MAP = new HashMap<>();

    /** 保存当前操作的数据库名  */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 设置默认数据源名称
     * @param dbName 默认数据源
     * @Date 2022/03/30 21:44
     */
    public static synchronized void setDefaultDB(String dbName){
        DEFAULT_DATA_SOURCE = dbName;
    }

    /**
     * 获取默认数据源名称
     * @param
     * @return {@link String} 默认数据源
     * @Author yanger
     * @Date 2022/03/30 21:45
     */
    public static String getDefaultDB(){
        return DEFAULT_DATA_SOURCE;
    }

    /**
     * 设置当前数据源名称
     * @param dbName 当前数据源名称
     * @Author yanger
     * @Date 2022/03/30 21:45
     */
    public static void setDB(String dbName) {
        contextHolder.set(dbName);
    }

    /**
     * 获取数据源名
     * @return {@link String} 当前数据源名称
     * @Author yanger
     * @Date 2022/03/30 21:46
     */
    public static String getDB() {
        return (contextHolder.get());
    }

    /**
     * 清除当前数据源名称
     */
    public static void clearDB() {
        contextHolder.remove();
    }

    /**
     * 切换到读库
     */
    public static void switchToRead() {
        contextHolder.set(READ_DATA_SOURCE);
    }

    /**
     * 切换到写库
     */
    public static void switchToWrite() {
        contextHolder.set(WRITE_DATA_SOURCE);
    }

}

