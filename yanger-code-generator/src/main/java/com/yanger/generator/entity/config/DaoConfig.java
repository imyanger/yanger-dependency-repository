package com.yanger.generator.entity.config;

import java.io.*;

import lombok.Data;

/**
 * dao配置
 * @Author yanger
 * @Date 2020/7/20 17:38
 */
@Data
public class DaoConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** dao的接口的前缀名 */
    private String interfacePrefix = "";

    /** dao的接口的后缀名 */
    private String interfaceSuffix = "Dao";

    /** dao入参模型类型 */
    private String objType = "po";

    /** dao层持久类工具 */
    private String daoUtilType = "mybatis-plus";

    /** 数据库字段代码规范风格 */
    private String nameCase = "camel";

    /** tinyint转换类型 */
    private String tinyintTransType = "Boolean";

}
