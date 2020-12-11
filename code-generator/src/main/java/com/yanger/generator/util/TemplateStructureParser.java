package com.yanger.generator.util;

import com.yanger.generator.entity.bo.TemplateStructure;
import com.yanger.generator.entity.config.ApiConfig;
import com.yanger.generator.entity.config.DaoConfig;
import com.yanger.generator.entity.config.GeneratorConfig;
import com.yanger.generator.entity.config.ServiceConfig;
import com.yanger.generator.entity.param.*;
import com.yanger.generator.entity.sql.TableInfo;
import com.yanger.generator.enums.ConverterWay;
import com.yanger.generator.enums.DaoUtilType;
import com.yanger.generator.enums.TemplateType;
import com.yanger.generator.exception.ConfigValidateException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 解析参数生成代码生成结构对象
 * @Author yanger
 * @Date 2020/9/16 19:55
 */
public class TemplateStructureParser {

    /**
     * @Description 解析参数，组织代码生成结构
     * @author yanger
     * @date 2020/9/16 23:51
     * @param generatorConfig
     * @param tableInfo
     * @return java.util.List<com.yanger.generator.entity.bo.TemplateStructure>
     */
    public static List<TemplateStructure> parse(GeneratorConfig generatorConfig, TableInfo tableInfo) {
        List<TemplateStructure> templateStructures = new ArrayList <>();

        ApiParam apiParam = ParamUtils.buildApiParam(generatorConfig, tableInfo);
        ServiceParam serviceParam = ParamUtils.buildServiceParam(generatorConfig, tableInfo);
        DaoParam daoParam = ParamUtils.buildDaoParam(generatorConfig, tableInfo);
        UtilsParam utilsParam = ParamUtils.buildUtilsParam(generatorConfig, tableInfo);

        ApiConfig apiConfig = generatorConfig.getApiConfig();
        ServiceConfig serviceConfig = generatorConfig.getServiceConfig();
        DaoConfig daoConfig = generatorConfig.getDaoConfig();

        String daoUtilType = daoConfig.getDaoUtilType();
        boolean interfaceNeed = serviceConfig.isInterfaceNeed();
        String codePackage = generatorConfig.getGeneralConfig().getCodePackage();

        // api
        templateStructures.add(TemplateStructure.builder().templateType(TemplateType.API).param(apiParam)
                .fileName(apiParam.getModelName() + apiParam.getApiSuffixName())
                .codePackage(codePackage + ".api").templatePath(getApiTemplateName(apiConfig)).build());
        String serviceTemplateName = getServiceTemplateName(daoUtilType, interfaceNeed);
        // service
        String serviceImplPackage = codePackage + ".service";

        if (StringUtils.isNotBlank(serviceTemplateName)) {
            templateStructures.add(TemplateStructure.builder().templateType(TemplateType.SERVICE).param(serviceParam)
                    .fileName(serviceParam.getServiceInterfaceName())
                    .codePackage(codePackage + ".service").templatePath(serviceTemplateName).build());
            serviceImplPackage = codePackage + ".service.impl";
        }
        // service-impl
        templateStructures.add(TemplateStructure.builder().templateType(TemplateType.SERVICE_IMPL).param(serviceParam)
                .fileName(serviceParam.getServiceImplName())
                .codePackage(serviceImplPackage).templatePath(getServiceImplTemplateName(daoUtilType, interfaceNeed)).build());
        // dao
        templateStructures.add(TemplateStructure.builder().templateType(TemplateType.DAO).param(daoParam)
                .fileName(daoParam.getDaoInterfaceName())
                .codePackage(codePackage + ".dao").templatePath(getDaoImplTemplateName(daoUtilType)).build());
        // converter
        List<ConverterParam> converterParams = utilsParam.getConverterParams();
        if (converterParams != null && converterParams.size() > 0) {
            String convertWay = generatorConfig.getGeneralConfig().getConvertWay();
            templateStructures.add(TemplateStructure.builder().templateType(TemplateType.CONVERT).param(utilsParam)
                   .fileName(utilsParam.getModelName() + "Converter")
                   .codePackage(codePackage + ".util").templatePath(getConvertImplTemplateName(apiParam, serviceParam, convertWay)).build());
        }
        // mapper
        if (DaoUtilType.isMybatisXml(daoUtilType)) {
            templateStructures.add(TemplateStructure.builder().templateType(TemplateType.MAPPER).param(daoParam)
                    .fileName(daoParam.getModelName() + "Mapper")
                    .codePackage("mapper").templatePath("/mapper/mybatis-xml.ftl").build());
        }
        // entity
        templateStructures.addAll(getEntityTemplateStructures(generatorConfig, tableInfo, apiParam, serviceParam));
        return templateStructures;
    }

