package com.yanger.starter.basic.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 启动成功后将实现指定的提示信息, 主要用于 starter 的自动装配类
 * @Author yanger
 * @Date 2021/1/27 19:32
 */
@Getter
@AllArgsConstructor
public enum LibraryEnum {

    /** Rest library enum */
    REST("Rest", LibraryEnum.START_URL),

    /** Agent library enum */
    AGENT("Agent", "/agent/ping"),

    /** Dubbo library enum */
    DUBBO("Dubbo", ""),

    /** Druid library enum */
    DRUID("Druid", "/druid/"),

    /** Swagger rest default library enum */
    SWAGGER2_REST_DEFAULT("Swagger(D)", "/swagger-ui.html"),

    /** Swagger rest default library enum */
    SWAGGER3_REST_DEFAULT("Swagger(D)", "/swagger-ui/index.html"),

    /** Swagger rest bootstrap library enum */
    SWAGGER_REST_BOOTSTRAP("Swagger(B)", "/doc.html"),

    /** Swagger dubbo json */
    SWAGGER_DUBBO_JSON("Swagger(Dubbo API)", "/swagger-dubbo/api-docs"),

    /** Swagger dubbo library enum */
    SWAGGER_DUBBO("Swagger(Dubbo)", "/dubbo.html"),

    /** Swagger json library enum */
    SWAGGER_JSON("Swagger(API)", "/v2/api-docs");

    /** START_URL */
    public static final String START_URL = "/actuator/info";

    /** Name */
    private final String name;

    /** Uri */
    private final String uri;

}

