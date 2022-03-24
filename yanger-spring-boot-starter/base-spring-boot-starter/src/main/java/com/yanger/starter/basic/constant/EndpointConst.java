package com.yanger.starter.basic.constant;

/**
 * @Author yanger
 * @Date 2022/3/23/023 17:35
 */
public class EndpointConst {

    /** health check 地址 */
    public static final String HEALTH_CHECK_URL = "/health";

    /** SerializeEnumContainer api 获取地址 */
    public static final String GET_SERIALIZE_ENUM_CONTAINER_URL = "/yanger/enums";

    /** knife4j 访问地址 */
    public static final String KNIFE4J_REST_URL = "/doc.html";

    /** swagger ui 访问地址 */
    public static final String SWAGGER3_REST_URL = "/swagger-ui/index.html";

    /** 动态修改日志访问地址 */
    public static final String DYNAMIC_LOG_LEVEL_URL = "/dynamic/logLevel/{logLevel}";

}