    /**
     * @Description 获取api模板名
     * @Author yanger
     * @Date 2020/9/16 20:00 
     * @param: apiConfig 
     * @return: java.lang.String
     */
    private static String getApiTemplateName(ApiConfig apiConfig) {
        boolean swagger = apiConfig.isSwagger();
        // 自定义返回类型
        String returnObjName = apiConfig.getReturnObjName();
        if (swagger) {
            if(StringUtils.isNotEmpty(returnObjName)) {
                return "/api/return-swagger-api.ftl";
            } else {
                return "/api/swagger-api.ftl";
            }
        } else {
            if(StringUtils.isNotEmpty(returnObjName)) {
                return "/api/return-api.ftl";
            } else {
                return "/api/simple-api.ftl";
            }
        }
    }

    /**
     * @Description 获取service模板
     * @author yanger
     * @date 2020/9/16 22:10
     * @param daoUtilType
     * @param interfaceNeed
     * @return java.lang.String
     */
    private static String getServiceTemplateName(String daoUtilType, boolean interfaceNeed) {
        daoUtilType = daoUtilType.toLowerCase();
        if (interfaceNeed) {
            if (DaoUtilType.isMybatis(daoUtilType)) {
                return "/service/service-interface.ftl";
            } else if (DaoUtilType.isMybatisPlus(daoUtilType)) {
                return "/service/mp-service-interface.ftl";
            } else if (DaoUtilType.isJPA(daoUtilType)) {
                return "/service/jpa-service-interface.ftl";
            } else {
                throw new ConfigValidateException("不符合规定的 daoUtilType：" + daoUtilType);
            }
        } else {
            return null;
        }
    }

    /**
     * @Description 获取service实现类模板
     * @author yanger
     * @date 2020/9/16 22:10
     * @param daoUtilType
     * @param interfaceNeed
     * @return java.lang.String
     */
    private static String getServiceImplTemplateName(String daoUtilType, boolean interfaceNeed) {
        daoUtilType = daoUtilType.toLowerCase();
        if (interfaceNeed) {
            if (DaoUtilType.isMybatis(daoUtilType)) {
                return "/service/service-impl.ftl";
            } else if (DaoUtilType.isMybatisPlus(daoUtilType)) {
                return "/service/mp-service-impl.ftl";
            } else if (DaoUtilType.isJPA(daoUtilType)) {
                return "/service/jpa-service-impl.ftl";
            } else {
                throw new ConfigValidateException("不符合规定的 daoUtilType：" + daoUtilType);
            }
        } else {
            if (DaoUtilType.isMybatis(daoUtilType)) {
                return "/service/service.ftl";
            } else if (DaoUtilType.isMybatisPlus(daoUtilType)) {
                return "/service/mp-service.ftl";
            } else if (DaoUtilType.isJPA(daoUtilType)) {
                return "/service/jpa-service.ftl";
            } else {
                throw new ConfigValidateException("不符合规定的 daoUtilType：" + daoUtilType);
            }
        }
    }

    /**
     * @Description 获取dao模板
     * @author yanger
     * @date 2020/9/16 22:37
     * @param daoUtilType
     * @return java.lang.String
     */
    private static String getDaoImplTemplateName(String daoUtilType) {
        DaoUtilType type = Arrays.stream(DaoUtilType.values()).filter(s -> s.getValue().equals(daoUtilType) || s.getDesc().equals(daoUtilType.toLowerCase())).findAny().get();
        switch (type) {
            case MybatisXml:
                return "/dao/mybatis-xml.ftl";
            case MybatisAnnotation:
                return "/dao/mybatis-annotation.ftl";
            case MybatisPlus:
                return "/dao/mybatis-plus.ftl";
            case JPA:
                return "/dao/jpa.ftl";
            default:
                throw new ConfigValidateException("不符合规定的 daoUtilType：" + daoUtilType);
        }
    }

