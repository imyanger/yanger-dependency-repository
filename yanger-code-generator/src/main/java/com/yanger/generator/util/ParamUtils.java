package com.yanger.generator.util;

import com.yanger.generator.entity.config.ApiConfig;
import com.yanger.generator.entity.config.DaoConfig;
import com.yanger.generator.entity.config.GeneratorConfig;
import com.yanger.generator.entity.config.ServiceConfig;
import com.yanger.generator.entity.param.*;
import com.yanger.generator.entity.sql.ColumnInfo;
import com.yanger.generator.entity.sql.TableInfo;
import com.yanger.generator.enums.ConverterType;
import com.yanger.generator.enums.DaoUtilType;
import com.yanger.generator.enums.ModelType;
import com.yanger.generator.enums.PageType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 参数处理工具类
 * @Author yanger
 * @Date 2020/8/27 16:39
 */
public class ParamUtils {


    /**
     * @Description 组织 ApiParam 参数
     * @Author yanger
     * @Date 2020/11/10 11:14
     * @param: generatorConfig
     * @param: tableInfo
     * @return: com.yanger.generator.entity.param.ApiParam
     */
    public static ApiParam buildApiParam(GeneratorConfig generatorConfig, TableInfo tableInfo) {
        ApiConfig apiConfig = generatorConfig.getApiConfig();
        ServiceConfig serviceConfig = generatorConfig.getServiceConfig();

        GenerateParam generateParam = buildGenerateParam(generatorConfig, tableInfo);
        ApiParam apiParam = ConverterUtils.copyProperties(generateParam, ApiParam::new);
        String codePackage = generateParam.getCodePackage();
        String modelName = generateParam.getModelName();

        apiParam.setMappingName(StrUtils.lowerCaseFirst(tableInfo.getClassName()));
        apiParam.setApiSuffixName(apiConfig.getControllerSuffix());
        apiParam.setQueryPackage(getPackage(codePackage, apiConfig.getQueryType()));
        apiParam.setQueryObjName(getObjName(modelName, apiConfig.getQueryType()));
        apiParam.setRetPackage(getPackage(codePackage, apiConfig.getReturnType()));
        apiParam.setRetObjName(getObjName(modelName, apiConfig.getReturnType()));
        apiParam.setSavePackage(getPackage(codePackage, apiConfig.getSaveType()));
        apiParam.setSaveObjName(getObjName(modelName, apiConfig.getSaveType()));
        if (serviceConfig.isInterfaceNeed()) {
            apiParam.setServiceInterfaceName(serviceConfig.getInterfacePrefix() + modelName + serviceConfig.getInterfaceSuffix());
        } else {
            apiParam.setServiceInterfaceName(modelName + serviceConfig.getImplementationSuffix());
        }
        apiParam.setServiceInterfacePackage(codePackage + ".service");
        apiParam.setServiceObjPackage(getPackage(codePackage, serviceConfig.getObjType()));
        apiParam.setServiceObjName(getObjName(modelName, serviceConfig.getObjType()));

        ApiConverterParam apiConverterParam = ParamUtils.buildApiConverterParam(apiParam);
        apiParam.setAcp(apiConverterParam);

        return apiParam;
    }

