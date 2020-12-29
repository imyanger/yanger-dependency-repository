package com.yanger.starter.basic.constant;

/**
 * @Description 所有的配置常量
 * @Author yanger
 * @Date 2020/12/21 10:01
 */
public final class ConfigKey {

    /** JSON_TIME_ZONE */
    public static final String JSON_TIME_ZONE = "yanger.json.time-zone";

    /** JSON_DATE_FORMAT */
    public static final String JSON_DATE_FORMAT = "yanger.json.date-formate";

    /** POM_INFO_VERSION */
    public static final String POM_INFO_VERSION = "info.version";
    /** POM_INFO_GROUPID */
    public static final String POM_INFO_GROUPID = "info.groupId";
    /** POM_INFO_ARTIFACTID */
    public static final String POM_INFO_ARTIFACTID = "info.artifactId";

    /** SERVICE_VERSION */
    public static final String SERVICE_VERSION = "yanger.boot.service.version";

    public static class SpringConfigKey {

        /** PROP_APPLICATION_NAME */
        public static final String APPLICATION_NAME = "spring.application.name";

    }

}
