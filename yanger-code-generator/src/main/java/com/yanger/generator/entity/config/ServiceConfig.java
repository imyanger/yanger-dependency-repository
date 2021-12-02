package com.yanger.generator.entity.config;

import java.io.*;

import lombok.Data;

/**
 * Service配置
 * @Author yanger
 * @Date 2020/7/20 17:38
 */
@Data
public class ServiceConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 是否需要接口 */
    private boolean interfaceNeed = true;

    /** Service的接口的前缀名 */
    private String interfacePrefix = "";

    /** Service的接口的后缀名 */
    private String interfaceSuffix = "Service";

    /** Service的后缀名 */
    private String implementationSuffix = "ServiceImpl";

    /** Service内入参模型类型 */
    private String objType = "po";

}