    /**
     * @Description 组织 ServiceParam 参数
     * @Author yanger
     * @Date 2020/11/10 11:15
     * @param: generatorConfig
     * @param: tableInfo
     * @return: com.yanger.generator.entity.param.ServiceParam
     */
    public static ServiceParam buildServiceParam(GeneratorConfig generatorConfig, TableInfo tableInfo) {
        ServiceConfig serviceConfig = generatorConfig.getServiceConfig();
        DaoConfig daoConfig = generatorConfig.getDaoConfig();

        GenerateParam generateParam = buildGenerateParam(generatorConfig, tableInfo);
        ServiceParam serviceParam = ConverterUtils.copyProperties(generateParam, ServiceParam::new);
        String codePackage = generateParam.getCodePackage();
        String modelName = generateParam.getModelName();

        serviceParam.setServiceInterfacePackage(codePackage + ".service");
        serviceParam.setServiceImplName(modelName + serviceConfig.getImplementationSuffix());
        if (serviceConfig.isInterfaceNeed()) {
            serviceParam.setServiceImplPackage(codePackage + ".service.impl");
            serviceParam.setServiceInterfaceName(serviceConfig.getInterfacePrefix() + modelName + serviceConfig.getInterfaceSuffix());
        } else {
            serviceParam.setServiceImplPackage(codePackage + ".service");
            serviceParam.setServiceInterfaceName(serviceParam.getServiceImplName());
        }
        serviceParam.setServiceObjPackage(getPackage(codePackage, serviceConfig.getObjType()));
        serviceParam.setServiceObjName(getObjName(modelName, serviceConfig.getObjType()));
        serviceParam.setDaoInterfaceName(daoConfig.getInterfacePrefix() + modelName + daoConfig.getInterfaceSuffix());
        serviceParam.setDaoInterfacePackage(codePackage + ".dao");

        ServiceConverterParam serviceConverterParam = ParamUtils.buildServiceConverterParam(serviceParam);
        serviceParam.setScp(serviceConverterParam);

        return serviceParam;
    }

    /**
     * @Description 组织 DaoParam 参数
     * @Author yanger
     * @Date 2020/11/10 14:20
     * @param: generatorConfig
     * @param: tableInfo
     * @return: com.yanger.generator.entity.param.DaoParam
     */
    public static DaoParam buildDaoParam(GeneratorConfig generatorConfig, TableInfo tableInfo) {
        DaoConfig daoConfig = generatorConfig.getDaoConfig();

        GenerateParam generateParam = buildGenerateParam(generatorConfig, tableInfo);
        DaoParam daoParam = ConverterUtils.copyProperties(generateParam, DaoParam::new);
        String codePackage = generateParam.getCodePackage();
        String modelName = generateParam.getModelName();

        daoParam.setDaoInterfaceName(daoConfig.getInterfacePrefix() + modelName + daoConfig.getInterfaceSuffix());
        daoParam.setDaoInterfacePackage(codePackage + ".dao");
        daoParam.setTableName(tableInfo.getTableName());
        daoParam.setColumns(tableInfo.getColumns());

        // 主键处理
        ColumnInfo pkColumnInfo = tableInfo.getPkColumnInfo();
        daoParam.setPkColumnName(pkColumnInfo.getColumnName());
        daoParam.setPkFieldName(pkColumnInfo.getFieldName());
        daoParam.setPkFieldClass(pkColumnInfo.getFieldClass());

        return daoParam;
    }

    /**
     * @Description 组织 EntityParam 参数
     * @Author yanger
     * @Date 2020/11/10 14:19 
     * @param: generatorConfig
     * @param: tableInfo
     * @param: entityName
     * @param: entityPackage 
     * @return: com.yanger.generator.entity.param.EntityParam
     */
    public static EntityParam buildEntityParams(GeneratorConfig generatorConfig, TableInfo tableInfo, String entityName, String entityPackage) {
        GenerateParam generateParam = buildGenerateParam(generatorConfig, tableInfo);
        EntityParam entityParam = ConverterUtils.copyProperties(generateParam, EntityParam::new);

        entityParam.setColumns(tableInfo.getColumns());
        entityParam.setTableName(tableInfo.getTableName());

        entityParam.setEntityName(entityName);
        entityParam.setEntityPackage(entityPackage);

        // 主键处理
        ColumnInfo pkColumnInfo = tableInfo.getPkColumnInfo();
        entityParam.setPkColumnName(pkColumnInfo.getColumnName());
        entityParam.setPkFieldName(pkColumnInfo.getFieldName());
        
        return entityParam;
    }

