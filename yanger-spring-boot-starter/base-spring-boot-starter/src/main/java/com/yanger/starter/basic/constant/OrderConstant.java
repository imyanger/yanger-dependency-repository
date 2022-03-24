package com.yanger.starter.basic.constant;

/**
 * Order 注解顺序
 * @Author yanger
 * @Date 2021/1/26 19:18
 */
public interface OrderConstant {

    /**  basic server runner order */
    int ORDER_STARTER_INFO_RUNNER = 999;

    /** configuration component runner order */
    int CONFIGURATION_COMPONENT_RUNNER = ORDER_STARTER_INFO_RUNNER - 1;

    /** swagger runner order */
    int ORDER_SWAGGER_INFO_RUNNER = ORDER_STARTER_INFO_RUNNER + 1;

    /** enum container runner order **/
    int ORDER_SERIALIZE_ENUM_CONTAINER_RUNNER = ORDER_SWAGGER_INFO_RUNNER + 1;

    /** enum dynamic log level runner order **/
    int ORDER_DYNAMIC_LOG_LEVEL_RUNNER = ORDER_SERIALIZE_ENUM_CONTAINER_RUNNER + 1;

    int getOrder();

}
