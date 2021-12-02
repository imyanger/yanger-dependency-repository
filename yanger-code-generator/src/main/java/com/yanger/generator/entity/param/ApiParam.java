package com.yanger.generator.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Api代码生成的参数
 * @Author yanger
 * @Date 2020/7/21 9:58
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ApiParam extends GenerateParam {

    /** controller的后缀名 */
    private String apiSuffixName;

    /** RequestMapping的值 */
    private String mappingName;

    /** 存储入参对象模型类型 */
    private String saveObjName;

    /** 存储入参对象模型包名 */
    private String savePackage;

    /** 查询入参对象模型类型 */
    private String queryObjName;

    /** 查询入参对象模型包名 */
    private String queryPackage;

    /** 返回对象模型类型 */
    private String retObjName;

    /** 返回对象模型包名 */
    private String retPackage;

    /** Service接口类类名 */
    private String serviceInterfaceName;

    /** Service接口代码包 */
    private String serviceInterfacePackage;

    /** Service层入参模型类型 */
    private String serviceObjName;

    /** Service层入参模型类型包名 */
    private String serviceObjPackage;

    /** 转换对象参数 */
    private ApiConverterParam acp;

}