    /**
     * @Description 组织 UtilsParam 参数
     * @Author yanger
     * @Date 2020/11/10 18:19
     * @param: generatorConfig
     * @param: tableInfo
     * @return: com.yanger.generator.entity.param.ConverterParam
     */
    public static UtilsParam buildUtilsParam(GeneratorConfig generatorConfig, TableInfo tableInfo) {
        return buildUtilsParam(generatorConfig, tableInfo, buildApiParam(generatorConfig, tableInfo), buildServiceParam(generatorConfig, tableInfo));
    }

    /**
     * @Description 组织 UtilsParam 参数
     * @Author yanger
     * @Date 2020/11/10 18:20
     * @param: generatorConfig
     * @param: tableInfo
     * @param: apiParam
     * @param: serviceParam
     * @return: com.yanger.generator.entity.param.UtilsParam
     */
    public static UtilsParam buildUtilsParam(GeneratorConfig generatorConfig, TableInfo tableInfo, ApiParam apiParam, ServiceParam serviceParam) {
        GenerateParam generateParam = buildGenerateParam(generatorConfig, tableInfo);
        UtilsParam utilsParam = ConverterUtils.copyProperties(generateParam, UtilsParam::new);

        utilsParam.setConverterPackage(generateParam.getCodePackage() + ".util");
        utilsParam.setTableName(tableInfo.getTableName());
        utilsParam.setColumns(tableInfo.getColumns());

        List<ConverterParam> converterParams = buildConverterParam(apiParam, serviceParam);

        if (converterParams != null && converterParams.size() > 0) {
            // 去重处理
            List<ConverterParam> uniqueConverterParams = converterParams.stream().collect(
                Collectors. collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparing(o -> o.getConverterType() + ";" + o.getSourceName() + ";" + o.getSourcePackage() + ";" + o.getTargetName() + ";" + o.getTargetPackage()))),
                    ArrayList::new));
            List<ConverterParam> newConverterParams = new ArrayList<>(converterParams.size());
            converterParams.forEach(s -> {
                if (uniqueConverterParams.contains(s)) {
                    newConverterParams.add(s);
                }
            });
            utilsParam.setConverterParams(newConverterParams);
        }

        // 转换类需要导入的包
        Set<String> importClassList = new HashSet<>();
        if (utilsParam.getConverterParams() != null && utilsParam.getConverterParams().size() > 0) {
            utilsParam.getConverterParams().forEach(s -> {
                if (ConverterType.OBJ.equals(s.getConverterType())){
                    importClassList.add(s.getSourcePackage() + "." + s.getSourceName());
                    importClassList.add(s.getTargetPackage() + "." + s.getTargetName());
                }
            });
        }
        utilsParam.setImportClassList(importClassList);

        return utilsParam;
    }

    /**
     * @Description 组织 GenerateParam 参数
     * @Author yanger
     * @Date 2020/8/30 12:21
     * @param: generatorConfig
     * @param: tableInfo
     * @return: com.yanger.generator.entity.param.GenerateParam
     */
    private static GenerateParam buildGenerateParam(GeneratorConfig generatorConfig, TableInfo tableInfo) {
        String daoUtilType = generatorConfig.getDaoConfig().getDaoUtilType().trim();
        PageType pageType = PageType.PAGE_HELPER;
        if(DaoUtilType.isJPA(daoUtilType)) {
            pageType = PageType.JPA_PAGE;
        } else if(DaoUtilType.isMybatisPlus(daoUtilType)) {
            pageType = PageType.MYBATIS_PLUS_PAGE;
        } else if(DaoUtilType.isMybatis(daoUtilType)) {
            pageType = PageType.PAGE_HELPER;
        }
        String codePackage = generatorConfig.getGeneralConfig().getCodePackage();
        return GenerateParam.builder()
            .authorName(generatorConfig.getGeneralConfig().getAuthorName().trim())
            .codePackage(codePackage)
            .modelName(tableInfo.getClassName().trim())
            .modelPackage(getPackage(codePackage, generatorConfig.getDaoConfig().getObjType().trim()))
            .pageType(pageType)
            .build();
    }

    /**
     * @Description 根据 model 名称和模型类型 组织包名
     * @Author yanger
     * @Date 2020/8/30 13:40
     * @param: codePackage
     * @param: type
     * @return: java.lang.String
     */
    private static String getPackage(String codePackage, String type) {
        ModelType modelType = EnumUtils.getEnumObject(ModelType.class,
                                                      x -> String.valueOf(x.getValue()).equals(type.trim())
                                                           || x.getDesc().equalsIgnoreCase(type.trim())).orElse(ModelType.PO);
        return codePackage + "." + modelType.getDesc().toLowerCase();
    }

    /**
     * @Description 获取对象名称
     * @Author yanger
     * @Date 2020/8/30 13:42
     * @param: modelName
     * @param: type
     * @return: java.lang.String
     */
    private static String getObjName(String modelName, String type) {
        ModelType modelType = EnumUtils.getEnumObject(ModelType.class,
                                                      x -> String.valueOf(x.getValue()).equals(type.trim())
                                                           || x.getDesc().equalsIgnoreCase(type.trim())).orElse(ModelType.PO);
        return modelName + (ModelType.PO.equals(modelType) ? "" : modelType.getDesc());
    }

    /**
     * @Description 根据 ApiParam 生成 ApiConverterParam
     * @Author yanger
     * @Date 2020/8/28 9:17
     * @param: apiParam
     * @return: com.yanger.generator.entity.param.ApiConverterParam
     */
    public static ApiConverterParam buildApiConverterParam(ApiParam apiParam) {
        ApiConverterParam apiConverterParam = new ApiConverterParam();
        boolean needImprotUtil = false;
        String modelName = apiParam.getModelName();
        String serviceObjName = apiParam.getServiceObjName();
        String saveObjName = apiParam.getSaveObjName();
        String retObjName = apiParam.getRetObjName();
        String saveObj = apiParam.getSavePackage() + "." + saveObjName;
        String serviceObj = apiParam.getServiceObjPackage() + "." + serviceObjName;
        String retObj = apiParam.getRetPackage() + "." + retObjName;
        String queryObj = apiParam.getQueryPackage() + "." + apiParam.getQueryObjName();
        if (!saveObj.equals(serviceObj)) {
            apiConverterParam.setSaveOrUpdateCp(getConverterObj(modelName, saveObjName, serviceObjName));
            apiConverterParam.setSaveListCp(getConverterList(modelName, saveObjName, serviceObjName));
            needImprotUtil = true;
        }
        if (!queryObj.equals(serviceObj)) {
            apiConverterParam.setQueryCp(getConverterObj(modelName, apiParam.getQueryObjName(), serviceObjName));
            needImprotUtil = true;
        }
        if (!retObj.equals(serviceObj)) {
            apiConverterParam.setReturnCp(getConverterObj(modelName, serviceObjName, retObjName));
            apiConverterParam.setReturnListCp(getConverterList(modelName, serviceObjName, retObjName));
            // 枚举转换
            ConverterType converterType = EnumUtils.getEnumObject(ConverterType.class,
                                                                  e -> e.getValue().
                                                                      equals(apiParam.getPageType().getValue())).get();
            apiConverterParam.setReturnPageCp(getConverter(modelName, serviceObjName, retObjName, converterType));
            needImprotUtil = true;
        }
        apiConverterParam.setNeedImprotUtil(needImprotUtil);
        return apiConverterParam;
    }

    /**
     * @Description 根据 ServiceParam 生成 ServiceConverterParam
     * @Author yanger
     * @Date 2020/8/28 9:55
     * @param: serviceParam
     * @return: com.yanger.generator.entity.param.ServiceConverterParam
     */
    public static ServiceConverterParam buildServiceConverterParam(ServiceParam serviceParam) {
        ServiceConverterParam serviceConverterParam = new ServiceConverterParam();
        String serviceObjName = serviceParam.getServiceObjName();
        String modelName = serviceParam.getModelName();
        String serviceObj = serviceParam.getServiceObjPackage() + "." + serviceObjName;
        String modelObj = serviceParam.getModelPackage() + "." + modelName;
        if (!modelObj.equals(serviceObj)) {
            serviceConverterParam.setSaveOrUpdateCp(getConverterObj(modelName, serviceObjName, modelName));
            serviceConverterParam.setSaveListCp(getConverterList(modelName, serviceObjName, modelName));
            serviceConverterParam.setReturnCp(getConverterObj(modelName, modelName, serviceObjName));
            serviceConverterParam.setServiceCp(getConverterObj(modelName, serviceObjName, modelName));
            serviceConverterParam.setReturnListCp(getConverterList(modelName, modelName, serviceObjName));
            // 枚举转换
            ConverterType converterType = EnumUtils.getEnumObject(ConverterType.class,
                                                                  e -> e.getValue().equals(serviceParam.getPageType().getValue())).get();
            serviceConverterParam.setReturnPageCp(getConverter(modelName, modelName, serviceObjName, converterType));
            serviceConverterParam.setNeedImprotUtil(true);
        }
        return serviceConverterParam;
    }

    /**
     * @Description 根据 ApiParam、ServiceParam 生成 ConverterParam 集合
     * @Author yanger
     * @Date 2020/11/10 18:15
     * @param: apiParam
     * @param: serviceParam
     * @return: java.util.List<com.yanger.generator.entity.param.ConverterParam>
     */
    public static List<ConverterParam> buildConverterParam(ApiParam apiParam, ServiceParam serviceParam) {
        List<ConverterParam> converterParams = new ArrayList<>();

        // 枚举转换
        ConverterType converterType = EnumUtils.getEnumObject(ConverterType.class,
                                                              e -> e.getValue().equals(serviceParam.getPageType().getValue())).get();
        // form
        String saveObjName = apiParam.getSaveObjName();
        String savePackage = apiParam.getSavePackage();
        String saveObj = savePackage + "." + saveObjName;
        // vo
        String retObjName = apiParam.getRetObjName();
        String retPackage = apiParam.getRetPackage();
        String retObj = retPackage + "." + retObjName;
        // query
        String queryObjName = apiParam.getQueryObjName();
        String queryPackage = apiParam.getQueryPackage();
        String queryObj = queryPackage + "." + queryObjName;
        // dto
        String serviceObjName = serviceParam.getServiceObjName();
        String serviceObjPackage = serviceParam.getServiceObjPackage();
        String serviceObj = serviceObjPackage + "." + serviceObjName;
        // po
        String modelName = apiParam.getModelName();
        String modelPackage = serviceParam.getModelPackage();
        String modelObj = modelPackage + "." + modelName;

        // form2bo、forms2bos
        if (!saveObj.equals(serviceObj)) {
            Arrays.asList(ConverterType.OBJ, ConverterType.LIST).forEach(type -> {
                converterParams.add(ConverterParam.builder()
                                        .converterType(type)
                                        .sourceName(saveObjName).sourcePackage(savePackage)
                                        .targetName(serviceObjName).targetPackage(serviceObjPackage)
                                        .build());
            });
        }
        // query2bo
        if (!queryObj.equals(serviceObj)) {
            converterParams.add(ConverterParam.builder()
                                    .converterType(ConverterType.OBJ)
                                    .sourceName(queryObjName).sourcePackage(queryPackage)
                                    .targetName(serviceObjName).targetPackage(serviceObjPackage)
                                    .build());
        }
        // bo、po
        if (!serviceObj.equals(modelObj)) {
            // bo2po、bos2pos
            Arrays.asList(ConverterType.OBJ, ConverterType.LIST).forEach(type -> {
                converterParams.add(ConverterParam.builder()
                                        .converterType(type)
                                        .sourceName(serviceObjName).sourcePackage(serviceObjPackage)
                                        .targetName(modelName).targetPackage(modelPackage)
                                        .build());
            });
            // po2bo、pos2bos、pos2bos（page）
            Arrays.asList(ConverterType.OBJ, ConverterType.LIST, converterType).forEach(type -> {
                converterParams.add(ConverterParam.builder()
                                        .converterType(type)
                                        .sourceName(modelName).sourcePackage(modelPackage)
                                        .targetName(serviceObjName).targetPackage(serviceObjPackage)
                                        .build());
            });
        }
        // bo2vo、bos2vos、bos2vos（page）
        if (!serviceObj.equals(retObj)) {
            Arrays.asList(ConverterType.OBJ, ConverterType.LIST, converterType).forEach(type -> {
                converterParams.add(ConverterParam.builder()
                                        .converterType(type)
                                        .sourceName(serviceObjName).sourcePackage(serviceObjPackage)
                                        .targetName(retObjName).targetPackage(retPackage)
                                        .build());
            });
        }

        return converterParams;
    }

    /**
     * @Description 获取转换方法
     * @Author yanger
     * @Date 2020/8/28 9:17
     * @param: modelName
     * @param: sourceObjName
     * @param: targetObjName
     * @return: java.lang.String
     */
    private static String getConverterList(String modelName, String sourceObjName, String targetObjName) {
        return getConverter(modelName, sourceObjName, targetObjName, ConverterType.LIST);
    }

    /**
     * @Description 获取转换方法
     * @Author yanger
     * @Date 2020/8/28 9:17
     * @param: modelName
     * @param: sourceObjName 源对象
     * @param: targetObjName 目标对象
     * @return: java.lang.String
     */
    private static String getConverterObj(String modelName, String sourceObjName, String targetObjName) {
        return getConverter(modelName, sourceObjName, targetObjName, ConverterType.OBJ);
    }

    /**
     * @Description 获取转换方法
     * @Author yanger
     * @Date 2020/8/28 9:16
     * @param: modelName
     * @param: sourceObjName
     * @param: targetObjName
     * @param: converterType
     * @return: java.lang.String
     */
    private static String getConverter(String modelName, String sourceObjName, String targetObjName , ConverterType converterType) {
        boolean isObj = ConverterType.OBJ.getValue() == converterType.getValue();
        String sourceSuf = ConverterType.OBJ.getValue() == converterType.getValue() ? "" :
                           ConverterType.LIST.getValue() == converterType.getValue() ? "s" : "Page";
        return new StringBuilder()
            .append(getField(targetObjName, converterType))
            .append(modelName).append("Converter").append(".")
            .append(getSimpleObj(modelName, sourceObjName)).append(isObj ? "" : "s")
            .append("2")
            .append(getSimpleObj(modelName, targetObjName)).append(isObj ? "" : "s")
            .append("(").append(StrUtils.lowerCaseFirst(sourceObjName)).append(sourceSuf).append(")")
            .append(";")
            .toString();
    }

    /**
     * @Description 获取对象简单描述
     * @Author yanger
     * @Date 2020/8/28 9:16
     * @param: modelName
     * @param: objName
     * @return: java.lang.String
     */
    private static String getSimpleObj(String modelName, String objName) {
        return modelName.equals(objName) ? "po" : objName.replace(modelName, "").toLowerCase();
    }

    /**
     * @Description 组装字段部分
     * @Author yanger
     * @Date 2020/8/28 9:15
     * @param: objName
     * @param: converterType
     * @return: java.lang.String
     */
    private static String getField(String objName, ConverterType converterType) {
        String obj,field;
        switch (converterType) {
            case OBJ:
                obj = objName;
                field = StrUtils.lowerCaseFirst(objName);
                break;
            case LIST:
                obj = "List<" + objName + ">";
                field = StrUtils.lowerCaseFirst(objName) + "s";
                break;
            case JPA_PAGE:
            case MYBATIS_PLUS_PAGE:
                obj = "Page<" + objName + ">";
                field = StrUtils.lowerCaseFirst(objName) + "Page";
                break;
            case PAGE_HELPER:
                obj = "PageInfo<" + objName + ">";
                field = StrUtils.lowerCaseFirst(objName) + "Page";
                break;
            default:
                throw new RuntimeException("参数异常");
        }
        return obj + " " + field + " = ";
    }

}

