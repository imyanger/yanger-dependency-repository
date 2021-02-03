package com.yanger.generator.entity.config;

import java.io.*;

import lombok.Data;

/**
 * @Description Api配置
 * @Author yanger
 * @Date 2020/7/20 17:38
 */
@Data
public class ApiConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** controller的命名 */
    private String controllerSuffix = "Controller";

    /** 是否引入swagger-ui */
    private boolean swagger = false;

    /** 存储入参对象模型类型 */
    private String saveType = "po";

    /** 查询入参对象模型类型 */
    private String queryType = "po";

    /** 返回对象模型类型 */
    private String returnType = "po";

    /** 通用返回对象名（全限定名） */
    private String returnObjName = "";

}
