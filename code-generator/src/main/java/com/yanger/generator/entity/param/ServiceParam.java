package com.yanger.generator.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Description Service代码生成的参数
 * @Author yanger
 * @Date 2020/7/22 10:18
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceParam extends GenerateParam {

    /** Service接口类类名 */
    private String serviceInterfaceName;

    /** Service接口代码包 */
    private String serviceInterfacePackage;

    /** Service实现类类名 */
    private String serviceImplName;

    /** Service实现类代码包 */
    private String serviceImplPackage;

    /** Service层入参模型类型 */
    private String serviceObjName;

    /** Service层入参模型类型包名 */
    private String serviceObjPackage;

    /** dao接口类名 */
    private String daoInterfaceName;

    /** dao接口类包名 */
    private String daoInterfacePackage;

    /** 转换对象参数 */
    private ServiceConverterParam scp;

}
