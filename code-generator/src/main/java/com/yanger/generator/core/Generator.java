package com.yanger.generator.core;

import com.yanger.generator.entity.bo.TemplateStructure;
import com.yanger.generator.entity.config.*;
import com.yanger.generator.entity.sql.TableInfo;
import com.yanger.generator.enums.CodeNameCase;
import com.yanger.generator.enums.DaoUtilType;
import com.yanger.generator.enums.ModelType;
import com.yanger.generator.enums.TemplateType;
import com.yanger.generator.exception.CodeGenerateException;
import com.yanger.generator.exception.ConfigValidateException;
import com.yanger.generator.util.DataSourceParser;
import com.yanger.generator.util.FreemarkerUtils;
import com.yanger.generator.util.SqlParser;
import com.yanger.generator.util.TemplateStructureParser;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description 代码生成器顶级抽象类
 * @Author yanger
 * @Date 2020/7/20 17:38
 */
@Slf4j
public class Generator {

    /**
     * @Description 生成代码
     * @author yanger
     */
    public void generate(GeneratorConfig generatorConfig) {
        checkConfig(generatorConfig);
        DataSourceConfig dataSourceConfig = generatorConfig.getDataSourceConfig();
        String codePath = generatorConfig.getGeneralConfig().getCodePath();
        String nameCase = generatorConfig.getDaoConfig().getNameCase();
        String tinyintTransType = generatorConfig.getDaoConfig().getTinyintTransType();
        if (dataSourceConfig.isIntactDb()) {
            List<TableInfo> tableInfos = DataSourceParser.parse(dataSourceConfig, nameCase, tinyintTransType);
            tableInfos.forEach(tableInfo -> generate(generatorConfig, tableInfo, codePath));
        } else {
            String sqlFilePath = dataSourceConfig.getSqlFilePath();
            Arrays.stream(sqlFilePath.split(";")).forEach(file -> {
                List<TableInfo> tableInfos = getTableInfos(file, nameCase, tinyintTransType);
                tableInfos.forEach(tableInfo -> generate(generatorConfig, tableInfo, codePath));
            });
        }

    }

    /**
     * @Description 生成代码
     * @Author yanger
     * @Date 2020/11/18 19:15
     * @param: generatorConfig
     * @param: tableInfo
     * @param: codePath
     */
    private void generate(GeneratorConfig generatorConfig, TableInfo tableInfo, String codePath) {
        List<TemplateStructure> templateStructures = TemplateStructureParser.parse(generatorConfig, tableInfo);
        templateStructures.forEach(templateStructure -> {
            try {
                String codeContent = FreemarkerUtils.processString(templateStructure.getTemplatePath(), templateStructure.getParam());
                String codePackage = templateStructure.getCodePackage();
                String fileName = templateStructure.getFileName();
                if (TemplateType.MAPPER.equals(templateStructure.getTemplateType())){
                    codePackage = codePath + "\\src\\main\\resources\\" + codePackage.replaceAll("\\.", "\\\\");
                    fileName += ".xml";
                } else {
                    codePackage = codePath + "\\src\\main\\java\\" + codePackage.replaceAll("\\.", "\\\\");
                    fileName += ".java";
                }
                log.info("生成文件==>{}，路径==>{}", fileName, codePackage);
                File path = new File(codePackage);
                if (!path.exists()) {
                    path.mkdirs();
                }
                File codeFile = new File(path, fileName);
                FileUtils.write(codeFile, codeContent, StandardCharsets.UTF_8);
            } catch (IOException | TemplateException e) {
                log.error("代码生成异常", e);
            }
        });
    }


