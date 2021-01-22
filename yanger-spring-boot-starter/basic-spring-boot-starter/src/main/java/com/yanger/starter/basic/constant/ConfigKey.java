package com.yanger.starter.basic.constant;

/**
 * @Description 所有的配置常量
 * @Author yanger
 * @Date 2020/12/21 10:01
 */
public final class ConfigKey {

    /** applicationClassName */
    public static final String APPLICATION_CLASS_NAME = "yanger.app.class.name";

    /** applicationClassName */
    public static final String  APPLICATION_NAME = "yanger.app.name";

    /** applicationType */
    public static final String APPLICATION_TYPE = "yanger.app.type";

    /** 启动方式, 用于区分是否通过 server.sh 脚本启动 */
    public static final String APPLICATION_START_TYPE = "yanger.app.start.type";

    /** spring 配置 */
    public static class SpringConfigKey {

        /** APPLICATION_NAME */
        public static final String APPLICATION_NAME = "spring.application.name";

        /** PROFILES_ACTIVE */
        public static final String PROFILES_ACTIVE = "spring.profiles.active";

    }





    /** JSON_TIME_ZONE */
    public static final String JSON_TIME_ZONE = "yanger.json.time-zone";

    /** JSON_DATE_FORMAT */
    public static final String JSON_DATE_FORMAT = "yanger.json.date-formate";

    /** POM_INFO_GROUPID */
    public static final String POM_INFO_GROUPID = "info.groupId";

    /** POM_INFO_ARTIFACTID */
    public static final String POM_INFO_ARTIFACTID = "info.artifactId";

    /** POM_INFO_VERSION */
    public static final String POM_INFO_VERSION = "info.version";



}