    /**
     * @Description 获取转换工具模板
     * @author yanger
     * @date 2020/9/16 22:57
     * @param apiParam
     * @param serviceParam
     * @param convertWay
     * @return java.lang.String
     */
    private static String getConvertImplTemplateName(ApiParam apiParam, ServiceParam serviceParam, String convertWay) {
        String saveObj = apiParam.getSavePackage() + "." + apiParam.getSaveObjName();
        String retObj = apiParam.getRetPackage() + "." + apiParam.getRetObjName();
        String queryObj = apiParam.getQueryPackage() + "." + apiParam.getQueryObjName();
        String modelObj = serviceParam.getModelPackage() + "." + apiParam.getModelName();
        String serviceObj = serviceParam.getServiceObjPackage() + "." + serviceParam.getServiceObjName();
        if(saveObj.equals(serviceObj) && queryObj.equals(serviceObj) && retObj.equals(serviceObj) && modelObj.equals(serviceObj)) {
            return null;
        } else {
            if (ConverterWay.BEAN_UTILS.getValue().equals(convertWay) || ConverterWay.BEAN_UTILS.getDesc().toLowerCase().equals(convertWay.toLowerCase())) {
                return "/convert/converter-bean-utils.ftl";
            } else if (ConverterWay.GET_SET.getValue().equals(convertWay) || ConverterWay.GET_SET.getDesc().toLowerCase().equals(convertWay.toLowerCase())) {
                return "/convert/converter-getset.ftl";
            } else {
                throw new ConfigValidateException("不符合规定的 convertWay：" + convertWay);
            }
        }
    }

    /**
     * @Description 构造所有需要生成模型类的 TemplateStructure 集合
     * @Author yanger
     * @Date 2020/11/10 18:44
     * @param: generatorConfig
     * @param: tableInfo
     * @param: apiParam
     * @param: serviceParam
     * @return: java.util.List<com.yanger.generator.entity.bo.TemplateStructure>
     * @throws
     */
    private static List<TemplateStructure> getEntityTemplateStructures(GeneratorConfig generatorConfig, TableInfo tableInfo,
                                                                       ApiParam apiParam, ServiceParam serviceParam) {
        List<TemplateStructure> entityTss = new ArrayList<>();
        String modelName = apiParam.getModelName();
        String modelObj = serviceParam.getModelPackage() + "." + modelName;
        String saveObj = apiParam.getSavePackage() + "." + apiParam.getSaveObjName();
        String retObj = apiParam.getRetPackage() + "." + apiParam.getRetObjName();
        String queryObj = apiParam.getQueryPackage() + "." + apiParam.getQueryObjName();
        String serviceObj = serviceParam.getServiceObjPackage() + "." + serviceParam.getServiceObjName();
        entityTss.add(getEntityTemplateStructure(modelName, modelName, generatorConfig, tableInfo));
        Arrays.asList(saveObj, retObj, queryObj, serviceObj).forEach(s -> {
            if (!modelObj.equals(s)) {
                String objName = s.substring(s.lastIndexOf(".") + 1);
                entityTss.add(getEntityTemplateStructure(modelName, objName, generatorConfig, tableInfo));
            }
        });
        // 去重处理
        return entityTss.stream().collect(Collectors. collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getFileName()))), ArrayList::new));
    }


    /**
     * @Description 构造 TemplateStructure 对象
     * @Author yanger
     * @Date 2020/11/10 18:44
     * @param: modelName
     * @param: objName
     * @param: generatorConfig
     * @param: tableInfo
     * @return: com.yanger.generator.entity.bo.TemplateStructure
     * @throws
     */
    private static TemplateStructure getEntityTemplateStructure(String modelName, String objName, GeneratorConfig generatorConfig, TableInfo tableInfo) {
        boolean swagger = generatorConfig.getApiConfig().isSwagger();
        boolean lombok = generatorConfig.getGeneralConfig().isLombok();
        String codePackage = generatorConfig.getGeneralConfig().getCodePackage();
        String daoUtilType = generatorConfig.getDaoConfig().getDaoUtilType();

        String templatePath = "/entity/simple.ftl";
        if (swagger && lombok) {
            templatePath = "/entity/lombok-swagger.ftl";
        } else {
            if (swagger) {
                templatePath = "/entity/swagger.ftl";
            }
            if (lombok) {
                templatePath = "/entity/lombok.ftl";
            }
        }
        if (DaoUtilType.isJPA(daoUtilType) && modelName.equals(objName)) {
            templatePath = templatePath.replace("/entity/", "/entity/jpa-");
        } else if(DaoUtilType.isMybatisPlus(daoUtilType) && modelName.equals(objName)) {
            templatePath = templatePath.replace("/entity/", "/entity/mp-");
        }

        String entityPackage = codePackage + "." + (modelName.equals(objName) ? "po" : objName.replace(modelName, "").toLowerCase());
        EntityParam entityParam = ParamUtils.buildEntityParams(generatorConfig, tableInfo, objName, entityPackage);
        TemplateStructure entityTs = TemplateStructure.builder().templateType(TemplateType.ENTITY).param(entityParam)
            .fileName(objName)
            .codePackage(entityPackage).templatePath(templatePath).build();

        return entityTs;
    }

}
