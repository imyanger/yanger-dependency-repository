package com.yanger.generator.entity.config;

import com.yanger.generator.enums.TemplateType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 代码生成配置
 * @Author yanger
 * @Date 2020/7/20 17:38
 */
@Data
public class GeneratorConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 代码生成通用配置 */
    private GeneralConfig generalConfig;

    /** Api配置 */
    private ApiConfig apiConfig = new ApiConfig();

    /** Service配置 */
    private ServiceConfig serviceConfig = new ServiceConfig();

    /** dao配置 */
    private DaoConfig daoConfig = new DaoConfig();

    /** 数据源配置 */
    private DataSourceConfig dataSourceConfig;

    /**  成模板类型集合 */
    private List<TemplateType> templateTypes = new ArrayList<>(0);

    /**
     * 增加生成模板类型
     * @Author yanger
     * @Date 2020/12/17 14:11
     * @param: templateType
     * @return: com.yanger.generator.entity.config.GeneratorConfig
     * @throws
     */
    public GeneratorConfig addTemplate(TemplateType templateType) {
        templateTypes.add(templateType);
        return this;
    }

}
