package com.yanger.generator.entity.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 代码生成配置
 * @Author yanger
 * @Date 2020/7/20 17:38
 */
@Data
public class GeneratorConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private GeneralConfig generalConfig;

    private ApiConfig apiConfig = new ApiConfig();

    private ServiceConfig serviceConfig = new ServiceConfig();

    private DaoConfig daoConfig = new DaoConfig();

    private DataSourceConfig dataSourceConfig;

}