    /**
     * @Description 解析 sql 获取 TableInfo
     * @Author yanger
     * @Date 2020/8/30 11:57
     * @param: sqlFilePath
     * @param: nameCase
     * @param: tinyintTransType
     * @return: com.yanger.generator.entity.sql.TableInfo
     */
    private List<TableInfo> getTableInfos(String sqlFilePath, String nameCase, String tinyintTransType) {
        List<TableInfo> tableInfos = new ArrayList<>(0);
        ClassPathResource resource = new ClassPathResource(sqlFilePath);
        String sql = null;
        try {
            sql = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                sql = IOUtils.toString(new FileInputStream(new File(sqlFilePath)), StandardCharsets.UTF_8);
            } catch (IOException ioe) {
                throw new CodeGenerateException("读取sql文件" + sqlFilePath + "异常：" + ioe.getMessage());
            }
            if (StringUtils.isEmpty(sql)) {
                throw new CodeGenerateException("读取sql文件" + sqlFilePath + "异常：" + e.getMessage());
            }
        }
        try {
            CodeNameCase codeNameCase = Arrays.stream(CodeNameCase.values())
                .filter(s -> s.getValue().equalsIgnoreCase(nameCase) || s.getCode().toString().equals(nameCase)).findFirst().orElse(CodeNameCase.CAMEL);
            Arrays.stream(sql.split(";")).forEach(eachSql -> {
                if (StringUtils.isNotEmpty(eachSql) && StringUtils.isNotEmpty(eachSql.replaceAll("\r\n", "")
                          .replaceAll("\n", "").replaceAll("\r", "").replaceAll(" ", ""))) {
                    TableInfo tableInfo = SqlParser.parseSql(eachSql, tinyintTransType, codeNameCase);
                    tableInfos.add(tableInfo);
                }
            });
            return tableInfos;
        } catch (Exception e) {
            throw new CodeGenerateException("解析sql异常：" + e.getMessage());
        }
    }

    /**
     * @Description 参数校验
     * @Author yanger
     * @Date 2020/8/29 14:13
     * @param: generatorConfig
     */
    private void checkConfig(GeneratorConfig generatorConfig) {
        checkGeneralConfig(generatorConfig.getGeneralConfig());
        checkApiConfig(generatorConfig.getApiConfig());
        checkServiceConfig(generatorConfig.getServiceConfig());
        checkDaoConfig(generatorConfig.getDaoConfig());
        checkDataSourceConfig(generatorConfig.getDataSourceConfig());
    }

    /**
     * @Description 通用参数校验
     * @Author yanger
     * @Date 2020/8/29 14:13
     * @param: generalConfig
     */
    private void checkGeneralConfig(GeneralConfig generalConfig) {
        if(StringUtils.isEmpty(generalConfig.getCodePath())) {
            throw new ConfigValidateException("代码目录 codePath 不能为空");
        }
        if(StringUtils.isEmpty(generalConfig.getCodePackage())) {
            throw new ConfigValidateException("代码包名 codePackage 不能为空");
        }
    }

    /**
     * @Description api 参数校验
     * @Author yanger
     * @Date 2020/8/29 14:13
     * @param: apiConfig
     */
    private void checkApiConfig(ApiConfig apiConfig) {
        checkObjType("ApiConfig.saveType", apiConfig.getSaveType());
        checkObjType("ApiConfig.queryType", apiConfig.getQueryType());
        checkObjType("ApiConfig.returnType", apiConfig.getReturnType());
    }

    /**
     * @Description service 参数校验
     * @Author yanger
     * @Date 2020/8/29 14:13
     * @param: serviceConfig
     */
    private void checkServiceConfig(ServiceConfig serviceConfig) {
        checkObjType("ServiceConfig.objType", serviceConfig.getObjType());
    }

    /**
     * @Description dao 参数校验
     * @Author yanger
     * @Date 2020/8/29 14:13
     * @param: daoConfig
     */
    private void checkDaoConfig(DaoConfig daoConfig) {
        checkObjType("DaoConfig.objType", daoConfig.getObjType());
        String nameCase = daoConfig.getNameCase();
        long nameCaseMatchCount = Arrays.stream(CodeNameCase.values())
            .filter(s -> s.getCode().toString().equals(nameCase) || s.getValue().equalsIgnoreCase(nameCase)).count();
        if (nameCaseMatchCount == 0) {
            throw new ConfigValidateException("不被支持的 DaoConfig.nameCase 参数值 " + nameCase + "，请使用 1、2 或者 camel、underline");
        }
        String daoUtilType = daoConfig.getDaoUtilType();
        long daoUtilTypeMatchCount = Arrays.stream(DaoUtilType.values())
            .filter(s -> s.getValue().toString().equals(daoUtilType) || s.getDesc().equalsIgnoreCase(daoUtilType)).count();
        if (daoUtilTypeMatchCount == 0) {
            throw new ConfigValidateException("不被支持的 DaoConfig.daoUtilType 参数值 " + daoUtilType + "，请使用 1、2、3、4 或者 mybatis-xml、mybatis-annotation、mybatis-plus、jpa");
        }
    }

    /**
     * @Description 校验数据库配置
     * @Author yanger
     * @Date 2020/8/29 14:13
     * @param: serviceConfig
     */
    private void checkDataSourceConfig(DataSourceConfig dataSourceConfig) {
        if (dataSourceConfig == null) {
            throw new ConfigValidateException("请设置数据源配置 dataSourceConfig");
        }
        if (dataSourceConfig.isDb()) {
            if(!dataSourceConfig.isIntactDb()) {
                throw new ConfigValidateException("数据源配置不完整，如果使用数据源方式生成代码，请配置 dataSourceConfig 必要的【driverName、url、userName、password】");
            }
        } else {
            String sqlFilePath = dataSourceConfig.getSqlFilePath();
            if (StringUtils.isEmpty(sqlFilePath)) {
                throw new ConfigValidateException("请设置数据源配置 dataSourceConfig，使用数据库连接请配置【driverName、url、userName、password】，使用 sql 文件请配置 dataSourceConfig.sqlFilePath");
            }
            ClassPathResource resource = new ClassPathResource(sqlFilePath);
            File file;
            try {
                file = resource.getFile();
            } catch (IOException e) {
                file = new File(sqlFilePath);
            }
            if (file == null || !file.exists()) {
                throw new ConfigValidateException("文件磁盘或 resources 目录下未发现 sql 文件：" + sqlFilePath);
            }
        }
    }

    /**
     * @Description 校验方法类型
     * @Author yanger
     * @Date 2020/8/29 14:13
     * @param: objTypeName
     * @param: objTypeVal
     */
    private void checkObjType(String objTypeName, String objTypeVal){
        Optional<ModelType> type = Arrays.stream(ModelType.values())
            .filter(modelType -> modelType.getValue().toString().equals(objTypeVal) || modelType.getDesc().equalsIgnoreCase(objTypeVal)).findFirst();
        if (!type.isPresent()) {
            throw new ConfigValidateException("不被支持的 " + objTypeName + " 参数值 " + objTypeVal + "，请使用 1、2、3、4、5、6 或者 vo、query、form、dto、bo、po");
        }
    }

}
