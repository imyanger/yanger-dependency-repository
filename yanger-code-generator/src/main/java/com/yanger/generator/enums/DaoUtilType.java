package com.yanger.generator.enums;

import lombok.Getter;

/**
 * @Description 模型对象类型
 * @Author yanger
 * @Date 2020/7/20 18:06
 */
@Getter
public enum DaoUtilType {

    MybatisXml(1, "mybatis-xml"),

    MybatisAnnotation(2, "mybatis-annotation"),

    MybatisPlus(3, "mybatis-plus"),

    JPA(4, "jpa");

    private Integer value;

    private String desc;

    DaoUtilType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * @Description 是否doa层工具为MybatisXml
     * @author yanger
     * @date 2020/9/16 22:19
     * @param daoUtilType
     * @return boolean
     */
    public static boolean isMybatisXml(String daoUtilType){
        return MybatisXml.getValue().equals(daoUtilType) || MybatisXml.getDesc().equals(daoUtilType.toLowerCase());
    }

    /**
     * @Description 是否doa层工具为MybatisAnnotation
     * @author yanger
     * @date 2020/9/16 22:19
     * @param daoUtilType
     * @return boolean
     */
    public static boolean isMybatisAnnotation(String daoUtilType) {
        return MybatisAnnotation.getValue().equals(daoUtilType) || MybatisAnnotation.getDesc().equals(daoUtilType.toLowerCase());
    }

    /**
     * @Description 是否doa层工具为Mybatis
     * @author yanger
     * @date 2020/9/16 22:19
     * @param daoUtilType
     * @return boolean
     */
    public static boolean isMybatis(String daoUtilType) {
        return isMybatisXml(daoUtilType) || isMybatisAnnotation(daoUtilType);
    }

    /**
     * @Description 是否doa层工具为MybatisPlus
     * @author yanger
     * @date 2020/9/16 22:19
     * @param daoUtilType
     * @return boolean
     */
    public static boolean isMybatisPlus(String daoUtilType) {
        return MybatisPlus.getValue().equals(daoUtilType) || MybatisPlus.getDesc().equals(daoUtilType.toLowerCase());
    }

    /**
     * @Description 是否doa层工具为JPA
     * @author yanger
     * @date 2020/9/16 22:19
     * @param daoUtilType
     * @return boolean
     */
    public static boolean isJPA(String daoUtilType) {
        return JPA.getValue().equals(daoUtilType) || JPA.getDesc().equals(daoUtilType.toLowerCase());
    }

}
