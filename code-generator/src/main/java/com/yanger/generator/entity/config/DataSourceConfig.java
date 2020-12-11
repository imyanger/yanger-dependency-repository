package com.yanger.generator.entity.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 数据源配置
 * @Author yanger
 * @Date 2020/7/20 17:38
 */
@Data
public class DataSourceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** sql文件 */
    private String sqlFilePath = "generator.sql";

    /** 数据库驱动名称 */
    private String driverName;

    /** 数据库连接地址 */
    private String url;

    /** 数据库用户名 */
    private String userName;

    /** 数据库用户密码 */
    private String password;

    /** 数据库 schema */
    private String schema;

    /** 需要生成的表 */
    private List<String> tables;

    /**
     * @Description 添加要生成的表
     * @Author yanger
     * @Date 2020/11/18 16:47
     * @param: table
     * @return: com.yanger.generator.entity.config.DataSourceConfig
     * @throws
     */
    public DataSourceConfig addTable(String table) {
        if(tables == null) {
            tables = new ArrayList<>(1);
        }
        tables.add(table);
        return this;
    }

    /**
     * @Description 判断是不是完整的DB配置
     * @Author yanger
     * @Date 2020/11/18 18:47
     * @return: boolean
     */
    public boolean isIntactDb() {
        return StringUtils.isNotEmpty(driverName) && StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password);
    }

    /**
     * @Description 判断是不是DB
     * @Author yanger
     * @Date 2020/11/18 18:47
     * @return: boolean
     */
    public boolean isDb() {
        return StringUtils.isNotEmpty(driverName) || StringUtils.isNotEmpty(url) || StringUtils.isNotEmpty(userName) || StringUtils.isNotEmpty(password);
    }

}
